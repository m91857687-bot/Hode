package com.example.engine

import com.example.model.Country
import com.example.model.Factory
import com.example.model.FactoryType
import com.example.model.ResourceType
import com.example.model.TradeContract

object IndustryEngine {

    fun processMonthlyFactories(
        country: Country,
        tradeContracts: List<TradeContract>,
        powerShortagePenaltyPercent: Int = 0,
        unlockedFactoryBonusPercent: Double = 0.0
    ): List<Factory> {
        val updatedFactories = mutableListOf<Factory>()

        // Collect available resources from domestic production and active import contracts
        val availableResources = mutableSetOf<ResourceType>()
        for ((_, res) in country.resources) {
            if (res.currentProductionUnits > 0) {
                availableResources.add(res.resourceType)
            }
        }
        for (contract in tradeContracts) {
            if (contract.buyerCountryId == country.id && !contract.isPaused && !contract.isExpired) {
                availableResources.add(contract.resourceType)
            }
        }

        for (factory in country.factories) {
            if (factory.isPaused) {
                updatedFactories.add(factory)
                continue
            }

            // Check if required primary input is available
            val reqInput = factory.type.requiredInput
            val hasPrimaryInput = reqInput == null || availableResources.contains(reqInput)

            // Check secondary input if any
            val secInput = factory.type.secondaryInput
            val hasSecondaryInput = secInput == null || availableResources.contains(secInput)

            val baseEfficiency = if (!hasPrimaryInput) {
                // Severe shortage penalty: factory almost stops (drops to 15%)
                15
            } else if (!hasSecondaryInput) {
                // Partial secondary shortage: drops to 50%
                50
            } else {
                // Full efficiency (85% to 98% based on level)
                (85 + (factory.level - 1) * 3).coerceAtMost(98)
            }

            // Apply power shortage penalty and technology bonus
            val efficiencyAfterPower = (baseEfficiency - powerShortagePenaltyPercent).coerceAtLeast(10)
            val finalEfficiency = (efficiencyAfterPower * (1.0 + unlockedFactoryBonusPercent)).toInt().coerceIn(10, 100)

            updatedFactories.add(factory.copy(efficiencyPercent = finalEfficiency))
        }

        return updatedFactories
    }

    fun buildFactory(
        country: Country,
        type: FactoryType,
        customNameAr: String? = null
    ): Pair<Country, Factory>? {
        val scaledCostMillions = EconomyScaleEngine.scaleFactoryCost(type.baseCostMillions, country)
        val costBillions = scaledCostMillions / 1000.0
        if (country.treasuryBillions < costBillions) {
            return null // Not enough treasury funds
        }

        val newFactory = Factory(
            id = "f_${country.id}_${System.currentTimeMillis() % 100000}",
            countryId = country.id,
            nameAr = customNameAr ?: "${type.nameAr} - مجمع ${country.factories.size + 1}",
            type = type,
            level = 1,
            efficiencyPercent = 85,
            isStateOwned = true,
            stateOwnershipRatio = 1.0
        )

        val updatedCountry = country.copy(
            treasuryBillions = country.treasuryBillions - costBillions,
            factories = country.factories + newFactory
        )

        return Pair(updatedCountry, newFactory)
    }

    fun upgradeFactory(
        country: Country,
        factoryId: String
    ): Country? {
        val factory = country.factories.find { it.id == factoryId } ?: return null
        if (factory.level >= 5) return null // Max level is 5

        val scaledUpgradeCostMillions = EconomyScaleEngine.scaleFactoryCost(factory.upgradeCostMillions, country)
        val costBillions = scaledUpgradeCostMillions / 1000.0
        if (country.treasuryBillions < costBillions) return null

        val upgraded = factory.copy(
            level = factory.level + 1,
            efficiencyPercent = (factory.efficiencyPercent + 3).coerceAtMost(99)
        )

        val newFactories = country.factories.map { if (it.id == factoryId) upgraded else it }
        return country.copy(
            treasuryBillions = country.treasuryBillions - costBillions,
            factories = newFactories
        )
    }

    fun toggleFactoryPause(
        country: Country,
        factoryId: String
    ): Country {
        val newFactories = country.factories.map {
            if (it.id == factoryId) it.copy(isPaused = !it.isPaused) else it
        }
        return country.copy(factories = newFactories)
    }

    fun privatizeFactory(
        country: Country,
        factoryId: String,
        stakeToSell: Double = 0.49
    ): Country? {
        val factory = country.factories.find { it.id == factoryId } ?: return null
        if (!factory.isStateOwned || factory.stateOwnershipRatio < stakeToSell) return null

        val proceedsBillions = (factory.type.baseCostMillions * factory.levelMultiplier * stakeToSell * 1.3) / 1000.0
        val remainingRatio = factory.stateOwnershipRatio - stakeToSell

        val updated = factory.copy(
            stateOwnershipRatio = remainingRatio,
            isStateOwned = remainingRatio >= 0.50
        )

        val newFactories = country.factories.map { if (it.id == factoryId) updated else it }
        return country.copy(
            treasuryBillions = country.treasuryBillions + proceedsBillions,
            factories = newFactories
        )
    }
}
