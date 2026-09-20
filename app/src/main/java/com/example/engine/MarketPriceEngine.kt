package com.example.engine

import com.example.model.MarketCommodity
import com.example.model.ResourceType
import kotlin.math.abs
import kotlin.random.Random

object MarketPriceEngine {

    /**
     * Calculates the smoothed new price of a resource:
     * Price Change = Demand Pressure - Supply Pressure + Inflation Pass-Through + Event Modifier + Mean Reversion
     */
    fun calculateNextPrice(
        resourceType: ResourceType,
        currentPrice: Double,
        globalSupplySurplusPercent: Double = 0.0, // positive = surplus, negative = shortage
        globalDemandSurplusPercent: Double = 0.0, // positive = high demand
        generalInflationRate: Double = 2.5,
        eventModifierPercent: Double = 0.0,
        isStrongEvent: Boolean = false,
        isExtremeEvent: Boolean = false
    ): Double {
        val basePrice = resourceType.defaultBasePrice
        val minAllowed = resourceType.minPrice
        val maxAllowed = resourceType.maxPrice

        // 1. Demand & Supply Pressures
        val demandFactor = globalDemandSurplusPercent * PriceBalanceConfig.DEMAND_ELASTICITY
        val supplyFactor = globalSupplySurplusPercent * PriceBalanceConfig.SUPPLY_ELASTICITY
        val marketPressure = demandFactor - supplyFactor

        // 2. Inflation & Mean Reversion (pull back toward base equilibrium)
        val inflationEffect = ((generalInflationRate - 2.0) / 100.0) * PriceBalanceConfig.INFLATION_PASS_THROUGH
        val meanReversionEffect = ((basePrice - currentPrice) / basePrice) * PriceBalanceConfig.MEAN_REVERSION_SPEED

        // 3. Volatility noise
        val randomNoise = (Random.nextDouble() - 0.5) * resourceType.volatility * 0.5

        // Raw percentage change
        val rawDeltaPercent = marketPressure + inflationEffect + meanReversionEffect + randomNoise + eventModifierPercent

        // 4. Smoothing Clamp based on event severity
        val maxMonthlyAllowed = when {
            isExtremeEvent -> PriceBalanceConfig.EXTREME_EVENT_CHANGE_PERCENT
            isStrongEvent -> PriceBalanceConfig.STRONG_EVENT_CHANGE_PERCENT
            else -> PriceBalanceConfig.MAX_NORMAL_MONTHLY_CHANGE_PERCENT
        }

        val clampedDeltaPercent = rawDeltaPercent.coerceIn(-maxMonthlyAllowed, maxMonthlyAllowed)
        val newPrice = currentPrice * (1.0 + clampedDeltaPercent)

        return newPrice.coerceIn(minAllowed, maxAllowed)
    }

    fun updateCommodityPrices(
        currentCommodities: Map<String, MarketCommodity>,
        globalInflationRate: Double = 2.8,
        activeEvents: List<String> = emptyList()
    ): Map<String, MarketCommodity> {
        val updated = mutableMapOf<String, MarketCommodity>()

        for (type in ResourceType.values()) {
            val existing = currentCommodities[type.name]
            val base = type.defaultBasePrice
            val current = existing?.currentPrice ?: base

            // Check if any active event affects this commodity
            var eventMod = 0.0
            var isStrong = false
            var isExtreme = false

            if (type == ResourceType.OIL && activeEvents.any { it.contains("OIL_SHOCK") }) {
                eventMod = 0.20
                isStrong = true
            } else if (type == ResourceType.WHEAT && activeEvents.any { it.contains("FOOD_CRISIS") }) {
                eventMod = 0.18
                isStrong = true
            } else if (type == ResourceType.NATURAL_GAS && activeEvents.any { it.contains("ENERGY_CRISIS") }) {
                eventMod = 0.22
                isStrong = true
            }

            val newPrice = calculateNextPrice(
                resourceType = type,
                currentPrice = current,
                generalInflationRate = globalInflationRate,
                eventModifierPercent = eventMod,
                isStrongEvent = isStrong,
                isExtremeEvent = isExtreme
            )

            val changePercent = if (current > 0.0) ((newPrice - current) / current) * 100.0 else 0.0
            val oldHistory = existing?.priceHistory ?: listOf(base)
            val newHistory = (oldHistory + newPrice).takeLast(12)

            updated[type.name] = MarketCommodity(
                resourceType = type,
                currentPrice = newPrice,
                basePrice = base,
                priceHistory = newHistory,
                monthlyChangePercent = changePercent
            )
        }

        return updated
    }
}
