package com.example.data

import com.example.model.Company
import com.example.model.Country
import com.example.model.CountryArchetype
import com.example.model.InfluenceEntry

object WorldData {

    fun getInitialCountries(): Map<String, Country> {
        val list = listOf(
            Country(
                id = "SA",
                nameAr = "المملكة العربية السعودية",
                nameEn = "Saudi Arabia",
                capital = "الرياض",
                flag = "🇸🇦",
                populationMillions = 36.5,
                gdpBillions = 1100.0,
                gdpGrowthPercent = 3.8,
                treasuryBillions = 120.0,
                sovereignDebtBillions = 260.0,
                inflationRate = 2.0,
                unemploymentRate = 4.7,
                taxRate = 0.15,
                stability = 88,
                industryIndex = 75,
                agricultureIndex = 45,
                energyIndex = 98,
                infrastructureIndex = 86,
                educationIndex = 80,
                healthIndex = 82,
                militaryIndex = 85,
                tradeSurplusBillions = 45.0,
                mainResources = listOf("نفط خام", "غاز طبيعي", "بتروكيماويات", "طاقة شمسية"),
                aiArchetype = CountryArchetype.RESOURCE_RICH,
                mapX = 0.58f,
                mapY = 0.44f,
                relationsWithPlayer = 70
            ),
            Country(
                id = "EG",
                nameAr = "جمهورية مصر العربية",
                nameEn = "Egypt",
                capital = "القاهرة",
                flag = "🇪🇬",
                populationMillions = 110.0,
                gdpBillions = 430.0,
                gdpGrowthPercent = 4.2,
                treasuryBillions = 35.0,
                sovereignDebtBillions = 165.0,
                inflationRate = 12.5,
                unemploymentRate = 6.9,
                taxRate = 0.22,
                stability = 74,
                industryIndex = 62,
                agricultureIndex = 68,
                energyIndex = 70,
                infrastructureIndex = 66,
                educationIndex = 65,
                healthIndex = 64,
                militaryIndex = 80,
                tradeSurplusBillions = -14.0,
                mainResources = listOf("غاز طبيعي", "قناة السويس", "زراعة", "سياحة"),
                aiArchetype = CountryArchetype.EMERGING_GIANT,
                mapX = 0.54f,
                mapY = 0.42f,
                relationsWithPlayer = 75
            ),
            Country(
                id = "AE",
                nameAr = "الإمارات العربية المتحدة",
                nameEn = "United Arab Emirates",
                capital = "أبوظبي",
                flag = "🇦🇪",
                populationMillions = 10.2,
                gdpBillions = 510.0,
                gdpGrowthPercent = 4.5,
                treasuryBillions = 95.0,
                sovereignDebtBillions = 140.0,
                inflationRate = 2.4,
                unemploymentRate = 2.8,
                taxRate = 0.09,
                stability = 92,
                industryIndex = 70,
                agricultureIndex = 30,
                energyIndex = 90,
                infrastructureIndex = 94,
                educationIndex = 84,
                healthIndex = 86,
                militaryIndex = 74,
                tradeSurplusBillions = 50.0,
                mainResources = listOf("خدمات مالية", "لوجستيات وموانئ", "نفط", "سياحة"),
                aiArchetype = CountryArchetype.TRADE_HUB,
                mapX = 0.61f,
                mapY = 0.45f,
                relationsWithPlayer = 80
            ),
            Country(
                id = "US",
                nameAr = "الولايات المتحدة الأمريكية",
                nameEn = "United States",
                capital = "واشنطن",
                flag = "🇺🇸",
                populationMillions = 340.0,
                gdpBillions = 27500.0,
                gdpGrowthPercent = 2.4,
                treasuryBillions = 480.0,
                sovereignDebtBillions = 33000.0,
                inflationRate = 3.1,
                unemploymentRate = 3.9,
                taxRate = 0.25,
                stability = 85,
                industryIndex = 88,
                agricultureIndex = 82,
                energyIndex = 89,
                infrastructureIndex = 88,
                educationIndex = 92,
                healthIndex = 85,
                militaryIndex = 98,
                tradeSurplusBillions = -850.0,
                mainResources = listOf("تقنية فائقة", "دولار عالمي", "طاقة صخرية", "صناعة دفاعية"),
                aiArchetype = CountryArchetype.SUPERPOWER,
                mapX = 0.22f,
                mapY = 0.36f,
                relationsWithPlayer = 60
            ),
            Country(
                id = "CN",
                nameAr = "جمهورية الصين الشعبية",
                nameEn = "China",
                capital = "بكين",
                flag = "🇨🇳",
                populationMillions = 1410.0,
                gdpBillions = 18000.0,
                gdpGrowthPercent = 4.8,
                treasuryBillions = 650.0,
                sovereignDebtBillions = 14000.0,
                inflationRate = 1.2,
                unemploymentRate = 5.1,
                taxRate = 0.25,
                stability = 86,
                industryIndex = 97,
                agricultureIndex = 78,
                energyIndex = 82,
                infrastructureIndex = 95,
                educationIndex = 86,
                healthIndex = 80,
                militaryIndex = 94,
                tradeSurplusBillions = 680.0,
                mainResources = listOf("مصنع العالم", "أتربة نادرة", "إلكترونيات", "بنية تحتية"),
                aiArchetype = CountryArchetype.SUPERPOWER,
                mapX = 0.78f,
                mapY = 0.38f,
                relationsWithPlayer = 65
            ),
            Country(
                id = "DE",
                nameAr = "جمهورية ألمانيا الاتحادية",
                nameEn = "Germany",
                capital = "برلين",
                flag = "🇩🇪",
                populationMillions = 84.0,
                gdpBillions = 4400.0,
                gdpGrowthPercent = 0.8,
                treasuryBillions = 110.0,
                sovereignDebtBillions = 2800.0,
                inflationRate = 2.6,
                unemploymentRate = 5.7,
                taxRate = 0.30,
                stability = 88,
                industryIndex = 93,
                agricultureIndex = 65,
                energyIndex = 72,
                infrastructureIndex = 91,
                educationIndex = 90,
                healthIndex = 91,
                militaryIndex = 70,
                tradeSurplusBillions = 210.0,
                mainResources = listOf("هندسة وميكانيكا", "سيارات فاخرة", "كيماويات"),
                aiArchetype = CountryArchetype.INDUSTRIAL,
                mapX = 0.51f,
                mapY = 0.28f,
                relationsWithPlayer = 60
            ),
            Country(
                id = "JP",
                nameAr = "اليابان",
                nameEn = "Japan",
                capital = "طوكيو",
                flag = "🇯🇵",
                populationMillions = 124.0,
                gdpBillions = 4200.0,
                gdpGrowthPercent = 1.2,
                treasuryBillions = 140.0,
                sovereignDebtBillions = 9500.0,
                inflationRate = 2.5,
                unemploymentRate = 2.6,
                taxRate = 0.28,
                stability = 94,
                industryIndex = 92,
                agricultureIndex = 48,
                energyIndex = 68,
                infrastructureIndex = 96,
                educationIndex = 92,
                healthIndex = 94,
                militaryIndex = 72,
                tradeSurplusBillions = 40.0,
                mainResources = listOf("روبوتات وأشباه موصلات", "سيارات", "تقنية متقدمة"),
                aiArchetype = CountryArchetype.INDUSTRIAL,
                mapX = 0.88f,
                mapY = 0.37f,
                relationsWithPlayer = 68
            ),
            Country(
                id = "GB",
                nameAr = "المملكة المتحدة",
                nameEn = "United Kingdom",
                capital = "لندن",
                flag = "🇬🇧",
                populationMillions = 68.0,
                gdpBillions = 3300.0,
                gdpGrowthPercent = 1.4,
                treasuryBillions = 75.0,
                sovereignDebtBillions = 3100.0,
                inflationRate = 3.2,
                unemploymentRate = 4.2,
                taxRate = 0.26,
                stability = 84,
                industryIndex = 78,
                agricultureIndex = 58,
                energyIndex = 74,
                infrastructureIndex = 87,
                educationIndex = 91,
                healthIndex = 86,
                militaryIndex = 84,
                tradeSurplusBillions = -90.0,
                mainResources = listOf("بنوك وأسواق مالية", "صناعات طيران ودفاع", "تعليم عالي"),
                aiArchetype = CountryArchetype.TRADE_HUB,
                mapX = 0.47f,
                mapY = 0.27f,
                relationsWithPlayer = 62
            ),
            Country(
                id = "IN",
                nameAr = "جمهورية الهند",
                nameEn = "India",
                capital = "نيودلهي",
                flag = "🇮🇳",
                populationMillions = 1428.0,
                gdpBillions = 3750.0,
                gdpGrowthPercent = 6.8,
                treasuryBillions = 85.0,
                sovereignDebtBillions = 2900.0,
                inflationRate = 4.9,
                unemploymentRate = 6.1,
                taxRate = 0.22,
                stability = 78,
                industryIndex = 80,
                agricultureIndex = 85,
                energyIndex = 74,
                infrastructureIndex = 72,
                educationIndex = 74,
                healthIndex = 70,
                militaryIndex = 88,
                tradeSurplusBillions = -70.0,
                mainResources = listOf("برمجيات وتقنية معلومات", "صناعات دوائية", "زراعة واسعة"),
                aiArchetype = CountryArchetype.EMERGING_GIANT,
                mapX = 0.70f,
                mapY = 0.46f,
                relationsWithPlayer = 65
            ),
            Country(
                id = "BR",
                nameAr = "جمهورية البرازيل الاتحادية",
                nameEn = "Brazil",
                capital = "برازيليا",
                flag = "🇧🇷",
                populationMillions = 215.0,
                gdpBillions = 2100.0,
                gdpGrowthPercent = 2.9,
                treasuryBillions = 65.0,
                sovereignDebtBillions = 1600.0,
                inflationRate = 4.0,
                unemploymentRate = 7.6,
                taxRate = 0.28,
                stability = 76,
                industryIndex = 72,
                agricultureIndex = 94,
                energyIndex = 84,
                infrastructureIndex = 68,
                educationIndex = 72,
                healthIndex = 71,
                militaryIndex = 68,
                tradeSurplusBillions = 75.0,
                mainResources = listOf("فول الصويا واللحوم", "حديد ومعادن", "طاقة كهرومائية", "نفط بحري"),
                aiArchetype = CountryArchetype.RESOURCE_RICH,
                mapX = 0.33f,
                mapY = 0.67f,
                relationsWithPlayer = 55
            ),
            Country(
                id = "TR",
                nameAr = "الجمهورية التركية",
                nameEn = "Turkey",
                capital = "أنقرة",
                flag = "🇹🇷",
                populationMillions = 86.0,
                gdpBillions = 1150.0,
                gdpGrowthPercent = 4.0,
                treasuryBillions = 42.0,
                sovereignDebtBillions = 480.0,
                inflationRate = 28.0,
                unemploymentRate = 8.5,
                taxRate = 0.23,
                stability = 77,
                industryIndex = 79,
                agricultureIndex = 72,
                energyIndex = 66,
                infrastructureIndex = 83,
                educationIndex = 76,
                healthIndex = 78,
                militaryIndex = 86,
                tradeSurplusBillions = -35.0,
                mainResources = listOf("صناعات دفاعية ومسيرات", "نسيج وسيارات", "ممر طاقة ولوجستيات"),
                aiArchetype = CountryArchetype.TRADE_HUB,
                mapX = 0.56f,
                mapY = 0.35f,
                relationsWithPlayer = 68
            )
        )
        return list.associateBy { it.id }
    }

