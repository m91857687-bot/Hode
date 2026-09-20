package com.example.model

enum class MilitaryBranch(val titleAr: String, val icon: String) {
    INFANTRY("القوات البرية والمشاة الآلية", "🪖"),
    ARMOR("سلاح المدرعات والدبابات", "🛡️"),
    ARTILLERY("المدفعية وراجمات الصواريخ التكتيكية", "💥"),
    AIR_DEFENSE("الدفاع الجوي ومنظومات الرادار", "📡"),
    AIR_FORCE("القوات الجوية والطيران الحربي", "✈️"),
    NAVY("القوات البحرية وأساطيل الحماية", "⚓"),
    SPECIAL_FORCES("قوات العمليات الخاصة والاستطلاع", "🎯"),
    STRATEGIC_DETERRENCE("قوة الردع الاستراتيجي الصاروخي", "🚀")
}

data class MilitaryUnitType(
    val branch: MilitaryBranch,
    val nameAr: String,
    val activeCount: Int,
    val baseMonthlyMaintenanceCostPerUnit: Double, // in Thousands USD
    val combatPowerRating: Int
)

data class MilitaryState(
    val activeSoldiersCount: Int,
    val units: Map<MilitaryBranch, MilitaryUnitType>,
    val readinessPercent: Int = 85, // 0 - 100
    val trainingLevelPercent: Int = 80, // 0 - 100
    val monthlyPersonnelBudgetMillions: Double,
    val monthlyMaintenanceBudgetMillions: Double,
    val monthlyTrainingBudgetMillions: Double,
    val strategicDeterrenceScore: Int = 40,
    val militaryEquipmentStockpile: Int = 500
) {
    val totalMonthlyBudgetMillions: Double
        get() = monthlyPersonnelBudgetMillions + monthlyMaintenanceBudgetMillions + monthlyTrainingBudgetMillions

    val overallMilitaryScore: Int
        get() = ((units.values.sumOf { it.combatPowerRating } * (readinessPercent / 100.0) * (trainingLevelPercent / 100.0)) / 10).toInt() + strategicDeterrenceScore
}
