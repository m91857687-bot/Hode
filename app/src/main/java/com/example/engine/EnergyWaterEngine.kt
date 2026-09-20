package com.example.engine

import com.example.model.Country
import com.example.model.EnergyGridState
import com.example.model.EnergyPlant
import com.example.model.EnergySourceType
import com.example.model.Factory
import com.example.model.WaterGridState

object EnergyWaterEngine {

    fun initializeEnergyGrid(country: Country): EnergyGridState {
        val tier = EconomyScaleEngine.getCountryScaleTier(country)
        val baseCapacity = when (tier) {
            com.example.model.CountryScaleTier.TINY -> 2.5
            com.example.model.CountryScaleTier.SMALL -> 8.0
            com.example.model.CountryScaleTier.MEDIUM -> 30.0
            com.example.model.CountryScaleTier.LARGE -> 90.0
            com.example.model.CountryScaleTier.MAJOR -> 260.0
            com.example.model.CountryScaleTier.GLOBAL_POWER -> 950.0
        }

        val plants = listOf(
            EnergyPlant("plant_1", EnergySourceType.NATURAL_GAS_TURBINE, capacityGw = baseCapacity * 0.45),
            EnergyPlant("plant_2", EnergySourceType.OIL_THERMAL, capacityGw = baseCapacity * 0.25),
            EnergyPlant("plant_3", EnergySourceType.SOLAR, capacityGw = baseCapacity * 0.20),
            EnergyPlant("plant_4", EnergySourceType.WIND, capacityGw = baseCapacity * 0.10)
        )

        val totalCap = plants.sumOf { it.capacityGw }
        val demand = totalCap * 0.82

        return EnergyGridState(
            plants = plants,
            totalCapacityGw = totalCap,
            currentProductionGw = totalCap * 0.90,
            totalDemandGw = demand,
            gridStabilityPercent = 98
        )
    }

    fun initializeWaterGrid(country: Country): WaterGridState {
        val pop = country.populationMillions
        val popConsumption = pop * 0.25 // 250 liters per capita
        val agriConsumption = (country.agricultureIndex / 100.0) * (pop * 0.65)
        val indConsumption = (country.industryIndex / 100.0) * (pop * 0.35)
        val totalDemand = popConsumption + agriConsumption + indConsumption

        val supply = totalDemand * 1.08 // healthy starting buffer
        val desal = if (country.energyIndex > 70) supply * 0.40 else supply * 0.15
        val natural = supply - desal

        return WaterGridState(
            desalinationCapacityM3Daily = desal,
            riverAndGroundwaterDaily = natural,
            totalWaterSupplyDaily = supply,
            populationConsumptionDaily = popConsumption,
            agriculturalConsumptionDaily = agriConsumption,
            industrialConsumptionDaily = indConsumption,
            totalDemandDaily = totalDemand
        )
    }

    fun calculateMonthlyEnergy(
        current: EnergyGridState,
        country: Country,
        factories: List<Factory>
    ): EnergyGridState {
        val activeFactoriesCount = factories.count { !it.isPaused }
        val factoryDemandGw = activeFactoriesCount * 0.45

        val baseCityDemand = (country.populationMillions * 0.40) * (country.infrastructureIndex / 100.0 + 0.5)
        val totalDemand = baseCityDemand + factoryDemandGw

        val totalCap = current.plants.filter { it.isOperational }.sumOf { it.capacityGw }
        val production = totalCap.coerceAtMost(totalDemand * 1.05) // produce what's needed up to capacity

        val stability = if (production >= totalDemand) 100 else ((production / totalDemand) * 100.0).toInt().coerceIn(20, 100)

        return current.copy(
            totalCapacityGw = totalCap,
            currentProductionGw = production,
            totalDemandGw = totalDemand,
            gridStabilityPercent = stability
        )
    }

    fun calculateMonthlyWater(
        current: WaterGridState,
        country: Country,
        activeFactoriesCount: Int
    ): WaterGridState {
        val pop = country.populationMillions
        val popDemand = pop * 0.25
        val agriDemand = (country.agriculturalProduction / 100.0) * (pop * 0.60)
        val indDemand = activeFactoriesCount * 0.20 + (pop * 0.15)
        val totalDemand = popDemand + agriDemand + indDemand

        val supply = current.desalinationCapacityM3Daily + current.riverAndGroundwaterDaily

        return current.copy(
            totalWaterSupplyDaily = supply,
            populationConsumptionDaily = popDemand,
            agriculturalConsumptionDaily = agriDemand,
            industrialConsumptionDaily = indDemand,
            totalDemandDaily = totalDemand
        )
    }
}