    fun getInitialCompanies(playerCountryId: String): List<Company> {
        val list = mutableListOf<Company>()
        when (playerCountryId) {
            "SA" -> {
                list.add(Company("sa_1", "أرامكو الوطنية للطاقة", "الطاقة والنفط", "SA", 2100.0, 115000.0, 0.88, 70000, true, "🛢️"))
                list.add(Company("sa_2", "سابك للصناعات البتروكيماوية", "البتروكيماويات", "SA", 85.0, 6200.0, 0.70, 31000, true, "🧪"))
                list.add(Company("sa_3", "مجموعة الاتصالات السعودية (stc)", "الاتصالات والتقنية", "SA", 65.0, 3400.0, 0.64, 25000, true, "📡"))
                list.add(Company("sa_4", "شركة معادن الوطنية", "التعدين والموارد", "SA", 42.0, 2100.0, 0.67, 18000, true, "⛏️"))
            }
            "EG" -> {
                list.add(Company("eg_1", "هيئة قناة السويس اللوجستية", "الملاحة واللوجستيات", "EG", 90.0, 9500.0, 1.0, 35000, true, "🚢"))
                list.add(Company("eg_2", "المصرية للاتصالات (وي)", "الاتصالات", "EG", 18.0, 1200.0, 0.80, 40000, true, "📡"))
                list.add(Company("eg_3", "مصر للأسمدة والكيماويات", "الصناعة", "EG", 12.0, 850.0, 0.51, 15000, true, "🏭"))
            }
            "AE" -> {
                list.add(Company("ae_1", "موانئ دبي العالمية (DP World)", "الموانئ والتجارة العالمية", "AE", 60.0, 4800.0, 0.80, 50000, true, "🚢"))
                list.add(Company("ae_2", "مجموعة إي آند للتكنولوجيا (e&)", "الاتصالات والذكاء الاصطناعي", "AE", 55.0, 3900.0, 0.60, 45000, true, "🤖"))
                list.add(Company("ae_3", "أدنوك للتوزيع والطاقة", "الطاقة", "AE", 48.0, 3200.0, 0.75, 28000, true, "⚡"))
            }
            else -> {
                list.add(Company("gen_1", "الشركة الوطنية القابضة للاستثمار", "الاستثمار والمالية", playerCountryId, 50.0, 3500.0, 0.65, 20000, true, "🏛️"))
                list.add(Company("gen_2", "الشبكة الوطنية للطاقة والمرافق", "الطاقة", playerCountryId, 40.0, 2800.0, 0.70, 18000, true, "⚡"))
                list.add(Company("gen_3", "مجمع التصنيع الوطني المتقدم", "الصناعة", playerCountryId, 30.0, 1900.0, 0.51, 15000, true, "🏭"))
            }
        }
        return list
    }

    fun getInitialInfluenceNetwork(playerCountryId: String, allCountryIds: List<String>): List<InfluenceEntry> {
        val entries = mutableListOf<InfluenceEntry>()
        for (target in allCountryIds) {
            if (target != playerCountryId) {
                // Player's initial influence in target country
                entries.add(
                    InfluenceEntry(
                        sourceCountryId = playerCountryId,
                        targetCountryId = target,
                        economicInfluence = (15..35).random(),
                        commercialInfluence = (10..30).random(),
                        diplomaticInfluence = (20..40).random(),
                        strategicInfluence = (10..25).random()
                    )
                )
                // Superpowers initial influence in target
                entries.add(
                    InfluenceEntry(
                        sourceCountryId = "US",
                        targetCountryId = target,
                        economicInfluence = 45,
                        commercialInfluence = 38,
                        diplomaticInfluence = 50,
                        strategicInfluence = 48
                    )
                )
                entries.add(
                    InfluenceEntry(
                        sourceCountryId = "CN",
                        targetCountryId = target,
                        economicInfluence = 48,
                        commercialInfluence = 45,
                        diplomaticInfluence = 35,
                        strategicInfluence = 30
                    )
                )
            }
        }
        return entries
    }
}
