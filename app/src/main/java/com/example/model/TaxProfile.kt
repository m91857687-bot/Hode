package com.example.model

data class TaxProfile(
    val incomeTaxRate: Double = 0.18,      // 5% - 50%
    val corporateTaxRate: Double = 0.20,   // 5% - 40%
    val vatRate: Double = 0.12,            // 0% - 25%
    val importTariffRate: Double = 0.08,   // 0% - 35%
    val exportTariffRate: Double = 0.04,   // 0% - 25%
    val resourceRoyaltyRate: Double = 0.15,// 5% - 50%
    val propertyTaxRate: Double = 0.02     // 0% - 10%
) {
    val effectiveTaxRate: Double
        get() = (incomeTaxRate * 0.35) + (corporateTaxRate * 0.30) + (vatRate * 0.20) + (importTariffRate * 0.10) + (propertyTaxRate * 0.05)

    // Economic feedback impacts:
    // Higher taxes decrease private investment attractiveness
    val investmentPenaltyScore: Double
        get() = ((corporateTaxRate - 0.15) * 80.0 + (importTariffRate - 0.05) * 40.0).coerceAtLeast(0.0)

    // Higher VAT and income tax reduce consumer consumption & public satisfaction
    val publicDiscontentScore: Double
        get() = ((incomeTaxRate - 0.15) * 70.0 + (vatRate - 0.10) * 80.0).coerceIn(0.0, 45.0)

    // Moderate tariffs protect local industry up to a threshold
    val industrialProtectionBonus: Double
        get() = if (importTariffRate in 0.05..0.20) importTariffRate * 40.0 else (0.20 - (importTariffRate - 0.20)) * 20.0
}
