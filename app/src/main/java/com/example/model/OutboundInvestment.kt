package com.example.model

enum class OutboundAssetType(val titleAr: String, val icon: String) {
    PORT_TERMINAL("محطة ميناء بحري", "🚢"),
    ENERGY_GRID("محطة ومرفق طاقة", "⚡"),
    INDUSTRIAL_COMPLEX("مجمع صناعي خارجي", "🏭"),
    MINING_CONCESSION("امتياز تعدين وموارد", "⛏️"),
    TECH_VENTURE("حصة في شركة تقنية", "💻"),
    LOGISTICS_CENTER("مركز شحن ولوجستيات", "📦"),
    AGRICULTURAL_ESTATE("مشروع أمن غذائي وزراعة", "🌾")
}

enum class OutboundRiskLevel(val titleAr: String, val colorHex: Long) {
    SAFE("استثمار آمن ومستقر", 0xFF00E676),
    MODERATE("مخاطر سوقية متوسطة", 0xFFFFD700),
    ELEVATED("مخاطر مرتفعة وتوترات", 0xFFFF9100),
    CRITICAL("خطر تأميم أو عقوبات", 0xFFFF1744)
}

enum class OutboundStatus(val titleAr: String) {
    ACTIVE("نشط ويدر عوائد"),
    THREATENED("مهدد باضطرابات"),
    NATIONALIZED("تم التأميم ومصادرة الأصول"),
    COLLAPSED("تعثر وانهيار المشروع")
}

data class OutboundInvestment(
    val id: String,
    val targetCountryId: String,
    val titleAr: String,
    val assetType: OutboundAssetType,
    val investedCapitalBillions: Double,
    val annualReturnPercent: Double = 9.0, // e.g. 9%
    val monthlyDividendsBillions: Double = (investedCapitalBillions * 0.09) / 12.0,
    
    // Influence acquired in host country
    val economicInfluenceGain: Int = 12,
    val tradeInfluenceGain: Int = 8,
    val strategicInfluenceGain: Int = 6,
    val diplomaticBonus: Int = 5,
    
    val riskLevel: OutboundRiskLevel = OutboundRiskLevel.SAFE,
    val riskPercent: Int = 5,
    val status: OutboundStatus = OutboundStatus.ACTIVE,
    val acquiredYear: Int = 2026,
    val acquiredMonth: Int = 1
)
