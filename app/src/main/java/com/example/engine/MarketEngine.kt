package com.example.engine

import com.example.model.MarketCommodity

object MarketEngine {

    fun updateCommodityPrices(
        currentCommodities: Map<String, MarketCommodity>,
        globalInflationRate: Double = 2.8,
        activeEvents: List<String> = emptyList()
    ): Map<String, MarketCommodity> {
        return MarketPriceEngine.updateCommodityPrices(currentCommodities, globalInflationRate, activeEvents)
    }
}
