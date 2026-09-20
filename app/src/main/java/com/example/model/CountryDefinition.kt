package com.example.model

enum class Continent(val titleAr: String) {
    ASIA("آسيا"),
    EUROPE("أوروبا"),
    AFRICA("إفريقيا"),
    AMERICAS("الأمريكتان"),
    OCEANIA("أوقيانوسيا"),
    MIDDLE_EAST("الشرق الأوسط")
}

enum class EconomicTier(val titleAr: String, val baseGdpScale: Double) {
    MICRO("اقتصاد نامٍ وصغير", 0.05),
    SMALL("اقتصاد متوسط محدود", 0.15),
    MEDIUM("اقتصاد إقليمي ناشئ", 0.40),
    LARGE("قوة اقتصادية متقدمة", 1.00),
    MAJOR("عملاق صناعي عالمي", 2.50),
    GLOBAL_POWER("قوة اقتصادية عظمى", 5.00)
}

data class CountryDefinition(
    val id: String, // e.g. "SAU"
    val iso2: String, // e.g. "SA"
    val iso3: String, // e.g. "SAU"
    val nameAr: String,
    val nameEn: String,
    val capital: String,
    val continent: Continent,
    val regionAr: String,
    val flag: String,
    val currencyNameAr: String,
    val currencySymbol: String,
    val archetype: CountryArchetype,
    val tier: EconomicTier,
    val primaryResources: List<ResourceType> = emptyList(),
    val mapCenterX: Float, // Normalized 0f to 1f for world map projection
    val mapCenterY: Float,
    val boundingBoxMinX: Float = mapCenterX - 0.03f,
    val boundingBoxMaxX: Float = mapCenterX + 0.03f,
    val boundingBoxMinY: Float = mapCenterY - 0.03f,
    val boundingBoxMaxY: Float = mapCenterY + 0.03f
)
