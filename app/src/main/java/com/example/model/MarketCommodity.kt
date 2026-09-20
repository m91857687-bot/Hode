package com.example.model

data class MarketCommodity(
    val resourceType: ResourceType,
    val currentPrice: Double,
    val basePrice: Double,
    val globalSupplyIndex: Double = 100.0,
    val globalDemandIndex: Double = 100.0,
    val priceHistory: List<Double> = emptyList(),
    val monthlyChangePercent: Double = 0.0
) {
    val priceRatio: Double
        get() = currentPrice / basePrice

    val statusColorHex: Long
        get() = when {
            monthlyChangePercent > 2.0 -> 0xFF00E676 // Green (Rising)
            monthlyChangePercent < -2.0 -> 0xFFFF1744 // Red (Falling)
            else -> 0xFFFFD700 // Gold (Stable)
        }
}
