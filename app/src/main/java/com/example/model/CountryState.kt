package com.example.model

data class CountryState(
    val countryId: String,
    val treasuryBillions: Double,
    val gdpBillions: Double,
    val gdpGrowthPercent: Double,
    val populationMillions: Double,
    val inflationRate: Double,
    val unemploymentRate: Double,
    val sovereignDebtBillions: Double,
    val monthlyRevenue: Double = 0.0,
    val monthlyExpenses: Double = 0.0,
    val taxProfile: TaxProfile = TaxProfile(),
    val stability: Int = 75,
    val centralBankInterestRate: Double = 0.045,

    // Sector Indexes (0 - 100)
    val industryIndex: Int = 60,
    val agricultureIndex: Int = 50,
    val energyIndex: Int = 60,
    val infrastructureIndex: Int = 65,
    val educationIndex: Int = 70,
    val healthIndex: Int = 70,
    val militaryIndex: Int = 60,

    // Dynamic Collections
    val resources: Map<String, CountryResource> = emptyMap(), // keyed by ResourceType.name
    val factories: List<Factory> = emptyList(),
    val tradeSurplusBillions: Double = 5.0,
    val relationsWithPlayer: Int = 50, // 0 - 100
    val diplomaticInfluence: Int = 50,
    val economicInfluence: Int = 50,
    val strategicInfluence: Int = 50,
    val culturalInfluence: Int = 45,
    val tradePower: Double = 50.0
) {
    val debtToGdpRatioPercent: Double
        get() = if (gdpBillions > 0) (sovereignDebtBillions / gdpBillions) * 100.0 else 0.0

    val foreignReservesBillions: Double
        get() = (treasuryBillions * 0.85) + (gdpBillions * 0.04)

    val activeFactoriesCount: Int
        get() = factories.count { !it.isPaused }

    val totalFactoryWorkers: Int
        get() = factories.filter { !it.isPaused }.sumOf { it.actualWorkers }

    val totalFactoryDividendsMillions: Double
        get() = factories.sumOf { it.monthlyStateDividendsMillions }
}
