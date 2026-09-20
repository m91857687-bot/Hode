package com.example.model

enum class FactoryType(
    val code: String,
    val nameAr: String,
    val descriptionAr: String,
    val icon: String,
    val requiredInput: ResourceType?,
    val secondaryInput: ResourceType?,
    val outputNameAr: String,
    val baseCostMillions: Double,
    val baseWorkersNeeded: Int,
    val baseInputPerMonth: Double,
    val baseOutputPerMonth: Double,
    val baseValuePerUnit: Double
) {
    STEEL_MILL(
        code = "STEEL",
        nameAr = "مجمع صلب وصلب صناعي",
        descriptionAr = "يحول خام الحديد والطاقة إلى سبائك الصلب اللازمة للبناء والأسلحة والآليات.",
        icon = "🏗️",
        requiredInput = ResourceType.IRON_ORE,
        secondaryInput = ResourceType.COAL,
        outputNameAr = "صلب صناعي",
        baseCostMillions = 24.0,
        baseWorkersNeeded = 3200,
        baseInputPerMonth = 40.0,
        baseOutputPerMonth = 32.0,
        baseValuePerUnit = 780.0
    ),
    OIL_REFINERY(
        code = "REFINERY",
        nameAr = "مصفاة تكرير وبتروكيماويات",
        descriptionAr = "تحويل النفط الخام إلى وقود عالي الجودة ولدائن وبتروكيماويات متطورة.",
        icon = "🛢️",
        requiredInput = ResourceType.OIL,
        secondaryInput = null,
        outputNameAr = "وقود وبتروكيماويات",
        baseCostMillions = 35.0,
        baseWorkersNeeded = 2500,
        baseInputPerMonth = 60.0,
        baseOutputPerMonth = 58.0,
        baseValuePerUnit = 120.0
    ),
    FERTILIZER_PLANT(
        code = "FERTILIZER",
        nameAr = "مصنع أسمدة كيميائية",
        descriptionAr = "استغلال الغاز الطبيعي لإنتاج اليوريا والأسمدة لدعم الإنتاج الزراعي والتصدير.",
        icon = "🧪",
        requiredInput = ResourceType.NATURAL_GAS,
        secondaryInput = null,
        outputNameAr = "أسمدة زراعية",
        baseCostMillions = 18.0,
        baseWorkersNeeded = 1800,
        baseInputPerMonth = 50.0,
        baseOutputPerMonth = 45.0,
        baseValuePerUnit = 380.0
    ),
    FOOD_PROCESSING(
        code = "FOOD",
        nameAr = "مجمع صناعات غذائية",
        descriptionAr = "تصنيع وتعليب منتجات الحبوب والزيوت والمواد الغذائية لدعم الأمن الغذائي.",
        icon = "🥫",
        requiredInput = ResourceType.WHEAT,
        secondaryInput = null,
        outputNameAr = "أغذية مصنعة",
        baseCostMillions = 12.0,
        baseWorkersNeeded = 2200,
        baseInputPerMonth = 30.0,
        baseOutputPerMonth = 28.0,
        baseValuePerUnit = 550.0
    ),
    BATTERY_GIGAFACTORY(
        code = "BATTERY",
        nameAr = "مصنع بطاريات عملاق",
        descriptionAr = "تحويل الليثيوم والعناصر النادرة إلى خلايا بطاريات لتخزين الطاقة والسيارات الكهربائية.",
        icon = "🔋",
        requiredInput = ResourceType.LITHIUM,
        secondaryInput = ResourceType.RARE_EARTHS,
        outputNameAr = "بطاريات متقدمة",
        baseCostMillions = 50.0,
        baseWorkersNeeded = 4000,
        baseInputPerMonth = 15.0,
        baseOutputPerMonth = 14.0,
        baseValuePerUnit = 2400.0
    ),
    SEMICONDUCTOR_FAB(
        code = "SEMI",
        nameAr = "مسبك أشباه الموصلات والرقائق",
        descriptionAr = "تصنيع رقائق السيليكون عالية الدقة لتشغيل الحواسيب والمعدات الذكية.",
        icon = "🔬",
        requiredInput = ResourceType.SILICON,
        secondaryInput = ResourceType.RARE_EARTHS,
        outputNameAr = "رقائق إلكترونية",
        baseCostMillions = 90.0,
        baseWorkersNeeded = 5000,
        baseInputPerMonth = 10.0,
        baseOutputPerMonth = 9.5,
        baseValuePerUnit = 8500.0
    ),
    COPPER_WORKS(
        code = "COPPER_W",
        nameAr = "مصنع كابلات ومنتجات نحاسية",
        descriptionAr = "تشكيل النحاس لشبكات الطاقة والاتصالات والمحركات الكهربائية.",
        icon = "🔌",
        requiredInput = ResourceType.COPPER,
        secondaryInput = null,
        outputNameAr = "كابلات وموصلات",
        baseCostMillions = 16.0,
        baseWorkersNeeded = 1900,
        baseInputPerMonth = 20.0,
        baseOutputPerMonth = 19.0,
        baseValuePerUnit = 12000.0
    ),
    AUTOMOTIVE_ASSEMBLY(
        code = "AUTO",
        nameAr = "مصنع مركبات وآليات",
        descriptionAr = "تجميع الشاحنات والسيارات والمركبات الثقيلة لدعم قطاع النقل والتصدير.",
        icon = "🚗",
        requiredInput = ResourceType.IRON_ORE,
        secondaryInput = ResourceType.RUBBER,
        outputNameAr = "مركبات ومعدات",
        baseCostMillions = 45.0,
        baseWorkersNeeded = 4500,
        baseInputPerMonth = 25.0,
        baseOutputPerMonth = 22.0,
        baseValuePerUnit = 3200.0
    ),
    ALUMINUM_SMELTER(
        code = "ALUM",
        nameAr = "مصهر الألمنيوم والسبائك الخفيفة",
        descriptionAr = "تحويل خام البوكسيت والطاقة الكهربائية إلى سبائك ألمنيوم لقطاعات الطيران والسيارات والبناء.",
        icon = "🧱",
        requiredInput = ResourceType.BAUXITE,
        secondaryInput = ResourceType.ELECTRICITY,
        outputNameAr = "ألمنيوم صناعي",
        baseCostMillions = 32.0,
        baseWorkersNeeded = 2800,
        baseInputPerMonth = 35.0,
        baseOutputPerMonth = 30.0,
        baseValuePerUnit = 2100.0
    ),
    CHEMICAL_PLANT(
        code = "CHEM",
        nameAr = "مجمع الكيماويات واللدائن",
        descriptionAr = "معالجة مشتقات الوقود والغاز لإنتاج البلاستيك والبوليمرات والمواد الكيميائية الأساسية.",
        icon = "🧪",
        requiredInput = ResourceType.OIL,
        secondaryInput = ResourceType.NATURAL_GAS,
        outputNameAr = "كيماويات ولدائن",
        baseCostMillions = 28.0,
        baseWorkersNeeded = 2200,
        baseInputPerMonth = 40.0,
        baseOutputPerMonth = 38.0,
        baseValuePerUnit = 1600.0
    ),
    MILITARY_VEHICLE_FACTORY(
        code = "MIL_VEH",
        nameAr = "مجمع الصناعات والمدرعات العسكرية",
        descriptionAr = "إنتاج الآليات المصفحة وقطع الغيار والمعدات الدفاعية الاستراتيجية لدعم القوات المسلحة.",
        icon = "🛡️",
        requiredInput = ResourceType.IRON_ORE,
        secondaryInput = ResourceType.RUBBER,
        outputNameAr = "عتاد ومدرعات عسكرية",
        baseCostMillions = 55.0,
        baseWorkersNeeded = 4200,
        baseInputPerMonth = 20.0,
        baseOutputPerMonth = 18.0,
        baseValuePerUnit = 4800.0
    ),
    STRATEGIC_ROCKET_FACTORY(
        code = "ROCKET",
        nameAr = "مجمع الصناعات الفضائية والردع الصاروخي",
        descriptionAr = "تطوير وحدات الردع الاستراتيجي الصاروخي وأنظمة الفضاء بتكامل سبائك الصلب والإلكترونيات المتقدمة.",
        icon = "🚀",
        requiredInput = ResourceType.IRON_ORE,
        secondaryInput = ResourceType.SILICON,
        outputNameAr = "منظومات ردع استراتيجي",
        baseCostMillions = 85.0,
        baseWorkersNeeded = 3600,
        baseInputPerMonth = 15.0,
        baseOutputPerMonth = 10.0,
        baseValuePerUnit = 9500.0
    )
}

