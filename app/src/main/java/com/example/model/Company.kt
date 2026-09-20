package com.example.model

data class Company(
    val id: String,
    val nameAr: String,
    val sector: String,
    val originCountryId: String,
    val marketValueBillions: Double,
    val annualProfitMillions: Double,
    val stateOwnershipPercent: Double, // e.g. 0.51 (51%)
    val employeesCount: Int,
    val isStateOwned: Boolean,
    val icon: String = "🏢",
    val capitalBillions: Double = marketValueBillions * 0.4,
    val monthlyRevenueMillions: Double = (annualProfitMillions * 2.5) / 12.0,
    val monthlyProfitMillions: Double = annualProfitMillions / 12.0,
    val privateOwnershipPercent: Double = (1.0 - stateOwnershipPercent).coerceAtLeast(0.0),
    val productionOutputIndex: Int = 70,
    val exportRatio: Double = 0.35,
    val isJointVentures: Boolean = false,
    val foreignPartnerName: String? = null,
    val isInternational: Boolean = false,
    val technologyLevel: Int = 65
) {
    val productionVolumeIndex: Int get() = productionOutputIndex

    val stateAnnualDividendsMillions: Double
        get() = annualProfitMillions * stateOwnershipPercent
        
    val stateMonthlyDividendsMillions: Double
        get() = monthlyProfitMillions * stateOwnershipPercent
}
