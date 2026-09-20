package com.example.engine

import com.example.data.GameEventsData
import com.example.model.Company
import com.example.model.Country
import com.example.model.CountryArchetype
import com.example.model.DiplomaticRelationStatus
import com.example.model.ForeignOffer
import com.example.model.GameEvent
import com.example.model.GameState
import com.example.model.InfluenceEntry
import com.example.model.InfluenceHistoryPoint
import com.example.model.InvestorType
import com.example.model.MarketCommodity
import com.example.model.MonthlyBudget
import com.example.model.OfferStatus
import com.example.model.OutboundInvestment
import com.example.model.OutboundRiskLevel
import com.example.model.OutboundStatus
import com.example.model.Project
import com.example.model.ProjectCategory
import com.example.model.ProjectStatus
import com.example.model.TradeContract
import kotlin.math.max
import kotlin.random.Random

object GameEngine {

    fun calculateMonthlyBudget(
        country: Country,
        companies: List<Company>,
        foreignProjects: List<Project>,
        outboundInvestments: List<OutboundInvestment> = emptyList(),
        tradeContracts: List<TradeContract> = emptyList(),
        marketCommodities: Map<String, MarketCommodity> = emptyMap()
    ): MonthlyBudget {
        return EconomyEngine.calculateMonthlyBudget(
            country = country,
            companies = companies,
            foreignProjects = foreignProjects,
            outboundInvestments = outboundInvestments,
            tradeContracts = tradeContracts,
            marketCommodities = marketCommodities
        )
    }

