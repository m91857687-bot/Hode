package com.example.model

data class MonthlyReport(
    val monthNumber: Int,
    val year: Int,
    val gdpGrowthPercent: Double,
    val gdpCauses: List<String>,
    val monthlyRevenueMillions: Double,
    val monthlyExpenseMillions: Double,
    val netSurplusOrDeficitMillions: Double,
    val revenueCauses: List<String>,
    val expenseCauses: List<String>,
    val newJobsCount: Int,
    val industrialGrowthPercent: Double,
    val exportValueMillions: Double,
    val researchProgressNotes: String?,
    val satisfactionChangePoints: Int,
    val topHighlights: List<String>
)

data class PolicyImpactPreview(
    val policyTitleAr: String,
    val revenueImpactDescriptionAr: String,
    val gdpImpactDescriptionAr: String,
    val employmentImpactDescriptionAr: String,
    val satisfactionImpactDescriptionAr: String,
    val netVerdictAr: String
)
