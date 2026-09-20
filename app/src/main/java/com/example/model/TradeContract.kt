package com.example.model

enum class ContractDuration(val months: Int, val labelAr: String) {
    ONE_MONTH(1, "شهر واحد"),
    SIX_MONTHS(6, "6 أشهر"),
    ONE_YEAR(12, "12 شهرًا (سنة)"),
    TWO_YEARS(24, "24 شهرًا (سنتان)"),
    FIVE_YEARS(60, "60 شهرًا (5 سنوات)")
}

data class TradeContract(
    val id: String,
    val buyerCountryId: String,
    val sellerCountryId: String,
    val resourceType: ResourceType,
    val monthlyQuantity: Double, // in thousands of units
    val agreedUnitPrice: Double, // price per unit
    val durationMonths: Int = 12,
    val remainingMonths: Int = 12,
    val tariffPercent: Double = 0.05,
    val transportCostPerUnit: Double = 5.0,
    val isPaused: Boolean = false,
    val autoRenew: Boolean = true
) {
    val totalMonthlyVolumeCostMillions: Double
        get() = (monthlyQuantity * agreedUnitPrice) / 1000.0

    val monthlyTariffRevenueMillions: Double
        get() = totalMonthlyVolumeCostMillions * tariffPercent

    val monthlyTransportCostMillions: Double
        get() = (monthlyQuantity * transportCostPerUnit) / 1000.0

    val totalMonthlyBuyerCostMillions: Double
        get() = totalMonthlyVolumeCostMillions + monthlyTariffRevenueMillions + monthlyTransportCostMillions

    val isExpired: Boolean
        get() = remainingMonths <= 0
}
