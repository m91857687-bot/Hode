package com.example.engine

object PriceBalanceConfig {
    const val MAX_NORMAL_MONTHLY_CHANGE_PERCENT = 0.08  // ±8%
    const val STRONG_EVENT_CHANGE_PERCENT = 0.15        // ±15%
    const val EXTREME_EVENT_CHANGE_PERCENT = 0.30       // ±30%

    const val MEAN_REVERSION_SPEED = 0.06               // 6% pullback per month toward base price
    const val INFLATION_PASS_THROUGH = 0.25             // Fraction of general inflation directly added to commodity prices

    // Global supply/demand elasticities
    const val SUPPLY_ELASTICITY = 0.35
    const val DEMAND_ELASTICITY = 0.45
}