    fun processMonthlyTick(state: GameState): GameState {
        val playerCountryId = state.playerCountryId ?: return state
        val player = state.countries[playerCountryId] ?: return state

        // 1. Advance Calendar
        var nextMonth = state.gameMonth + 1
        var nextYear = state.gameYear
        if (nextMonth > 12) {
            nextMonth = 1
            nextYear += 1
        }

        // 2. Process Global Market Commodities (Supply/Demand dynamic pricing)
        val updatedMarketCommodities = MarketEngine.updateCommodityPrices(state.marketCommodities)

        // 3. Process Trade Contracts (Fulfillment, Transport, Tariffs, Seller/Buyer balances)
        val (contractsAfterTrade, countriesAfterTrade) = TradeEngine.processMonthlyTradeContracts(
            contracts = state.tradeContracts,
            countries = state.countries
        )

        // 4. Process Domestic Projects
        val updatedDomestic = mutableListOf<Project>()
        var playerGdpDelta = 0.0
        var playerIndustryDelta = 0
        var playerInfraDelta = 0
        var playerEnergyDelta = 0
        var playerAgriDelta = 0
        var playerEduDelta = 0
        var playerHealthDelta = 0
        var playerStabilityDelta = 0
        var playerTradePowerDelta = 0.0
        var playerJobsCreatedMonth = 0

        for (proj in state.domesticProjects) {
            if (proj.status == ProjectStatus.UNDER_CONSTRUCTION) {
                val rem = proj.remainingMonths - 1
                if (rem <= 0) {
                    val completed = proj.copy(
                        remainingMonths = 0,
                        isCompleted = true,
                        status = ProjectStatus.COMPLETED
                    )
                    updatedDomestic.add(completed)
                    playerGdpDelta += completed.gdpBoostBillions
                    playerIndustryDelta += completed.industryBoost
                    playerInfraDelta += completed.infrastructureBoost
                    playerEnergyDelta += completed.energyBoost
                    playerAgriDelta += completed.agricultureBoost
                    playerEduDelta += completed.educationBoost
                    playerHealthDelta += completed.healthcareBoost
                    playerStabilityDelta += completed.stabilityBoost
                    playerTradePowerDelta += completed.tradePowerBoost
                    playerJobsCreatedMonth += completed.jobsCreated
                } else {
                    updatedDomestic.add(proj.copy(remainingMonths = rem))
                }
            } else {
                updatedDomestic.add(proj)
            }
        }

        // 5. Process Foreign Projects & Outbound Investments
        val updatedForeign = mutableListOf<Project>()
        val influenceGains = mutableMapOf<Pair<String, String>, Int>()

        for (proj in state.foreignInvestments) {
            if (!proj.isCompleted) {
                val rem = proj.remainingMonths - 1
                if (rem <= 0) {
                    updatedForeign.add(proj.copy(remainingMonths = 0, isCompleted = true, status = ProjectStatus.COMPLETED))
                    val key = Pair(proj.investorCountryId, proj.targetCountryId)
                    influenceGains[key] = (influenceGains[key] ?: 0) + proj.influenceGain
                } else {
                    updatedForeign.add(proj.copy(remainingMonths = rem))
                }
            } else {
                updatedForeign.add(proj)
            }
        }

        val updatedOutbound = state.outboundInvestments.map { inv ->
            val hostCountry = countriesAfterTrade[inv.targetCountryId]
            if (hostCountry != null && hostCountry.relationsWithPlayer < 20 && inv.status == OutboundStatus.ACTIVE) {
                inv.copy(status = OutboundStatus.THREATENED, riskLevel = OutboundRiskLevel.CRITICAL)
            } else {
                inv
            }
        }

        // 6. Update Influence Network
        val updatedInfluence = state.influenceNetwork.map { entry ->
            val gain = influenceGains[Pair(entry.sourceCountryId, entry.targetCountryId)] ?: 0
            if (gain > 0) {
                entry.copy(
                    economicInfluence = (entry.economicInfluence + gain).coerceAtMost(100),
                    commercialInfluence = (entry.commercialInfluence + gain / 2).coerceAtMost(100),
                    strategicInfluence = (entry.strategicInfluence + gain / 3).coerceAtMost(100)
                )
            } else {
                entry
            }
        }

        // 7. Process Factories for All Countries (Material shortage check, output, dividends)
        val countriesWithFactories = countriesAfterTrade.mapValues { (_, c) ->
            val processedFacts = IndustryEngine.processMonthlyFactories(c, contractsAfterTrade)
            c.copy(factories = processedFacts)
        }

        // 8. Autonomous AI Simulation for Non-Player Nations
        val (countriesAfterAI, contractsAfterAI) = AIEngine.processAITick(
            nonPlayerCountries = countriesWithFactories,
            playerCountryId = playerCountryId,
            activeContracts = contractsAfterTrade
        )

        // 9. Update Macro-Economy & Financials for Player and All Countries
        val updatedCountries = countriesAfterAI.toMutableMap()
        for ((cId, country) in countriesAfterAI) {
            val isPlayer = (cId == playerCountryId)
            val budget = EconomyEngine.calculateMonthlyBudget(
                country = country,
                companies = state.companies,
                foreignProjects = updatedForeign,
                outboundInvestments = if (isPlayer) updatedOutbound else emptyList(),
                tradeContracts = contractsAfterAI,
                marketCommodities = updatedMarketCommodities
            )
            val netCash = budget.netCashflow

            var newTreasury = country.treasuryBillions + netCash
            var newDebt = country.sovereignDebtBillions

            if (newTreasury < 0.0) {
                newDebt += (-newTreasury)
                newTreasury = 0.0
            } else if (netCash > 1.0 && newDebt > 0.0) {
                val debtPayoff = (netCash * 0.15).coerceAtMost(newDebt)
                newDebt -= debtPayoff
                newTreasury -= debtPayoff
            }

            val newGrowth = EconomyEngine.calculateNewGdpGrowth(country, budget, country.activeFactories.size)
            val monthlyBaseGrowth = (newGrowth / 100.0) / 12.0
            var newGdp = country.gdpBillions * (1.0 + monthlyBaseGrowth)

            val newInflation = EconomyEngine.calculateNewInflation(country, budget)
            val newUnemployment = EconomyEngine.calculateNewUnemployment(
                country = country,
                gdpGrowth = newGrowth,
                totalWorkers = country.factories.filter { !it.isPaused }.sumOf { it.actualWorkers }
            )
            val newStability = EconomyEngine.calculateNewStability(country, budget)

            var newIndustry = country.industryIndex
            var newInfra = country.infrastructureIndex
            var newEnergy = country.energyIndex
            var newAgri = country.agricultureIndex
            var newEdu = country.educationIndex
            var newHealth = country.healthIndex
            var newTradePower = country.tradePower

            if (isPlayer) {
                newGdp += (playerGdpDelta / 12.0)
                newIndustry = (newIndustry + playerIndustryDelta).coerceIn(10, 99)
                newInfra = (newInfra + playerInfraDelta).coerceIn(10, 99)
                newEnergy = (newEnergy + playerEnergyDelta).coerceIn(10, 99)
                newAgri = (newAgri + playerAgriDelta).coerceIn(10, 99)
                newEdu = (newEdu + playerEduDelta).coerceIn(10, 99)
                newHealth = (newHealth + playerHealthDelta).coerceIn(10, 99)
                newTradePower = (newTradePower + playerTradePowerDelta).coerceIn(10.0, 99.0)
            }

            // Recalculate 5 Pillars of Influence
            val newEconInf = ((newGdp / 300.0) * 15.0 + (newIndustry * 0.35)).toInt().coerceIn(10, 99)
            val newTradeInf = (newTradePower * 0.7 + (newInfra * 0.3)).toInt().coerceIn(10, 99)
            val newDiplInf = (newStability * 0.5 + country.relationsWithPlayer * 0.5).toInt().coerceIn(10, 99)
            val newStratInf = ((newEnergy * 0.4) + (country.militaryIndex * 0.4) + (newInfra * 0.2)).toInt().coerceIn(10, 99)
            val newCultInf = (newEdu * 0.6 + newStability * 0.4).toInt().coerceIn(10, 99)

            updatedCountries[cId] = country.copy(
                gdpBillions = newGdp,
                gdpGrowthPercent = newGrowth,
                treasuryBillions = newTreasury,
                sovereignDebtBillions = newDebt,
                inflationRate = newInflation,
                unemploymentRate = newUnemployment,
                stability = newStability,
                industryIndex = newIndustry,
                infrastructureIndex = newInfra,
                energyIndex = newEnergy,
                agricultureIndex = newAgri,
                educationIndex = newEdu,
                healthIndex = newHealth,
                tradePower = newTradePower,
                monthlyRevenue = budget.totalRevenue,
                monthlyExpenses = budget.totalExpense,
                economicInfluence = newEconInf,
                tradeInfluence = newTradeInf,
                diplomaticInfluence = newDiplInf,
                strategicInfluence = newStratInf,
                culturalInfluence = newCultInf
            )
        }

        // 6. Foreign Offers Generation with Diversified Investor Types
        val updatedOffers = state.foreignOffers.toMutableList()
        val hasPendingOffer = updatedOffers.any { it.status == OfferStatus.PENDING || it.status == OfferStatus.NEGOTIATING }
        if (!hasPendingOffer && (nextMonth % 2 == 0 || updatedOffers.isEmpty())) {
            val potentialSenders = updatedCountries.keys.filter { it != playerCountryId }
            if (potentialSenders.isNotEmpty()) {
                val senderId = potentialSenders.random()
                val senderCountry = updatedCountries[senderId]
                val newOffer = createDiversifiedForeignOffer(senderId, senderCountry?.flag ?: "🌐", senderCountry?.nameAr ?: "")
                updatedOffers.add(0, newOffer)
            }
        }

        // 7. Dynamic Geopolitical & State-Aware Economic Events
        var pendingEvent: GameEvent? = state.activePendingEvent
        if (pendingEvent == null && (nextMonth % 4 == 0 || Random.nextInt(1, 100) <= 22)) {
            val playerLatest = updatedCountries[playerCountryId] ?: player
            pendingEvent = generateStateAwareEvent(playerLatest, nextYear, nextMonth)
        }

        // 8. Track Metric Histories for Charts & Analytics
        val playerCurrent = updatedCountries[playerCountryId] ?: player
        val updatedGdpHistory = (state.gdpHistory + playerCurrent.gdpBillions).takeLast(24)
        val updatedTreasuryHistory = (state.treasuryHistory + playerCurrent.treasuryBillions).takeLast(24)
        val updatedInflationHistory = (state.inflationHistory + playerCurrent.inflationRate).takeLast(24)
        val updatedInfluenceHistory = (state.influenceHistory + InfluenceHistoryPoint(
            year = nextYear,
            month = nextMonth,
            totalScore = playerCurrent.nationalPowerScore,
            economicPart = playerCurrent.economicInfluence,
            tradePart = playerCurrent.tradeInfluence,
            diplomaticPart = playerCurrent.diplomaticInfluence,
            strategicPart = playerCurrent.strategicInfluence,
            culturalPart = playerCurrent.culturalInfluence
        )).takeLast(24)

        // 9. Update Integrated Pillars: Energy, Water, Labor, Gov, Research, Military
        val currentEnergy = state.energyGrid ?: EnergyWaterEngine.initializeEnergyGrid(playerCurrent)
        val updatedEnergy = EnergyWaterEngine.calculateMonthlyEnergy(currentEnergy, playerCurrent, playerCurrent.factories)

        val currentWater = state.waterGrid ?: EnergyWaterEngine.initializeWaterGrid(playerCurrent)
        val updatedWater = EnergyWaterEngine.calculateMonthlyWater(currentWater, playerCurrent, playerCurrent.factories.size)

        val currentGov = state.governmentServices ?: GovernmentEngine.initializeGovernmentServices(playerCurrent)
        val updatedGov = GovernmentEngine.processMonthlyGovernmentTick(currentGov, playerCurrent)

        val currentLabor = state.laborMarket ?: LaborMarketEngine.initializeLaborMarket(playerCurrent)
        val updatedLabor = LaborMarketEngine.processMonthlyLaborTick(
            current = currentLabor,
            country = playerCurrent,
            healthSatisfactionBonus = if (playerCurrent.healthIndex > 70) 2 else 0,
            educationSatisfactionBonus = if (playerCurrent.educationIndex > 70) 2 else 0,
            powerShortagePenalty = if (updatedEnergy.hasShortage) 5 else 0,
            waterShortagePenalty = if (updatedWater.hasWaterShortage) 4 else 0
        )

        val currentResearch = state.researchState ?: ResearchEngine.initializeResearchState(playerCurrent)
        val updatedResearch = ResearchEngine.processMonthlyResearchTick(currentResearch, playerCurrent)

        val currentMilitary = state.militaryState ?: MilitaryEngine.initializeMilitaryState(playerCurrent)
        val updatedMilitary = MilitaryEngine.processMonthlyMilitaryTick(currentMilitary, playerCurrent)

        // Update Resource Stockpiles & Inventory
        val updatedInventory = state.resourceInventory.toMutableMap()
        for ((_, res) in playerCurrent.resources) {
            val code = res.resourceType.code
            val prevStock = updatedInventory[code] ?: (res.monthlyCapacityUnits * 2.0)
            updatedInventory[code] = prevStock + res.currentProductionUnits
        }

        // Generate Monthly Report with causality breakdown (Section 56 & 57)
        val gdpDelta = playerCurrent.gdpBillions - player.gdpBillions
        val gdpPct = if (player.gdpBillions > 0) (gdpDelta / player.gdpBillions) * 100.0 else 0.0
        val revM = playerCurrent.monthlyRevenue * 1000.0
        val expM = playerCurrent.monthlyExpenses * 1000.0
        val netM = revM - expM

        val gdpCauses = mutableListOf<String>()
        if (playerGdpDelta > 0) gdpCauses.add("المشاريع المكتملة +${String.format("%.1f", playerGdpDelta)}B$")
        if (playerCurrent.tradePower >= player.tradePower) gdpCauses.add("نمو الصادرات والتجارة الدولية +1.1%")
        if (playerCurrent.factories.any { !it.isPaused }) gdpCauses.add("الإنتاج والتحويل الصناعي +0.8%")
        if (updatedEnergy.hasShortage) gdpCauses.add("عجز شبكة الكهرباء والطاقة -0.6%")
        if (gdpCauses.isEmpty()) gdpCauses.add("استقرار الاستهلاك المحلي والخدمات +0.5%")

        val monthlyReport = com.example.model.MonthlyReport(
            monthNumber = nextMonth,
            year = nextYear,
            gdpGrowthPercent = gdpPct,
            gdpCauses = gdpCauses,
            monthlyRevenueMillions = revM,
            monthlyExpenseMillions = expM,
            netSurplusOrDeficitMillions = netM,
            revenueCauses = listOf(
                "عوائد الضرائب والرسوم الجمركية",
                "أرباح المصانع والشركات السيادية",
                "عوائد صادرات النفط والمعادن والطاقة"
            ),
            expenseCauses = listOf(
                "رواتب موظفي الدولة والخدمات المدنية",
                "موازنة الدفاع والأمن والجاهزية",
                "صيانة الشبكات والبنية التحتية وفوائد الدين"
            ),
            newJobsCount = playerJobsCreatedMonth.coerceAtLeast(4200),
            industrialGrowthPercent = (playerCurrent.industryIndex - player.industryIndex).toDouble().coerceAtLeast(0.4),
            exportValueMillions = (playerCurrent.tradePower * 2.5).coerceAtLeast(8.0),
            researchProgressNotes = updatedResearch.activeResearchTech?.let { "التقدم في ${it.titleAr}: ${it.progressPercent}%" },
            satisfactionChangePoints = playerCurrent.stability - player.stability,
            topHighlights = listOf(
                "الناتج المحلي: ${EconomyScaleEngine.formatCurrency(playerCurrent.gdpBillions)}",
                "صافي ميزانية الشهر: ${if (netM >= 0) "+" else ""}${String.format("%.1f", netM)}M$",
                "الاستقرار ومؤشر الرضا: ${playerCurrent.stability}%"
            )
        )

        return state.copy(
            gameYear = nextYear,
            gameMonth = nextMonth,
            countries = updatedCountries,
            domesticProjects = updatedDomestic,
            foreignInvestments = updatedForeign,
            outboundInvestments = updatedOutbound,
            foreignOffers = updatedOffers,
            influenceNetwork = updatedInfluence,
            influenceHistory = updatedInfluenceHistory,
            gdpHistory = updatedGdpHistory,
            treasuryHistory = updatedTreasuryHistory,
            inflationHistory = updatedInflationHistory,
            energyGrid = updatedEnergy,
            waterGrid = updatedWater,
            governmentServices = updatedGov,
            laborMarket = updatedLabor,
            researchState = updatedResearch,
            militaryState = updatedMilitary,
            resourceInventory = updatedInventory,
            monthlyReport = monthlyReport,
            activePendingEvent = pendingEvent,
            lastSaveTimeMillis = System.currentTimeMillis()
        )
    }