data class Factory(
    val id: String,
    val countryId: String,
    val nameAr: String,
    val type: FactoryType,
    val level: Int = 1, // 1 to 5
    val efficiencyPercent: Int = 85, // 0 - 100
    val isPaused: Boolean = false,
    val isStateOwned: Boolean = true,
    val stateOwnershipRatio: Double = 1.0,
    val customWorkersCount: Int? = null
) {
    val levelMultiplier: Double
        get() = 1.0 + (level - 1) * 0.45

    val actualWorkers: Int
        get() = customWorkersCount ?: (type.baseWorkersNeeded * levelMultiplier).toInt()

    val monthlyInputNeeded: Double
        get() = if (isPaused) 0.0 else type.baseInputPerMonth * levelMultiplier * (efficiencyPercent / 100.0)

    val monthlySecondaryInputNeeded: Double
        get() = if (isPaused || type.secondaryInput == null) 0.0 else (type.baseInputPerMonth * 0.5) * levelMultiplier * (efficiencyPercent / 100.0)

    val monthlyOutputProduced: Double
        get() = if (isPaused) 0.0 else type.baseOutputPerMonth * levelMultiplier * (efficiencyPercent / 100.0)

    val monthlyRevenueMillions: Double
        get() = if (isPaused) 0.0 else (monthlyOutputProduced * type.baseValuePerUnit) / 1000.0

    val monthlyMaintenanceMillions: Double
        get() = (type.baseCostMillions * 0.012 * levelMultiplier) * (if (isPaused) 0.3 else 1.0)

    val monthlyWagesMillions: Double
        get() = (actualWorkers * 2200.0) / 1_000_000.0

    val monthlyGrossProfitMillions: Double
        get() = monthlyRevenueMillions - (monthlyMaintenanceMillions + monthlyWagesMillions)

    val monthlyStateDividendsMillions: Double
        get() = if (isStateOwned) monthlyGrossProfitMillions.coerceAtLeast(0.0) * stateOwnershipRatio else 0.0

    val upgradeCostMillions: Double
        get() = type.baseCostMillions * 0.65 * (level + 1)
}
