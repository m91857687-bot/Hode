package com.example.model

enum class PublicServiceType(
    val titleAr: String,
    val icon: String,
    val baseEmployeesPerMillionPop: Int,
    val defaultBudgetSharePercent: Double
) {
    EDUCATION("التعليم والجامعات والتدريب المهني", "🎓", 7500, 0.22),
    HEALTHCARE("المستشفيات والرعاية الصحية", "🏥", 6200, 0.25),
    POLICE_SECURITY("الأمن العام والشرطة والدفاع المدني", "🚓", 3800, 0.15),
    INFRASTRUCTURE("صيانة الطرق والجسور والشبكات", "🏗️", 2500, 0.14),
    PUBLIC_TRANSPORT("النقل العام والسكك الحديدية", "🚆", 1800, 0.08),
    ADMINISTRATION("الجهاز الإداري والخدمات الحكومية", "🏛️", 4000, 0.10),
    WASTE_MANAGEMENT("إدارة النفايات والصحة البيئية", "♻️", 1200, 0.06)
}

data class PublicServiceStatus(
    val type: PublicServiceType,
    val fundingLevelPercent: Int = 100, // 50% to 150%
    val employeesCount: Int,
    val monthlyOperatingCostMillions: Double,
    val monthlyWagesCostMillions: Double,
    val efficiencyPercent: Int = 85
) {
    val totalMonthlyExpenseMillions: Double
        get() = monthlyOperatingCostMillions + monthlyWagesCostMillions
}

data class SocialWelfarePrograms(
    val pensionsSpendingMonthlyMillions: Double = 5.0,
    val unemploymentBenefitMonthlyMillions: Double = 2.0,
    val foodSubsidyMonthlyMillions: Double = 3.0,
    val energySubsidyMonthlyMillions: Double = 4.0
) {
    val totalMonthlyCostMillions: Double
        get() = pensionsSpendingMonthlyMillions + unemploymentBenefitMonthlyMillions + foodSubsidyMonthlyMillions + energySubsidyMonthlyMillions
}

data class GovernmentServicesState(
    val services: Map<PublicServiceType, PublicServiceStatus>,
    val welfare: SocialWelfarePrograms,
    val totalCivilServantsCount: Int,
    val publicSatisfactionBonus: Int = 0
) {
    val totalMonthlyServicesCostMillions: Double
        get() = services.values.sumOf { it.totalMonthlyExpenseMillions } + welfare.totalMonthlyCostMillions
}
