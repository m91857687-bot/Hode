package com.example.model

enum class CountryScaleTier(
    val titleAr: String,
    val descriptionAr: String,
    val typicalGdpRangeBillions: ClosedFloatingPointRange<Double>,
    val typicalPopRangeMillions: ClosedFloatingPointRange<Double>,
    val costMultiplier: Double,
    val baseWageMultiplier: Double
) {
    TINY(
        titleAr = "دولة صغيرة / جزيرة",
        descriptionAr = "اقتصاد محدود محلي ذو تكاليف منخفضة وميزانيات مرنة",
        typicalGdpRangeBillions = 1.0..40.0,
        typicalPopRangeMillions = 0.2..4.0,
        costMultiplier = 0.15,
        baseWageMultiplier = 0.65
    ),
    SMALL(
        titleAr = "دولة صاعدة / متوسطة صغرى",
        descriptionAr = "اقتصاد متوسط نامٍ، يركز على القطاعات النوعية والخدمات",
        typicalGdpRangeBillions = 40.0..250.0,
        typicalPopRangeMillions = 4.0..20.0,
        costMultiplier = 0.40,
        baseWageMultiplier = 0.85
    ),
    MEDIUM(
        titleAr = "قوة إقليمية متوسطة",
        descriptionAr = "اقتصاد متنوع ذو قاعدة صناعية وزراعية وبنية تحتية واسعة",
        typicalGdpRangeBillions = 250.0..850.0,
        typicalPopRangeMillions = 20.0..60.0,
        costMultiplier = 1.00,
        baseWageMultiplier = 1.00
    ),
    LARGE(
        titleAr = "قوة اقتصادية كبرى",
        descriptionAr = "سوق ضخم وإنتاج صناعي واستثمارات دولية نشطة",
        typicalGdpRangeBillions = 850.0..2500.0,
        typicalPopRangeMillions = 60.0..140.0,
        costMultiplier = 2.20,
        baseWageMultiplier = 1.25
    ),
    MAJOR(
        titleAr = "عملاق اقتصادي قاري",
        descriptionAr = "ميزانيات حكومية عملاقة، شركات متعددة الجنسيات، وقوة استهلاك واسعة",
        typicalGdpRangeBillions = 2500.0..8000.0,
        typicalPopRangeMillions = 140.0..400.0,
        costMultiplier = 4.50,
        baseWageMultiplier = 1.50
    ),
    GLOBAL_POWER(
        titleAr = "قوة عظمى عالمية",
        descriptionAr = "أكبر اقتصادات الكوكب ذات تأثير مباشر على السوق والتجارة العالمية",
        typicalGdpRangeBillions = 8000.0..40000.0,
        typicalPopRangeMillions = 300.0..1500.0,
        costMultiplier = 9.00,
        baseWageMultiplier = 1.80
    );

    companion object {
        fun fromGdpAndPop(gdpBillions: Double, populationMillions: Double): CountryScaleTier {
            return when {
                gdpBillions >= 8000.0 || (populationMillions >= 300.0 && gdpBillions >= 2500.0) -> GLOBAL_POWER
                gdpBillions >= 2500.0 || populationMillions >= 140.0 -> MAJOR
                gdpBillions >= 850.0 || populationMillions >= 60.0 -> LARGE
                gdpBillions >= 250.0 || populationMillions >= 20.0 -> MEDIUM
                gdpBillions >= 40.0 || populationMillions >= 4.0 -> SMALL
                else -> TINY
            }
        }
    }
}
