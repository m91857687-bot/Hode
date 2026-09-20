package com.example.engine

import com.example.model.Country
import com.example.model.CountryScaleTier
import kotlin.math.pow
import kotlin.math.sqrt

object EconomyScaleEngine {

    fun getCountryScaleTier(country: Country): CountryScaleTier {
        return CountryScaleTier.fromGdpAndPop(country.gdpBillions, country.populationMillions)
    }

    /**
     * Calculates the country price index (purchasing power / local cost baseline).
     * Normalizes around 1.0 for a typical medium country.
     */
    fun calculatePriceIndex(country: Country): Double {
        val tier = getCountryScaleTier(country)
        val gdpPerCapitaThousands = if (country.populationMillions > 0) {
            country.gdpBillions / country.populationMillions
        } else 10.0

        val inflationModifier = 1.0 + ((country.inflationRate - 2.5) * 0.02).coerceIn(-0.15, 0.40)
        val wealthFactor = (gdpPerCapitaThousands / 25.0).coerceIn(0.4, 2.5)

        return (tier.costMultiplier * 0.4 + wealthFactor * 0.6) * inflationModifier
    }

    /**
     * Dynamic Construction Cost formula:
     * Construction Cost = Base Cost × Country Scale Factor × Material Index × Inflation Factor
     */
    fun scaleConstructionCost(baseCostMillions: Double, country: Country): Double {
        val tier = getCountryScaleTier(country)
        val scaleFactor = when (tier) {
            CountryScaleTier.TINY -> 0.20
            CountryScaleTier.SMALL -> 0.45
            CountryScaleTier.MEDIUM -> 1.00
            CountryScaleTier.LARGE -> 2.20
            CountryScaleTier.MAJOR -> 4.50
            CountryScaleTier.GLOBAL_POWER -> 8.50
        }
        val inflationMod = 1.0 + ((country.inflationRate - 2.0) * 0.015).coerceIn(-0.1, 0.5)
        return (baseCostMillions * scaleFactor * inflationMod).coerceAtLeast(0.2)
    }

    /**
     * Dynamic Factory Cost: Small: 0.5M-5M, Medium: 5M-25M, Large: 25M-100M, Mega: 100M+
     * Scaled gracefully per tier.
     */
    fun scaleFactoryCost(baseCostMillions: Double, country: Country): Double {
        val tier = getCountryScaleTier(country)
        val factor = when (tier) {
            CountryScaleTier.TINY -> 0.25
            CountryScaleTier.SMALL -> 0.55
            CountryScaleTier.MEDIUM -> 1.00
            CountryScaleTier.LARGE -> 2.00
            CountryScaleTier.MAJOR -> 3.80
            CountryScaleTier.GLOBAL_POWER -> 7.00
        }
        return (baseCostMillions * factor).coerceAtLeast(0.5)
    }

    /**
     * Dynamic Research Cost: Basic: 0.1M-1M, Advanced: 1M-10M, Major: 10M-100M.
     */
    fun scaleResearchCost(baseCostMillions: Double, country: Country): Double {
        val tier = getCountryScaleTier(country)
        val factor = when (tier) {
            CountryScaleTier.TINY -> 0.15
            CountryScaleTier.SMALL -> 0.40
            CountryScaleTier.MEDIUM -> 1.00
            CountryScaleTier.LARGE -> 2.50
            CountryScaleTier.MAJOR -> 5.00
            CountryScaleTier.GLOBAL_POWER -> 10.00
        }
        return (baseCostMillions * factor).coerceAtLeast(0.1)
    }

    /**
     * Dynamic Military Unit & Equipment Cost.
     */
    fun scaleMilitaryCost(baseCostMillions: Double, country: Country): Double {
        val tier = getCountryScaleTier(country)
        val factor = when (tier) {
            CountryScaleTier.TINY -> 0.20
            CountryScaleTier.SMALL -> 0.50
            CountryScaleTier.MEDIUM -> 1.00
            CountryScaleTier.LARGE -> 2.20
            CountryScaleTier.MAJOR -> 4.50
            CountryScaleTier.GLOBAL_POWER -> 8.00
        }
        return (baseCostMillions * factor).coerceAtLeast(0.1)
    }

    /**
     * Scales sector salary based on GDP per capita, labor demand/supply, and inflation.
     */
    fun scaleSectorWage(baseSalary: Double, country: Country, shortageFactor: Double = 1.0): Double {
        val gdpPerCapitaThousands = if (country.populationMillions > 0) {
            (country.gdpBillions * 1000.0) / country.populationMillions
        } else 20000.0

        val wealthMultiplier = sqrt(gdpPerCapitaThousands / 20000.0).coerceIn(0.4, 2.8)
        val inflationMultiplier = 1.0 + ((country.inflationRate - 2.0) * 0.01).coerceIn(-0.1, 0.6)
        val shortageMultiplier = shortageFactor.coerceIn(0.75, 1.6)

        val rawWage = baseSalary * wealthMultiplier * inflationMultiplier * shortageMultiplier

        // Minimum wage floor
        val minimumWageFloor = baseSalary * 0.45
        return rawWage.coerceAtLeast(minimumWageFloor)
    }

    /**
     * Formats financial amounts clearly for UI:
     * e.g. 15.2M, 2.4B, 120K. Never scientific notation or giant unreadable zeros.
     */
    fun formatCurrency(amountMillionsOrBillions: Double, isBillions: Boolean = true): String {
        val totalMillions = if (isBillions) amountMillionsOrBillions * 1000.0 else amountMillionsOrBillions
        return when {
            totalMillions >= 1_000_000.0 -> String.format("%.2f T$", totalMillions / 1_000_000.0)
            totalMillions >= 1_000.0 -> String.format("%.2f B$", totalMillions / 1_000.0)
            totalMillions >= 1.0 -> String.format("%.2f M$", totalMillions)
            totalMillions >= 0.001 -> String.format("%.1f K$", totalMillions * 1000.0)
            else -> String.format("%.2f M$", totalMillions)
        }
    }
}
