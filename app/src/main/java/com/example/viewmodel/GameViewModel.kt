package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GameRepository
import com.example.data.ProjectTemplates
import com.example.data.WorldData
import com.example.engine.GameEngine
import com.example.engine.IndustryEngine
import com.example.engine.TradeEngine
import com.example.model.Company
import com.example.model.EventChoice
import com.example.model.Factory
import com.example.model.FactoryType
import com.example.model.ForeignOffer
import com.example.model.GameSpeed
import com.example.model.GameState
import com.example.model.MapFilterMode
import com.example.model.NavigationTab
import com.example.model.OfferStatus
import com.example.model.OutboundAssetType
import com.example.model.OutboundInvestment
import com.example.model.OutboundRiskLevel
import com.example.model.OutboundStatus
import com.example.model.Project
import com.example.model.ProjectStatus
import com.example.model.ResourceType
import com.example.model.TaxProfile
import com.example.model.TradeContract
import com.example.engine.EconomyScaleEngine
import com.example.engine.EnergyWaterEngine
import com.example.engine.GovernmentEngine
import com.example.engine.LaborMarketEngine
import com.example.engine.ResearchEngine
import com.example.engine.MilitaryEngine
import com.example.engine.EconomyEngine
import com.example.model.PublicServiceType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GameRepository(application)
    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private var tickerJob: Job? = null

    init {
        val saved = repository.loadGame()
        if (saved != null && saved.playerCountryId != null) {
            _uiState.value = saved.copy(gameSpeed = GameSpeed.PAUSED)
        } else {
            _uiState.value = GameState(
                countries = WorldData.getInitialCountries()
            )
        }
    }

    fun selectPlayerCountry(countryId: String) {
        val initialCountries = if (_uiState.value.countries.isEmpty()) {
            WorldData.getInitialCountries()
        } else {
            _uiState.value.countries
        }
        val companies = WorldData.getInitialCompanies(countryId)
        val influence = WorldData.getInitialInfluenceNetwork(countryId, initialCountries.keys.toList())
        val market = WorldData.getInitialMarketCommodities()
        val trades = WorldData.getInitialTradeContracts(countryId)

        val playerCountry = initialCountries[countryId]
        val energy = playerCountry?.let { EnergyWaterEngine.initializeEnergyGrid(it) }
        val water = playerCountry?.let { EnergyWaterEngine.initializeWaterGrid(it) }
        val gov = playerCountry?.let { GovernmentEngine.initializeGovernmentServices(it) }
        val labor = playerCountry?.let { LaborMarketEngine.initializeLaborMarket(it) }
        val research = playerCountry?.let { ResearchEngine.initializeResearchState(it) }
        val military = playerCountry?.let { MilitaryEngine.initializeMilitaryState(it) }

        val inventory = mutableMapOf<String, Double>()
        playerCountry?.resources?.values?.forEach { res ->
            inventory[res.resourceType.code] = res.monthlyCapacityUnits * 2.5
        }

        _uiState.update { current ->
            current.copy(
                playerCountryId = countryId,
                playerGems = 150,
                countries = initialCountries,
                companies = companies,
                influenceNetwork = influence,
                marketCommodities = market,
                tradeContracts = trades,
                resourceInventory = inventory,
                energyGrid = energy,
                waterGrid = water,
                governmentServices = gov,
                laborMarket = labor,
                researchState = research,
                militaryState = military,
                gameYear = 2026,
                gameMonth = 1,
                gameSpeed = GameSpeed.PAUSED,
                currentTab = NavigationTab.MAP,
                activePendingEvent = null,
                activeNegotiationOffer = null,
                selectedCountryIdForDossier = null,
                isInventoryDialogOpen = false,
                isMonthlyReportOpen = false
            )
        }
        saveGame()
    }

    fun setGameSpeed(speed: GameSpeed) {
        _uiState.update { it.copy(gameSpeed = speed) }
        restartTicker()
    }

    private fun restartTicker() {
        tickerJob?.cancel()
        val speed = _uiState.value.gameSpeed
        if (speed == GameSpeed.PAUSED || speed.multiplier <= 0) {
            return
        }

        tickerJob = viewModelScope.launch {
            while (isActive) {
                delay(speed.multiplier)
                advanceMonth()
            }
        }
    }

    fun advanceMonthManual() {
        advanceMonth()
    }

    private fun advanceMonth() {
        _uiState.update { current ->
            val nextState = GameEngine.processMonthlyTick(current)
            if (nextState.activePendingEvent != null && current.activePendingEvent == null) {
                nextState.copy(gameSpeed = GameSpeed.PAUSED)
            } else {
                nextState
            }
        }
        if (_uiState.value.gameSpeed == GameSpeed.PAUSED) {
            tickerJob?.cancel()
        }
        saveGame()
    }

    fun setTab(tab: NavigationTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setMapFilter(mode: MapFilterMode) {
        _uiState.update { it.copy(mapFilterMode = mode) }
    }

    fun selectCountryForDossier(countryId: String?) {
        _uiState.update { it.copy(selectedCountryIdForDossier = countryId) }
    }

    fun startDomesticProject(project: Project): Boolean {
        val playerCountry = _uiState.value.playerCountry ?: return false
        if (playerCountry.treasuryBillions < project.totalCostBillions) {
            return false
        }

        _uiState.update { current ->
            val updatedCountry = playerCountry.copy(
                treasuryBillions = playerCountry.treasuryBillions - project.totalCostBillions
            )
            val updatedCountries = current.countries.toMutableMap().apply {
                put(playerCountry.id, updatedCountry)
            }
            val activeProject = project.copy(status = ProjectStatus.UNDER_CONSTRUCTION)
            val updatedProjects = current.domesticProjects + activeProject
            current.copy(
                countries = updatedCountries,
                domesticProjects = updatedProjects
            )
        }
        saveGame()
        return true
    }

    fun investInForeignCountry(project: Project): Boolean {
        val playerCountry = _uiState.value.playerCountry ?: return false
        if (playerCountry.treasuryBillions < project.totalCostBillions) {
            return false
        }

        _uiState.update { current ->
            val updatedCountry = playerCountry.copy(
                treasuryBillions = playerCountry.treasuryBillions - project.totalCostBillions
            )
            val updatedCountries = current.countries.toMutableMap().apply {
                put(playerCountry.id, updatedCountry)
            }
            val updatedForeign = current.foreignInvestments + project
            current.copy(
                countries = updatedCountries,
                foreignInvestments = updatedForeign,
                selectedCountryIdForDossier = null
            )
        }
        saveGame()
        return true
    }

    fun startOutboundInvestment(
        targetCountryId: String,
        assetType: OutboundAssetType,
        capitalBillions: Double,
        titleAr: String
    ): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        if (player.treasuryBillions < capitalBillions) return false

        val newAsset = OutboundInvestment(
            id = "outbound_${System.currentTimeMillis()}",
            targetCountryId = targetCountryId,
            titleAr = titleAr,
            assetType = assetType,
            investedCapitalBillions = capitalBillions,
            annualReturnPercent = 9.5,
            monthlyDividendsBillions = (capitalBillions * 0.095) / 12.0,
            economicInfluenceGain = (capitalBillions * 2.5).toInt().coerceIn(6, 25),
            tradeInfluenceGain = (capitalBillions * 2.0).toInt().coerceIn(4, 20),
            strategicInfluenceGain = (capitalBillions * 1.5).toInt().coerceIn(3, 15),
            diplomaticBonus = 6,
            riskLevel = OutboundRiskLevel.SAFE,
            status = OutboundStatus.ACTIVE,
            acquiredYear = _uiState.value.gameYear,
            acquiredMonth = _uiState.value.gameMonth
        )

        _uiState.update { current ->
            val updatedPlayer = player.copy(
                treasuryBillions = player.treasuryBillions - capitalBillions
            )
            val updatedCountries = current.countries.toMutableMap().apply {
                put(player.id, updatedPlayer)
            }
            val updatedOutbound = current.outboundInvestments + newAsset

            // Gain influence in host country
            val updatedInfluence = current.influenceNetwork.map { entry ->
                if (entry.sourceCountryId == player.id && entry.targetCountryId == targetCountryId) {
                    entry.copy(
                        economicInfluence = (entry.economicInfluence + newAsset.economicInfluenceGain).coerceAtMost(100),
                        commercialInfluence = (entry.commercialInfluence + newAsset.tradeInfluenceGain).coerceAtMost(100),
                        strategicInfluence = (entry.strategicInfluence + newAsset.strategicInfluenceGain).coerceAtMost(100),
                        diplomaticInfluence = (entry.diplomaticInfluence + newAsset.diplomaticBonus).coerceAtMost(100)
                    )
                } else entry
            }

            current.copy(
                countries = updatedCountries,
                outboundInvestments = updatedOutbound,
                influenceNetwork = updatedInfluence,
                selectedCountryIdForDossier = null
            )
        }
        saveGame()
        return true
    }

    fun openNegotiation(offer: ForeignOffer) {
        _uiState.update { it.copy(activeNegotiationOffer = offer, gameSpeed = GameSpeed.PAUSED) }
        tickerJob?.cancel()
    }

    fun closeNegotiation() {
        _uiState.update { it.copy(activeNegotiationOffer = null) }
    }

    fun updateNegotiationSliders(
        stateEquity: Double,
        taxRate: Double,
        localJobQuota: Double,
        taxHolidayYears: Int
    ) {
        updateExtendedNegotiationTerms(
            stateEquity = stateEquity,
            taxRate = taxRate,
            localJobQuota = localJobQuota,
            taxHolidayYears = taxHolidayYears,
            contractYears = _uiState.value.activeNegotiationOffer?.negotiatedContractYears ?: 15,
            localProductionRatio = _uiState.value.activeNegotiationOffer?.negotiatedLocalProductionRatio ?: 0.40,
            exportRatio = _uiState.value.activeNegotiationOffer?.negotiatedExportRatio ?: 0.50,
            hasTechTransfer = _uiState.value.activeNegotiationOffer?.hasTechTransferClause ?: true,
            hasLocalRdCenter = _uiState.value.activeNegotiationOffer?.hasLocalRdCenter ?: false,
            hasLocalSuppliers = _uiState.value.activeNegotiationOffer?.hasLocalSuppliersCommitment ?: true
        )
    }

    fun updateExtendedNegotiationTerms(
        stateEquity: Double,
        taxRate: Double,
        localJobQuota: Double,
        taxHolidayYears: Int,
        contractYears: Int,
        localProductionRatio: Double,
        exportRatio: Double,
        hasTechTransfer: Boolean,
        hasLocalRdCenter: Boolean,
        hasLocalSuppliers: Boolean
    ) {
        val currentOffer = _uiState.value.activeNegotiationOffer ?: return
        val hostCountry = _uiState.value.playerCountry ?: return

        val updatedOffer = currentOffer.copy(
            negotiatedStateEquity = stateEquity,
            negotiatedTaxRate = taxRate,
            negotiatedLocalJobQuota = localJobQuota,
            negotiatedTaxHolidayYears = taxHolidayYears,
            negotiatedContractYears = contractYears,
            negotiatedLocalProductionRatio = localProductionRatio,
            negotiatedExportRatio = exportRatio,
            hasTechTransferClause = hasTechTransfer,
            hasLocalRdCenter = hasLocalRdCenter,
            hasLocalSuppliersCommitment = hasLocalSuppliers
        )
        val (likelihood, feedback) = GameEngine.evaluateCounterOffer(updatedOffer, hostCountry)

        val finalOffer = updatedOffer.copy(
            aiAcceptanceLikelihood = likelihood,
            aiFeedbackMessage = feedback
        )

        _uiState.update { it.copy(activeNegotiationOffer = finalOffer) }
    }

    fun acceptForeignOffer(offer: ForeignOffer) {
        val player = _uiState.value.playerCountry ?: return

        _uiState.update { current ->
            val updatedOffers = current.foreignOffers.map {
                if (it.id == offer.id) it.copy(status = OfferStatus.ACCEPTED) else it
            }

            val upfrontCapital = offer.investmentValueBillions * offer.negotiatedStateEquity * 0.35
            val updatedPlayer = player.copy(
                gdpBillions = player.gdpBillions + (offer.investmentValueBillions * 0.85),
                industryIndex = (player.industryIndex + 6).coerceAtMost(99),
                infrastructureIndex = (player.infrastructureIndex + 4).coerceAtMost(99),
                treasuryBillions = player.treasuryBillions + upfrontCapital
            )

            // Foreign investor gains influence in player's country
            val updatedInfluence = current.influenceNetwork.map { entry ->
                if (entry.sourceCountryId == offer.proposingCountryId && entry.targetCountryId == player.id) {
                    entry.copy(
                        economicInfluence = (entry.economicInfluence + 14).coerceAtMost(100),
                        commercialInfluence = (entry.commercialInfluence + 10).coerceAtMost(100),
                        strategicInfluence = (entry.strategicInfluence + offer.strategicInfluenceBoost).coerceAtMost(100)
                    )
                } else {
                    entry
                }
            }

            // Create Joint Venture company from the accepted FDI agreement
            val jvCompany = Company(
                id = "jv_${System.currentTimeMillis()}",
                nameAr = "${offer.foreignCompanyName} - الشراكة الوطنية",
                sector = offer.category.titleAr,
                originCountryId = player.id,
                marketValueBillions = offer.investmentValueBillions,
                capitalBillions = offer.investmentValueBillions * 0.5,
                annualProfitMillions = offer.investmentValueBillions * 140.0,
                stateOwnershipPercent = offer.negotiatedStateEquity,
                employeesCount = offer.jobsOffered,
                isStateOwned = offer.negotiatedStateEquity >= 0.50,
                isJointVentures = true,
                foreignPartnerName = offer.foreignCompanyName,
                productionOutputIndex = 75,
                technologyLevel = if (offer.hasTechTransferClause) 85 else 70,
                icon = offer.category.icon
            )

            current.copy(
                foreignOffers = updatedOffers,
                activeNegotiationOffer = null,
                companies = current.companies + jvCompany,
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) },
                influenceNetwork = updatedInfluence
            )
        }
        saveGame()
    }

    fun rejectForeignOffer(offer: ForeignOffer) {
        _uiState.update { current ->
            val updatedOffers = current.foreignOffers.map {
                if (it.id == offer.id) it.copy(status = OfferStatus.REJECTED) else it
            }
            current.copy(foreignOffers = updatedOffers, activeNegotiationOffer = null)
        }
        saveGame()
    }

    fun resolveActiveEvent(choice: EventChoice) {
        val player = _uiState.value.playerCountry ?: return

        _uiState.update { current ->
            var updatedTreasury = player.treasuryBillions - choice.costBillions + choice.treasuryBonusBillions
            val updatedDebt = if (updatedTreasury < 0.0) {
                val deficit = -updatedTreasury
                updatedTreasury = 0.0
                player.sovereignDebtBillions + deficit
            } else {
                player.sovereignDebtBillions
            }

            val updatedGdp = player.gdpBillions * (1.0 + (choice.gdpChangePercent / 100.0))
            val updatedStability = (player.stability + choice.stabilityChange).coerceIn(10, 99)
            val updatedInflation = (player.inflationRate + choice.inflationChange).coerceAtLeast(0.5)

            val updatedPlayer = player.copy(
                treasuryBillions = updatedTreasury,
                sovereignDebtBillions = updatedDebt,
                gdpBillions = updatedGdp,
                stability = updatedStability,
                inflationRate = updatedInflation
            )

            val event = current.activePendingEvent
            val newHistory = if (event != null) current.eventHistory + event else current.eventHistory

            current.copy(
                activePendingEvent = null,
                eventHistory = newHistory,
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
    }

    fun updateTaxRate(newRate: Double) {
        val player = _uiState.value.playerCountry ?: return
        _uiState.update { current ->
            val updatedPlayer = player.copy(taxRate = newRate.coerceIn(0.05, 0.45))
            current.copy(
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
    }

    fun payDownNationalDebt(amountBillions: Double): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        if (player.treasuryBillions < amountBillions || player.sovereignDebtBillions <= 0.0) return false

        val actualPayment = amountBillions.coerceAtMost(player.sovereignDebtBillions)
        _uiState.update { current ->
            val updatedPlayer = player.copy(
                treasuryBillions = player.treasuryBillions - actualPayment,
                sovereignDebtBillions = player.sovereignDebtBillions - actualPayment,
                stability = (player.stability + 2).coerceAtMost(99)
            )
            current.copy(
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
        return true
    }

    fun injectCapitalToCompany(companyId: String, amountBillions: Double): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        val company = _uiState.value.companies.find { it.id == companyId } ?: return false
        if (player.treasuryBillions < amountBillions) return false

        _uiState.update { current ->
            val updatedCompany = company.copy(
                capitalBillions = company.capitalBillions + amountBillions,
                marketValueBillions = company.marketValueBillions + (amountBillions * 1.4),
                annualProfitMillions = company.annualProfitMillions + (amountBillions * 110.0),
                productionOutputIndex = (company.productionOutputIndex + 6).coerceAtMost(99)
            )
            val updatedCompanies = current.companies.map { if (it.id == companyId) updatedCompany else it }
            val updatedPlayer = player.copy(treasuryBillions = player.treasuryBillions - amountBillions)

            current.copy(
                companies = updatedCompanies,
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
        return true
    }

    fun expandCompanyProduction(companyId: String, costBillions: Double): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        val company = _uiState.value.companies.find { it.id == companyId } ?: return false
        if (player.treasuryBillions < costBillions) return false

        _uiState.update { current ->
            val updatedCompany = company.copy(
                productionOutputIndex = (company.productionOutputIndex + 10).coerceAtMost(99),
                employeesCount = company.employeesCount + 4500,
                annualProfitMillions = company.annualProfitMillions + (costBillions * 90.0)
            )
            val updatedCompanies = current.companies.map { if (it.id == companyId) updatedCompany else it }
            val updatedPlayer = player.copy(
                treasuryBillions = player.treasuryBillions - costBillions,
                gdpBillions = player.gdpBillions + (costBillions * 1.8),
                industrialProduction = (player.industrialProduction + 3).coerceAtMost(99)
            )

            current.copy(
                companies = updatedCompanies,
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
        return true
    }

    fun privatizeCompany(companyId: String): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        val company = _uiState.value.companies.find { it.id == companyId } ?: return false
        if (!company.isStateOwned || company.stateOwnershipPercent <= 0.1) return false

        val stakeToSell = 0.35.coerceAtMost(company.stateOwnershipPercent)
        val proceeds = company.marketValueBillions * stakeToSell

        _uiState.update { current ->
            val updatedCompany = company.copy(
                stateOwnershipPercent = company.stateOwnershipPercent - stakeToSell,
                isStateOwned = (company.stateOwnershipPercent - stakeToSell) >= 0.50
            )
            val updatedCompanies = current.companies.map { if (it.id == companyId) updatedCompany else it }
            val updatedPlayer = player.copy(treasuryBillions = player.treasuryBillions + proceeds)

            current.copy(
                companies = updatedCompanies,
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
        return true
    }

    fun foundNewCompany(name: String, sector: String, capitalBillions: Double): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        if (player.treasuryBillions < capitalBillions) return false

        val newCompany = Company(
            id = "comp_user_${System.currentTimeMillis()}",
            nameAr = name,
            sector = sector,
            originCountryId = player.id,
            marketValueBillions = capitalBillions * 2.2,
            annualProfitMillions = capitalBillions * 120.0,
            stateOwnershipPercent = 1.0,
            employeesCount = (capitalBillions * 4000).toInt(),
            isStateOwned = true,
            icon = "🏢",
            capitalBillions = capitalBillions,
            productionOutputIndex = 70
        )

        _uiState.update { current ->
            val updatedPlayer = player.copy(treasuryBillions = player.treasuryBillions - capitalBillions)
            current.copy(
                companies = current.companies + newCompany,
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
        return true
    }

    fun improveRelations(targetCountryId: String): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        val cost = 0.6
        if (player.treasuryBillions < cost) return false

        _uiState.update { current ->
            val target = current.countries[targetCountryId] ?: return@update current
            val updatedTarget = target.copy(relationsWithPlayer = (target.relationsWithPlayer + 15).coerceAtMost(100))
            val updatedPlayer = player.copy(treasuryBillions = player.treasuryBillions - cost)

            val updatedInfluence = current.influenceNetwork.map { entry ->
                if (entry.sourceCountryId == player.id && entry.targetCountryId == targetCountryId) {
                    entry.copy(
                        diplomaticInfluence = (entry.diplomaticInfluence + 12).coerceAtMost(100),
                        culturalInfluence = (entry.culturalInfluence + 6).coerceAtMost(100)
                    )
                } else entry
            }

            current.copy(
                countries = current.countries.toMutableMap().apply {
                    put(player.id, updatedPlayer)
                    put(target.id, updatedTarget)
                },
                influenceNetwork = updatedInfluence
            )
        }
        saveGame()
        return true
    }

    fun setMapFilterMode(mode: MapFilterMode) {
        _uiState.update { it.copy(mapFilterMode = mode) }
    }

    fun updateTaxProfile(incomeTax: Double, corporateTax: Double, vat: Double, tariff: Double) {
        val player = _uiState.value.playerCountry ?: return
        val newProfile = TaxProfile(
            incomeTaxRate = incomeTax.coerceIn(0.0, 0.60),
            corporateTaxRate = corporateTax.coerceIn(0.0, 0.50),
            vatRate = vat.coerceIn(0.0, 0.35),
            importTariffRate = tariff.coerceIn(0.0, 0.40)
        )
        val updatedPlayer = player.copy(
            taxProfile = newProfile,
            taxRate = newProfile.effectiveTaxRate
        )
        _uiState.update { current ->
            current.copy(
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
    }

    fun updateTaxProfile(newProfile: TaxProfile) {
        val player = _uiState.value.playerCountry ?: return
        val updatedPlayer = player.copy(
            taxProfile = newProfile,
            taxRate = newProfile.effectiveTaxRate
        )
        _uiState.update { current ->
            current.copy(
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
    }

    fun buildFactory(type: FactoryType, customName: String? = null): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        val result = IndustryEngine.buildFactory(player, type, customName) ?: return false
        val (updatedPlayer, _) = result

        _uiState.update { current ->
            current.copy(
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
        return true
    }

    fun upgradeFactory(factoryId: String): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        val updatedPlayer = IndustryEngine.upgradeFactory(player, factoryId) ?: return false

        _uiState.update { current ->
            current.copy(
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
        return true
    }

    fun toggleFactoryPause(factoryId: String): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        val updatedPlayer = IndustryEngine.toggleFactoryPause(player, factoryId)

        _uiState.update { current ->
            current.copy(
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
        return true
    }

    fun privatizeFactory(factoryId: String, stakeRatio: Double = 0.49): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        val updatedPlayer = IndustryEngine.privatizeFactory(player, factoryId, stakeRatio) ?: return false

        _uiState.update { current ->
            current.copy(
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
        return true
    }

    fun createTradeContract(
        sellerCountryId: String,
        resourceType: ResourceType,
        monthlyQuantity: Double,
        durationMonths: Int
    ): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        val seller = _uiState.value.countries[sellerCountryId] ?: return false
        val marketCommodity = _uiState.value.marketCommodities[resourceType.name]
        val agreedPrice = marketCommodity?.currentPrice ?: resourceType.defaultBasePrice

        val contract = TradeEngine.createTradeContract(
            buyerCountryId = player.id,
            sellerCountry = seller,
            resourceType = resourceType,
            monthlyQuantity = monthlyQuantity,
            marketPrice = agreedPrice,
            durationMonths = durationMonths,
            tariffPercent = player.taxProfile.importTariffRate
        )

        _uiState.update { current ->
            current.copy(tradeContracts = current.tradeContracts + contract)
        }
        saveGame()
        return true
    }

    fun buySpotCommodity(resourceType: ResourceType, quantityThousands: Double): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        val commodity = _uiState.value.marketCommodities[resourceType.name]
        val price = commodity?.currentPrice ?: resourceType.defaultBasePrice

        val updatedPlayer = TradeEngine.buySpotCommodity(player, resourceType, quantityThousands, price) ?: return false

        _uiState.update { current ->
            current.copy(
                countries = current.countries.toMutableMap().apply { put(player.id, updatedPlayer) }
            )
        }
        saveGame()
        return true
    }

    fun toggleResourceInventory(open: Boolean) {
        _uiState.update { it.copy(isInventoryDialogOpen = open) }
    }

    fun toggleMonthlyReport(open: Boolean) {
        _uiState.update { it.copy(isMonthlyReportOpen = open) }
    }

    fun startResearch(techId: String) {
        _uiState.update { current ->
            val research = current.researchState ?: return@update current
            val updated = ResearchEngine.selectTechToResearch(research, techId)
            current.copy(researchState = updated)
        }
        saveGame()
    }

    fun recruitScientists(count: Int = 200): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        val costBillions = (count * 0.005) / 1000.0
        if (player.treasuryBillions < costBillions) return false

        _uiState.update { current ->
            val research = current.researchState ?: return@update current
            val updatedCountry = player.copy(treasuryBillions = player.treasuryBillions - costBillions)
            current.copy(
                countries = current.countries.toMutableMap().apply { put(player.id, updatedCountry) },
                researchState = research.copy(scientistsCount = research.scientistsCount + count)
            )
        }
        saveGame()
        return true
    }

    fun recruitSoldiers(count: Int = 5000): Boolean {
        val player = _uiState.value.playerCountry ?: return false
        val costBillions = (count * 0.004) / 1000.0
        if (player.treasuryBillions < costBillions) return false

        _uiState.update { current ->
            val military = current.militaryState ?: return@update current
            val updatedCountry = player.copy(treasuryBillions = player.treasuryBillions - costBillions)
            current.copy(
                countries = current.countries.toMutableMap().apply { put(player.id, updatedCountry) },
                militaryState = military.copy(activeSoldiersCount = military.activeSoldiersCount + count)
            )
        }
        saveGame()
        return true
    }

    fun adjustMilitaryBudget(personnelDeltaM: Double, maintDeltaM: Double, trainingDeltaM: Double) {
        _uiState.update { current ->
            val military = current.militaryState ?: return@update current
            val updated = military.copy(
                monthlyPersonnelBudgetMillions = (military.monthlyPersonnelBudgetMillions + personnelDeltaM).coerceAtLeast(0.5),
                monthlyMaintenanceBudgetMillions = (military.monthlyMaintenanceBudgetMillions + maintDeltaM).coerceAtLeast(0.5),
                monthlyTrainingBudgetMillions = (military.monthlyTrainingBudgetMillions + trainingDeltaM).coerceAtLeast(0.2)
            )
            current.copy(militaryState = updated)
        }
        saveGame()
    }

    fun adjustPublicServiceFunding(serviceType: PublicServiceType, fundingPercent: Int) {
        _uiState.update { current ->
            val gov = current.governmentServices ?: return@update current
            val service = gov.services[serviceType] ?: return@update current
            val updatedService = service.copy(fundingLevelPercent = fundingPercent.coerceIn(50, 160))
            val updatedServices = gov.services.toMutableMap().apply { put(serviceType, updatedService) }
            current.copy(governmentServices = gov.copy(services = updatedServices))
        }
        saveGame()
    }

    fun previewPolicyChange(policyNameAr: String, deltaRatePercent: Double) {
        val player = _uiState.value.playerCountry ?: return
        val preview = EconomyEngine.previewPolicyChange(player, policyNameAr, deltaRatePercent)
        _uiState.update { it.copy(activePolicyPreview = preview) }
    }

    fun dismissPolicyPreview() {
        _uiState.update { it.copy(activePolicyPreview = null) }
    }

    fun useGemsForSpeedup(months: Int = 1, gemCost: Int = 15): Boolean {
        val currentGems = _uiState.value.playerGems
        if (currentGems < gemCost) return false

        _uiState.update { current ->
            val updatedDomestic = current.domesticProjects.map { proj ->
                if (proj.status == ProjectStatus.UNDER_CONSTRUCTION) {
                    val rem = (proj.remainingMonths - months).coerceAtLeast(0)
                    proj.copy(
                        remainingMonths = rem,
                        isCompleted = rem == 0,
                        status = if (rem == 0) ProjectStatus.COMPLETED else ProjectStatus.UNDER_CONSTRUCTION
                    )
                } else proj
            }
            current.copy(
                playerGems = currentGems - gemCost,
                domesticProjects = updatedDomestic
            )
        }
        saveGame()
        return true
    }

    fun resetGame() {
        tickerJob?.cancel()
        repository.clearSavedGame()
        _uiState.value = GameState(countries = WorldData.getInitialCountries())
    }

    private fun saveGame() {
        repository.saveGame(_uiState.value)
    }

    override fun onCleared() {
        super.onCleared()
        tickerJob?.cancel()
    }
}
