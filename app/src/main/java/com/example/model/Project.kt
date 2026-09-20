package com.example.model

enum class ProjectCategory(val titleAr: String, val icon: String) {
    COMMERCIAL_PORT("ميناء تجاري ذكي", "🚢"),
    POWER_PLANT("محطة طاقة نظيفة ونووية", "⚡"),
    HIGH_SPEED_RAIL("شبكة قطارات فائقة السرعة", "🚄"),
    PETROCHEMICAL_REFINERY("مجمع بتروكيماويات وتكرير", "🛢️"),
    TECH_CITY("مدينة تقنية وذكاء اصطناعي", "💻"),
    INDUSTRIAL_ZONE("منطقة صناعية وتصديرية", "🏭"),
    AGRITECH_HUB("منطقة زراعية متطورة وأمن غذائي", "🌾"),
    RESEARCH_UNIVERSITY("جامعة بحثية ومختبرات ابتكار", "🎓"),
    MEDICAL_CENTER("مجمع طبي ومستشفى تخصصي", "🏥"),
    LOGISTICS_HUB("مركز لوجستي وتوزيع إقليمي", "📦"),
    
    // Legacy categories for compatibility
    INDUSTRY("صناعة عامة", "🏭"),
    TECH("تقنية عامة", "💻"),
    ENERGY("طاقة عامة", "⚡"),
    LOGISTICS("لوجستيات عامة", "🚢"),
    AGRICULTURE("زراعة عامة", "🌾"),
    INFRASTRUCTURE("بنية تحتية عامة", "🚄"),
    MINING("تعدين وموارد", "⛏️")
}

enum class ProjectStatus(val titleAr: String, val colorHex: Long) {
    PLANNING("قيد التخطيط", 0xFF00E5FF),
    UNDER_CONSTRUCTION("قيد البناء", 0xFFFFD700),
    COMPLETED("مكتمل ويعمل", 0xFF00E676),
    STALLED("متعثر لضعف السيولة", 0xFFFF1744)
}

data class Project(
    val id: String,
    val titleAr: String,
    val descriptionAr: String,
    val category: ProjectCategory,
    val totalCostBillions: Double,
    val durationMonths: Int,
    val remainingMonths: Int,
    val isCompleted: Boolean = false,
    val status: ProjectStatus = if (isCompleted) ProjectStatus.COMPLETED else ProjectStatus.UNDER_CONSTRUCTION,
    val targetCountryId: String, // Host country where project operates
    val investorCountryId: String, // Country that funded/owns the project
    val annualReturnPercent: Double, // e.g. 8.5%
    
    // Concrete Economic and Sector Impacts
    val gdpBoostBillions: Double = 0.0,
    val jobsCreated: Int = 5000,
    val monthlyMaintenanceBillions: Double = 0.02,
    val monthlyRevenueBoostBillions: Double = 0.04,
    val riskFactorPercent: Int = 5,
    val attractivenessBoost: Int = 4,
    
    val industryBoost: Int = 0,
    val infrastructureBoost: Int = 0,
    val energyBoost: Int = 0,
    val agricultureBoost: Int = 0,
    val educationBoost: Int = 0,
    val healthcareBoost: Int = 0,
    val stabilityBoost: Int = 0,
    val tradePowerBoost: Double = 2.0,
    val culturalBoost: Int = 0,
    
    val influenceGain: Int = 5 // Influence acquired by investor in host nation
) {
    val progressPercent: Float
        get() = if (durationMonths <= 0) 1f else ((durationMonths - remainingMonths).toFloat() / durationMonths).coerceIn(0f, 1f)

    val monthlyReturnBillions: Double
        get() = (totalCostBillions * (annualReturnPercent / 100.0)) / 12.0
}
