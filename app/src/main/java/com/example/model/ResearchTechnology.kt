package com.example.model

enum class ResearchField(val titleAr: String, val icon: String) {
    AGRICULTURE("الزراعة والغذاء والأمن الحيوي", "🌾"),
    ENERGY("الطاقة المتجددة والاندماج النووي", "⚡"),
    INDUSTRY("التصنيع المتقدم والأتمتة", "🏭"),
    MEDICINE("الطب الحيوي والصيدلة المتقدمة", "🏥"),
    ELECTRONICS("أشباه الموصلات والرقائق الدقيقة", "🔬"),
    AI_ROBOTICS("الذكاء الاصطناعي والروبوتات الصناعية", "🤖"),
    MATERIALS("المواد النانوية والسبائك الخارقة", "💎"),
    SPACE("تقنيات الفضاء والأقمار الصناعية", "🚀"),
    TRANSPORTATION("اللوجستيات الفائقة والسكك المغناطيسية", "🚆")
}

data class TechNode(
    val id: String,
    val titleAr: String,
    val descriptionAr: String,
    val field: ResearchField,
    val baseCostMillions: Double,
    val requiredMonths: Int,
    val requiredScientists: Int,
    val currentProgressMonths: Int = 0,
    val isCompleted: Boolean = false,
    val isUnderActiveResearch: Boolean = false,
    val prerequisiteTechId: String? = null,
    val productivityBonusPercent: Double = 0.0,
    val energyEfficiencyBonusPercent: Double = 0.0,
    val factoryOutputBonusPercent: Double = 0.0,
    val tradeLogisticsBonusPercent: Double = 0.0
) {
    val progressPercent: Int
        get() = if (requiredMonths > 0) ((currentProgressMonths.toDouble() / requiredMonths) * 100).toInt().coerceIn(0, 100) else 100
}

data class ResearchState(
    val scientistsCount: Int = 1200,
    val monthlyResearchBudgetMillions: Double = 4.5,
    val laboratoriesCount: Int = 8,
    val universitiesCount: Int = 4,
    val techTree: List<TechNode> = emptyList()
) {
    val activeResearchTech: TechNode?
        get() = techTree.find { it.isUnderActiveResearch && !it.isCompleted }

    val completedTechsCount: Int
        get() = techTree.count { it.isCompleted }
}
