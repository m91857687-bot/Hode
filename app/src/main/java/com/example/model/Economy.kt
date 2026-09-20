package com.example.model

data class MonthlyBudget(
    // Revenues (Billions USD / month)
    val taxRevenues: Double,
    val stateCompanyProfits: Double,
    val customsAndTariffs: Double,
    val resourceExports: Double,
    val foreignInvestmentsDividends: Double,
    val tourismAndServices: Double,

    // Expenses (Billions USD / month)
    val educationBudget: Double,
    val healthcareBudget: Double,
    val infrastructureMaintenance: Double,
    val militaryAndSecurity: Double,
    val socialSubsidies: Double,
    val researchAndDev: Double,
    val debtInterestService: Double
) {
    val totalRevenue: Double
        get() = taxRevenues + stateCompanyProfits + customsAndTariffs + resourceExports + foreignInvestmentsDividends + tourismAndServices

    val totalExpense: Double
        get() = educationBudget + healthcareBudget + infrastructureMaintenance + militaryAndSecurity + socialSubsidies + researchAndDev + debtInterestService

    val netCashflow: Double
        get() = totalRevenue - totalExpense
}
