package com.example.model

enum class LaborSector(
    val code: String,
    val titleAr: String,
    val icon: String,
    val defaultBaseWage: Double, // Game salary units
    val defaultSkillLevelAr: String
) {
    AGRICULTURE("AGRI", "الزراعة والصيد والغذاء", "🌾", 1200.0, "أساسي"),
    MINING("MINE", "التعدين والموارد الطبيعية", "⛏️", 2200.0, "متوسط"),
    MANUFACTURING("MFG", "الصناعة التحويلية والمصانع", "🏭", 1800.0, "متوسط"),
    CONSTRUCTION("CNST", "البناء والتشييد والعقارات", "🏗️", 1700.0, "متوسط"),
    SERVICES("SERV", "التجارة والخدمات واللوجستيات", "🛒", 1400.0, "أساسي"),
    TRANSPORT("TRNS", "النقل والموانئ والسكك الحديدية", "🚆", 1600.0, "متوسط"),
    HEALTHCARE("HLTH", "الرعاية الصحية والمستشفيات", "🏥", 2500.0, "متقدم"),
    EDUCATION("EDUC", "التعليم والتدريب المهني", "🎓", 2200.0, "متقدم"),
    GOVERNMENT("GOVT", "الجهاز الإداري والأمن المدني", "🏛️", 2100.0, "متوسط"),
    RESEARCH("RSRCH", "الأبحاث والمختبرات العلمية", "🔬", 3200.0, "خبير"),
    TECHNOLOGY("TECH", "التقنية والبرمجيات وأشباه الموصلات", "💻", 4000.0, "خبير"),
    MILITARY("MIL", "القوات المسلحة والصناعات العسكرية", "🪖", 2300.0, "متوسط"),
    ENERGY("NRG", "محطات الطاقة وشبكات الكهرباء", "⚡", 2600.0, "متقدم")
}

data class SectorLaborData(
    val sector: LaborSector,
    val workersCount: Int,
    val currentMonthlyWage: Double,
    val openVacancies: Int = 0,
    val laborShortagePercent: Double = 0.0
)

data class PopulationDemographics(
    val totalPopulationMillions: Double,
    val workingAgeMillions: Double,
    val childrenMillions: Double,
    val elderlyMillions: Double,
    val laborForceMillions: Double,
    val unemployedMillions: Double,
    val literacyRatePercent: Double = 82.0,
    val overallSatisfactionPercent: Int = 75 // 0-100 Population Satisfaction
) {
    val unemploymentRatePercent: Double
        get() = if (laborForceMillions > 0.0) (unemployedMillions / laborForceMillions * 100.0).coerceIn(1.0, 45.0) else 5.0
}

data class LaborMarketState(
    val demographics: PopulationDemographics,
    val sectors: Map<LaborSector, SectorLaborData>,
    val nationalAverageWage: Double,
    val minimumWageFloor: Double = 800.0,
    val monthlyWelfareSpendingMillions: Double = 0.0
)