    private fun createDiversifiedForeignOffer(senderId: String, senderFlag: String, senderNameAr: String): ForeignOffer {
        val id = "offer_${System.currentTimeMillis()}"
        return when (senderId) {
            "JP", "JPN" -> ForeignOffer(
                id = id,
                proposingCountryId = senderId,
                foreignCompanyName = "تكتل تويوتا وسوفت بنك للتقنية $senderFlag",
                projectTitleAr = "إنشاء مجمع تصنيع أشباه الموصلات والبطاريات الذكية",
                descriptionAr = "يسعى الائتلاف الياباني لبناء منشأة متطورة للرقائق الإلكترونية فائقة الدقة مع تدريب 1,000 مهندس محلي سنوياً.",
                category = ProjectCategory.TECH_CITY,
                investmentValueBillions = 4.2,
                jobsOffered = 18500,
                projectDurationMonths = 16,
                investorType = InvestorType.TechnologyFocused,
                initialForeignEquity = 0.65,
                initialStateEquity = 0.35,
                initialTaxRate = 0.12,
                initialLocalJobQuota = 0.45,
                initialTaxHolidayYears = 2,
                negotiatedStateEquity = 0.35,
                negotiatedTaxRate = 0.12,
                negotiatedLocalJobQuota = 0.45,
                negotiatedTaxHolidayYears = 2,
                hasTechTransferClause = true,
                hasLocalRdCenter = true,
                aiAcceptanceLikelihood = 82,
                aiFeedbackMessage = "المستثمر التقني الياباني يرحب بالتعاون ويركز على المهارات وحماية الملكية الفكرية."
            )
            "DE", "DEU" -> ForeignOffer(
                id = id,
                proposingCountryId = senderId,
                foreignCompanyName = "كونسورتيوم سيمنز وبوش الصناعي $senderFlag",
                projectTitleAr = "مجمع تصنيع توربينات الطاقة والمعدات الهندسية الثقيلة",
                descriptionAr = "مجموعة صناعية ألمانية رائدة ترغب بإنشاء قاعدة إقليمية لتجميع المحركات ومعدات البنية التحتية.",
                category = ProjectCategory.INDUSTRIAL_ZONE,
                investmentValueBillions = 3.5,
                jobsOffered = 14000,
                projectDurationMonths = 14,
                investorType = InvestorType.LongTerm,
                initialForeignEquity = 0.60,
                initialStateEquity = 0.40,
                initialTaxRate = 0.15,
                initialLocalJobQuota = 0.50,
                initialTaxHolidayYears = 1,
                negotiatedStateEquity = 0.40,
                negotiatedTaxRate = 0.15,
                negotiatedLocalJobQuota = 0.50,
                negotiatedTaxHolidayYears = 1,
                hasLocalSuppliersCommitment = true,
                aiAcceptanceLikelihood = 78,
                aiFeedbackMessage = "الشركة الألمانية حريصة على الاستقرار والبنية التحتية واعتماد موردين محليين."
            )
            "CN", "CHN" -> ForeignOffer(
                id = id,
                proposingCountryId = senderId,
                foreignCompanyName = "مؤسسة الموانئ والتشييد الصينية (CCCC) $senderFlag",
                projectTitleAr = "بناء منطقة حرة لوجستية ومحطة قطارات شحن بضائع",
                descriptionAr = "تمويل استراتيجي صيني ضخم لتشييد مجمع مستودعات ومركز شحن دولي لتعزيز حركة التجارة العالمية.",
                category = ProjectCategory.LOGISTICS_HUB,
                investmentValueBillions = 5.0,
                jobsOffered = 25000,
                projectDurationMonths = 18,
                investorType = InvestorType.Strategic,
                initialForeignEquity = 0.70,
                initialStateEquity = 0.30,
                initialTaxRate = 0.10,
                initialLocalJobQuota = 0.40,
                initialTaxHolidayYears = 3,
                negotiatedStateEquity = 0.30,
                negotiatedTaxRate = 0.10,
                negotiatedLocalJobQuota = 0.40,
                negotiatedTaxHolidayYears = 3,
                strategicInfluenceBoost = 8,
                aiAcceptanceLikelihood = 85,
                aiFeedbackMessage = "المستثمر الصيني الاستراتيجي يقدم سيولة ضخمة بشرط امتيازات لوجستية وتخفيض الضرائب."
            )
            "US", "USA" -> ForeignOffer(
                id = id,
                proposingCountryId = senderId,
                foreignCompanyName = "صندوق بلاك روك ووول ستريت للنمو $senderFlag",
                projectTitleAr = "صندوق استثمار في مراكز البيانات السحابية والذكاء الاصطناعي",
                descriptionAr = "ضخ استثماري مباشر لتأسيس مراكز حوسبة سحابية فائقة لتغذية الأسواق الرقمية الإقليمية.",
                category = ProjectCategory.TECH_CITY,
                investmentValueBillions = 4.8,
                jobsOffered = 11000,
                projectDurationMonths = 12,
                investorType = InvestorType.AggressiveGrowth,
                initialForeignEquity = 0.75,
                initialStateEquity = 0.25,
                initialTaxRate = 0.08,
                initialLocalJobQuota = 0.35,
                initialTaxHolidayYears = 3,
                negotiatedStateEquity = 0.25,
                negotiatedTaxRate = 0.08,
                negotiatedLocalJobQuota = 0.35,
                negotiatedTaxHolidayYears = 3,
                aiAcceptanceLikelihood = 75,
                aiFeedbackMessage = "صندوق الاستثمار الأمريكي يسعى لعائد مالي مرتفع وإعفاءات ضريبية سريعة."
            )
            else -> ForeignOffer(
                id = id,
                proposingCountryId = senderId,
                foreignCompanyName = "الائتلاف الدولي للطاقة المتجددة $senderFlag",
                projectTitleAr = "مشروع إنتاج الهيدروجين الأخضر ومحطة توليد طاقة شمسية",
                descriptionAr = "استثمار نوعي لتوليد الطاقة النظيفة والتصدير مع دفع رسوم امتياز منتظمة لخزينة الدولة.",
                category = ProjectCategory.POWER_PLANT,
                investmentValueBillions = 3.0,
                jobsOffered = 9500,
                projectDurationMonths = 13,
                investorType = InvestorType.ResourceFocused,
                initialForeignEquity = 0.55,
                initialStateEquity = 0.45,
                initialTaxRate = 0.14,
                initialLocalJobQuota = 0.50,
                initialTaxHolidayYears = 2,
                negotiatedStateEquity = 0.45,
                negotiatedTaxRate = 0.14,
                negotiatedLocalJobQuota = 0.50,
                negotiatedTaxHolidayYears = 2,
                aiAcceptanceLikelihood = 80,
                aiFeedbackMessage = "المستثمر يركز على الموارد النظيفة وإمدادات الطاقة طويلة الأجل."
            )
        }
    }

