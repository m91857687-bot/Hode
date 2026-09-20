package com.example.model

enum class ResourceCategory(val titleAr: String, val icon: String) {
    ENERGY("طاقة ووقود", "⚡"),
    METALS("معادن وصناعة", "⛏️"),
    AGRICULTURE("زراعة وغذاء", "🌾"),
    HIGH_TECH("عناصر متقدمة وتقنية", "🔬"),
    UTILITIES("خدمات ومرافق أساسية", "🚰")
}

enum class ResourceType(
    val code: String,
    val nameAr: String,
    val nameEn: String,
    val category: ResourceCategory,
    val icon: String,
    val defaultBasePrice: Double, // Game economy base units
    val unitLabelAr: String,
    val minPrice: Double,
    val maxPrice: Double,
    val volatility: Double = 0.08
) {
    // Energy
    OIL("OIL", "النفط الخام", "Crude Oil", ResourceCategory.ENERGY, "🛢️", 70.0, "برميل", 35.0, 150.0, 0.12),
    NATURAL_GAS("GAS", "الغاز الطبيعي", "Natural Gas", ResourceCategory.ENERGY, "🔥", 45.0, "ألف م³", 20.0, 110.0, 0.10),
    COAL("COAL", "الفحم الحجري", "Coal", ResourceCategory.ENERGY, "⛏️", 25.0, "طن", 12.0, 60.0, 0.06),
    URANIUM("URAN", "اليورانيوم", "Uranium", ResourceCategory.ENERGY, "☢️", 450.0, "رطل", 200.0, 950.0, 0.09),
    ELECTRICITY("ELEC", "الكهرباء والطاقة", "Electricity", ResourceCategory.UTILITIES, "⚡", 15.0, "م.واط/ساعة", 6.0, 35.0, 0.07),

    // Metals & Minerals
    IRON_ORE("IRON", "خام الحديد", "Iron Ore", ResourceCategory.METALS, "🪨", 35.0, "طن", 16.0, 80.0, 0.07),
    COPPER("COPP", "النحاس", "Copper", ResourceCategory.METALS, "🥉", 80.0, "طن", 40.0, 180.0, 0.08),
    GOLD("GOLD", "الذهب", "Gold", ResourceCategory.METALS, "🪙", 1800.0, "أونصة", 1100.0, 2800.0, 0.05),
    SILVER("SILV", "الفضة", "Silver", ResourceCategory.METALS, "🥈", 25.0, "أونصة", 12.0, 60.0, 0.08),
    LITHIUM("LITH", "الليثيوم", "Lithium", ResourceCategory.HIGH_TECH, "🔋", 160.0, "طن", 80.0, 420.0, 0.14),
    RARE_EARTHS("RARE", "العناصر النادرة", "Rare Earths", ResourceCategory.HIGH_TECH, "💎", 300.0, "طن", 140.0, 750.0, 0.12),
    BAUXITE("BAUX", "البوكسيت (ألمنيوم)", "Bauxite", ResourceCategory.METALS, "🧱", 30.0, "طن", 14.0, 70.0, 0.06),
    NICKEL("NICK", "النيكل", "Nickel", ResourceCategory.METALS, "🔩", 90.0, "طن", 45.0, 210.0, 0.09),
    SILICON("SILI", "السيليكون الصناعي", "Silicon", ResourceCategory.HIGH_TECH, "🔬", 65.0, "طن", 30.0, 160.0, 0.08),

    // Agriculture & Raw Bio
    TIMBER("TMBR", "الأخشاب", "Timber", ResourceCategory.AGRICULTURE, "🌲", 20.0, "م³", 10.0, 48.0, 0.05),
    FISH("FISH", "الثروة السمكية", "Fish", ResourceCategory.AGRICULTURE, "🐟", 18.0, "طن", 9.0, 42.0, 0.06),
    WHEAT("WHT", "القمح والحبوب", "Wheat", ResourceCategory.AGRICULTURE, "🌾", 15.0, "طن", 7.0, 38.0, 0.08),
    RICE("RICE", "الأرز", "Rice", ResourceCategory.AGRICULTURE, "🍚", 18.0, "طن", 8.0, 45.0, 0.07),
    CORN("CORN", "الذرة", "Corn", ResourceCategory.AGRICULTURE, "🌽", 13.0, "طن", 6.0, 32.0, 0.07),
    COFFEE("COFF", "البن والمحاصيل النقدية", "Coffee", ResourceCategory.AGRICULTURE, "☕", 55.0, "طن", 25.0, 130.0, 0.10),
    COTTON("COTN", "القطن والألياف", "Cotton", ResourceCategory.AGRICULTURE, "🧵", 30.0, "طن", 14.0, 70.0, 0.07),
    RUBBER("RUBR", "المطاط الطبيعي", "Rubber", ResourceCategory.METALS, "🛞", 40.0, "طن", 18.0, 95.0, 0.08),
    SUGAR("SUGR", "السكر", "Sugar", ResourceCategory.AGRICULTURE, "🧂", 20.0, "طن", 9.0, 50.0, 0.07),
    LIVESTOCK("LIVE", "المواشي واللحوم", "Livestock", ResourceCategory.AGRICULTURE, "🐄", 35.0, "رأس", 16.0, 85.0, 0.06),
    WATER("WATR", "المياه العذبة", "Fresh Water", ResourceCategory.UTILITIES, "🚰", 10.0, "ألف م³", 4.0, 30.0, 0.05)
}

data class ResourceDefinition(
    val id: String,
    val type: ResourceType,
    val name: String,
    val unit: String,
    val basePrice: Double,
    val minPrice: Double,
    val maxPrice: Double,
    val productionCost: Double,
    val storageCost: Double,
    val globalSupply: Double,
    val globalDemand: Double,
    val volatility: Double
)

data class CountryResource(
    val resourceType: ResourceType,
    val reservesUnits: Double, // Reserves in millions of units
    val monthlyCapacityUnits: Double, // Max capacity per month (in thousands)
    val currentProductionUnits: Double, // Actual production this month
    val extractionCostPerUnit: Double, // Cost to produce 1 unit
    val governmentSharePercent: Double = 0.50,
    val privateSharePercent: Double = 0.50
) {
    val utilizationRatePercent: Double
        get() = if (monthlyCapacityUnits > 0) (currentProductionUnits / monthlyCapacityUnits * 100.0).coerceIn(0.0, 100.0) else 0.0

    fun calculateMonthlyGrossValue(marketPrice: Double): Double {
        return (currentProductionUnits * marketPrice) / 1000.0
    }

    fun calculateMonthlyGovernmentRevenue(marketPrice: Double): Double {
        val gross = calculateMonthlyGrossValue(marketPrice)
        val extractionCost = (currentProductionUnits * extractionCostPerUnit) / 1000.0
        val netProfit = (gross - extractionCost).coerceAtLeast(0.0)
        return (gross * 0.10) + (netProfit * governmentSharePercent)
    }
}
