package com.example.model

data class MonthlyBudget(
    // Revenues (Billions USD / month)
    val taxRevenues: Double,
    val stateCompanyProfits: Double,
    val customsAndTariffs: Double,
    val resourceExports: Double,
    val foreignInvestmentsDividends: Double,
    val tourismAndServices: Double,
    val corporateTaxRevenue: Double = 0.0,
    val vatRevenue: Double = 0.0,
    val factoryProfits: Double = 0.0,

    // Expenses (Billions USD / month)
    val educationBudget: Double,
    val healthcareBudget: Double,
    val infrastructureMaintenance: Double,
    val militaryAndSecurity: Double,
    val socialSubsidies: Double,
    val researchAndDev: Double,
    val debtInterestService: Double,
    val policeAndInterior: Double = 0.0,
    val publicAdministration: Double = 0.0,
    val energyAndUtilities: Double = 0.0
) {
    val totalRevenue: Double
        get() = taxRevenues + corporateTaxRevenue + vatRevenue + stateCompanyProfits + factoryProfits + customsAndTariffs + resourceExports + foreignInvestmentsDividends + tourismAndServices

    val totalExpense: Double
        get() = educationBudget + healthcareBudget + infrastructureMaintenance + militaryAndSecurity + policeAndInterior + publicAdministration + energyAndUtilities + socialSubsidies + researchAndDev + debtInterestService

    val netCashflow: Double
        get() = totalRevenue - totalExpense
}
