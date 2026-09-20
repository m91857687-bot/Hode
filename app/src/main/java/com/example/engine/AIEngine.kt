package com.example.engine

import com.example.model.Country
import com.example.model.CountryArchetype
import com.example.model.Factory
import com.example.model.FactoryType
import com.example.model.TradeContract
import kotlin.random.Random

object AIEngine {

    fun processAITick(
        nonPlayerCountries: Map<String, Country>,
        playerCountryId: String,
        activeContracts: List<TradeContract>
    ): Pair<Map<String, Country>, List<TradeContract>> {
        val updatedCountries = nonPlayerCountries.toMutableMap()
        val updatedContracts = activeContracts.toMutableList()

        for ((id, country) in nonPlayerCountries) {
            if (id == playerCountryId) continue

            // 1. Natural GDP & Treasury progression based on Archetype
            val growth = country.gdpGrowthPercent / 100.0
            val newGdp = country.gdpBillions * (1.0 + (growth / 12.0))
            val netRevenue = (newGdp * 0.015) - (country.sovereignDebtBillions * 0.003)
            val newTreasury = (country.treasuryBillions + netRevenue).coerceAtLeast(0.5)

            // 2. Autonomous Factory Upgrade or Construction
            var currentFactories = country.factories
            if (Random.nextInt(100) < 15 && newTreasury > 5.0) {
                // Try upgrading existing factory
                val upgradable = currentFactories.firstOrNull { it.level < 5 }
                if (upgradable != null) {
                    val upgraded = upgradable.copy(level = upgradable.level + 1)
                    currentFactories = currentFactories.map { if (it.id == upgradable.id) upgraded else it }
                } else if (currentFactories.size < 4) {
                    // Try building new factory suitable for archetype
                    val newType = when (country.aiArchetype) {
                        CountryArchetype.RESOURCE_RICH -> FactoryType.OIL_REFINERY
                        CountryArchetype.INDUSTRIAL -> FactoryType.SEMICONDUCTOR_FAB
                        CountryArchetype.SUPERPOWER -> FactoryType.BATTERY_GIGAFACTORY
                        CountryArchetype.EMERGING_GIANT -> FactoryType.STEEL_MILL
                        else -> FactoryType.FOOD_PROCESSING
                    }
                    val newFact = Factory(
                        id = "f_${id}_${System.currentTimeMillis() % 10000}",
                        countryId = id,
                        nameAr = "${newType.nameAr} الوطني",
                        type = newType,
                        level = 1
                    )
                    currentFactories = currentFactories + newFact
                }
            }

            // 3. Autonomous Stability adjustments
            val newStability = (country.stability + Random.nextInt(-1, 2)).coerceIn(20, 98)

            updatedCountries[id] = country.copy(
                gdpBillions = newGdp,
                treasuryBillions = newTreasury,
                factories = currentFactories,
                stability = newStability
            )
        }

        return Pair(updatedCountries, updatedContracts)
    }
}
