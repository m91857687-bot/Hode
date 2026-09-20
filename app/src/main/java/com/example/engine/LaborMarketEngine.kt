package com.example.engine

import com.example.model.Country
import com.example.model.LaborMarketState
import com.example.model.LaborSector
import com.example.model.PopulationDemographics
import com.example.model.SectorLaborData

object LaborMarketEngine {

    fun initializeLaborMarket(country: Country): LaborMarketState {
        val pop = country.populationMillions
        val workingAge = pop * 0.64
        val children = pop * 0.22
        val elderly = pop * 0.14
        val laborForce = workingAge * 0.72
        val unemployed = laborForce * (country.unemploymentRate / 100.0)
        val employed = (laborForce - unemployed).coerceAtLeast(0.1)

        val demographics = PopulationDemographics(
            totalPopulationMillions = pop,
            workingAgeMillions = workingAge,
            childrenMillions = children,
            elderlyMillions = elderly,
            laborForceMillions = laborForce,
            unemployedMillions = unemployed,
            literacyRatePercent = (country.educationIndex * 0.95 + 10.0).coerceIn(40.0, 99.0),
            overallSatisfactionPercent = country.stability
        )

        // Distribute workers across sectors
        val totalWorkersInt = (employed * 1_000_000).toInt()
        val sectorShares = mapOf(
            LaborSector.SERVICES to 0.32,
            LaborSector.MANUFACTURING to 0.18,
            LaborSector.AGRICULTURE to 0.12,
            LaborSector.GOVERNMENT to 0.10,
            LaborSector.CONSTRUCTION to 0.08,
            LaborSector.TRANSPORT to 0.05,
            LaborSector.HEALTHCARE to 0.05,
            LaborSector.EDUCATION to 0.04,
            LaborSector.MILITARY to 0.02,
            LaborSector.MINING to 0.015,
            LaborSector.ENERGY to 0.012,
            LaborSector.TECHNOLOGY to 0.008,
            LaborSector.RESEARCH to 0.005
        )

        val sectorsMap = mutableMapOf<LaborSector, SectorLaborData>()
        var totalWageSum = 0.0
        var totalWageCount = 0

        for (sector in LaborSector.values()) {
            val share = sectorShares[sector] ?: 0.01
            val count = (totalWorkersInt * share).toInt().coerceAtLeast(100)
            val wage = EconomyScaleEngine.scaleSectorWage(sector.defaultBaseWage, country)
            sectorsMap[sector] = SectorLaborData(
                sector = sector,
                workersCount = count,
                currentMonthlyWage = wage,
                openVacancies = (count * 0.04).toInt(),
                laborShortagePercent = 0.0
            )
            totalWageSum += wage * count
            totalWageCount += count
        }

        val avgWage = if (totalWageCount > 0) totalWageSum / totalWageCount else 1800.0

        return LaborMarketState(
            demographics = demographics,
            sectors = sectorsMap,
            nationalAverageWage = avgWage,
            minimumWageFloor = avgWage * 0.40,
            monthlyWelfareSpendingMillions = 0.0
        )
    }

    /**
     * Monthly update of the labor market:
     * - Adjusts wages based on unemployment & shortage
     * - Adjusts satisfaction based on real wages, unemployment, and inflation
     */
    fun processMonthlyLaborTick(
        current: LaborMarketState,
        country: Country,
        healthSatisfactionBonus: Int = 0,
        educationSatisfactionBonus: Int = 0,
        powerShortagePenalty: Int = 0,
        waterShortagePenalty: Int = 0
    ): LaborMarketState {
        val unempRate = country.unemploymentRate
        val inflation = country.inflationRate

        // If high unemployment, wages face downward pressure (down to floor)
        // If low unemployment (< 4.5%), wages face upward shortage pressure
        val wagePressure = when {
            unempRate > 12.0 -> -0.012
            unempRate > 8.0 -> -0.005
            unempRate < 4.0 -> +0.015
            unempRate < 3.0 -> +0.025
            else -> 0.002
        }

        val updatedSectors = mutableMapOf<LaborSector, SectorLaborData>()
        var totalWageSum = 0.0
        var totalWageCount = 0

        for ((sector, data) in current.sectors) {
            val inflationMod = (inflation * 0.002).coerceIn(-0.01, 0.02)
            val delta = wagePressure + inflationMod
            val newWage = (data.currentMonthlyWage * (1.0 + delta)).coerceAtLeast(current.minimumWageFloor)

            updatedSectors[sector] = data.copy(
                currentMonthlyWage = newWage
            )
            totalWageSum += newWage * data.workersCount
            totalWageCount += data.workersCount
        }

        val avgWage = if (totalWageCount > 0) totalWageSum / totalWageCount else current.nationalAverageWage

        // Satisfaction calculation (0-100)
        // Affected by: Employment (+), Inflation (-), Health/Edu (+), Shortages (-)
        val employmentFactor = (100.0 - unempRate * 2.5).coerceIn(10.0, 95.0)
        val inflationFactor = (100.0 - inflation * 3.5).coerceIn(20.0, 100.0)
        val rawSatisfaction = (
            employmentFactor * 0.40 +
            inflationFactor * 0.25 +
            (country.stability * 0.20) +
            healthSatisfactionBonus +
            educationSatisfactionBonus -
            powerShortagePenalty -
            waterShortagePenalty
        ).toInt().coerceIn(10, 100)

        val updatedDemographics = current.demographics.copy(
            unemployedMillions = current.demographics.laborForceMillions * (unempRate / 100.0),
            overallSatisfactionPercent = rawSatisfaction
        )

        return current.copy(
            demographics = updatedDemographics,
            sectors = updatedSectors,
            nationalAverageWage = avgWage
        )
    }
}