    /**
     * AI evaluates counter-offer taking into account the distinct InvestorType personality.
     */
    fun evaluateCounterOffer(offer: ForeignOffer, hostCountry: Country): Pair<Int, String> {
        var score = 75

        // Host country macro factors
        score += (hostCountry.stability - 70) / 4
        score += (hostCountry.infrastructureIndex - 70) / 5

        when (offer.investorType) {
            InvestorType.TechnologyFocused -> {
                if (offer.hasTechTransferClause) score += 12
                if (offer.hasLocalRdCenter) score += 15
                if (offer.negotiatedTaxRate > 0.18) score -= 22
                if (offer.negotiatedStateEquity > 0.45) score -= 18
            }
            InvestorType.LowRisk -> {
                if (hostCountry.stability >= 85) score += 18
                if (offer.negotiatedTaxHolidayYears >= 3) score += 14
                if (offer.negotiatedTaxRate > 0.14) score -= 24
                if (offer.negotiatedLocalJobQuota > 0.65) score -= 15
            }
            InvestorType.AggressiveGrowth -> {
                val foreignEquity = offer.negotiatedForeignEquity
                if (foreignEquity < 0.60) score -= 30
                if (offer.negotiatedTaxHolidayYears >= 2) score += 15
                if (offer.negotiatedTaxRate < 0.12) score += 15
            }
            InvestorType.GovernmentBacked -> {
                // Sovereign funds are happy with balanced state equity
                if (offer.negotiatedStateEquity in 0.40..0.51) score += 16
                if (hostCountry.relationsWithPlayer >= 70) score += 15
            }
            InvestorType.Strategic -> {
                if (hostCountry.infrastructureIndex >= 80) score += 15
                if (offer.negotiatedContractYears >= 20) score += 12
                if (offer.hasLocalSuppliersCommitment) score += 8
            }
            InvestorType.ResourceFocused -> {
                if (hostCountry.energyIndex >= 75) score += 14
                if (offer.negotiatedExportRatio >= 0.50) score += 12
                if (offer.negotiatedTaxRate > 0.20) score -= 25
            }
            InvestorType.LongTerm -> {
                if (hostCountry.stability >= 75) score += 12
                if (offer.hasLocalSuppliersCommitment) score += 10
                if (offer.negotiatedContractYears >= 15) score += 10
            }
        }

        score = score.coerceIn(5, 98)

        val message = when {
            score >= 75 -> "المستثمر يعتبر الشروط الحالية ممتازة ومطابقة لأهدافه الاستراتيجية ومستعد للتوقيع الفوري!"
            score in 50..74 -> "المستثمر يرى أن الشروط مقبولة ولكنها عند الحد الأدنى لجدوى المشروع المالية."
            score in 30..49 -> "المستثمر يبدي تحفظات جدية حول الضرائب ونسب المشاركة، ويرى أن الصفقة غير متوازنة."
            else -> "المستثمر يرفض هذه الشروط رفضاً قاطعاً ويعتبرها عائقاً استثمارياً كبيراً."
        }

        return Pair(score, message)
    }

    /**
     * Generates dynamic events linked directly to real economic and state conditions.
     */
    private fun generateStateAwareEvent(country: Country, year: Int, month: Int): GameEvent {
        // High inflation trigger (> 8%)
        if (country.inflationRate > 8.0) {
            return GameEventsData.createInflationCrisisEvent(year, month)
        }
        // High unemployment trigger (> 7.5%)
        if (country.unemploymentRate > 7.5) {
            return GameEventsData.createUnemploymentReliefEvent(year, month)
        }
        // High debt to GDP trigger (> 70%)
        if (country.debtToGdpRatioPercent > 70.0) {
            return GameEventsData.createDebtRestructuringEvent(year, month)
        }
        // High investment attractiveness trigger (> 85)
        if (country.investmentAttractiveness > 85) {
            return GameEventsData.createGlobalHeadquartersBiddingEvent(year, month)
        }
        // Default to classic dynamic event
        return GameEventsData.generateEvent(country.id, year, month)
    }
}
