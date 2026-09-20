package com.example.data

import com.example.model.Company
import com.example.model.Continent
import com.example.model.Country
import com.example.model.CountryArchetype
import com.example.model.CountryDefinition
import com.example.model.CountryResource
import com.example.model.CountryState
import com.example.model.EconomicTier
import com.example.model.Factory
import com.example.model.FactoryType
import com.example.model.InfluenceEntry
import com.example.model.MarketCommodity
import com.example.model.ResourceType
import com.example.model.TaxProfile
import com.example.model.TradeContract

object WorldData {

    fun getInitialCountries(): Map<String, Country> {
        val list = mutableListOf<Country>()

        // 1. SAUDI ARABIA (SAU)
        list.add(
            createCountry(
                id = "SAU",
                iso2 = "SA",
                nameAr = "المملكة العربية السعودية",
                nameEn = "Saudi Arabia",
                capital = "الرياض",
                continent = Continent.MIDDLE_EAST,
                regionAr = "شبه الجزيرة العربية والخليج",
                flag = "🇸🇦",
                currencyNameAr = "ريال سعودي",
                currencySymbol = "ر.س",
                populationMillions = 36.5,
                gdpBillions = 1100.0,
                gdpGrowthPercent = 3.8,
                treasuryBillions = 120.0,
                sovereignDebtBillions = 260.0,
                inflationRate = 2.0,
                unemploymentRate = 4.7,
                stability = 88,
                archetype = CountryArchetype.RESOURCE_RICH,
                tier = EconomicTier.MAJOR,
                mapX = 0.58f,
                mapY = 0.44f,
                taxProfile = TaxProfile(incomeTaxRate = 0.0, corporateTaxRate = 0.20, vatRate = 0.15, importTariffRate = 0.05),
                primaryResources = listOf(ResourceType.OIL, ResourceType.NATURAL_GAS, ResourceType.GOLD, ResourceType.RARE_EARTHS),
                factories = listOf(
                    Factory("sau_f1", "SAU", "مصفاة رأس تنورة وبترورابغ", FactoryType.OIL_REFINERY, level = 4, efficiencyPercent = 95),
                    Factory("sau_f2", "SAU", "مجمع سابك للأسمدة المتقدمة", FactoryType.FERTILIZER_PLANT, level = 3, efficiencyPercent = 90),
                    Factory("sau_f3", "SAU", "مجمع حديد سابك الصناعي", FactoryType.STEEL_MILL, level = 3, efficiencyPercent = 88)
                )
            )
        )

        // 2. YEMEN (YEM)
        list.add(
            createCountry(
                id = "YEM",
                iso2 = "YE",
                nameAr = "الجمهورية اليمنية",
                nameEn = "Yemen",
                capital = "صنعاء",
                continent = Continent.MIDDLE_EAST,
                regionAr = "جنوب شبه الجزيرة العربية",
                flag = "🇾🇪",
                currencyNameAr = "ريال يمني",
                currencySymbol = "﷼",
                populationMillions = 34.2,
                gdpBillions = 22.5,
                gdpGrowthPercent = 1.2,
                treasuryBillions = 1.6,
                sovereignDebtBillions = 9.8,
                inflationRate = 18.5,
                unemploymentRate = 24.0,
                stability = 42,
                archetype = CountryArchetype.DEVELOPING,
                tier = EconomicTier.MICRO,
                mapX = 0.59f,
                mapY = 0.48f,
                taxProfile = TaxProfile(incomeTaxRate = 0.15, corporateTaxRate = 0.20, vatRate = 0.05, importTariffRate = 0.10),
                primaryResources = listOf(ResourceType.OIL, ResourceType.NATURAL_GAS, ResourceType.COFFEE, ResourceType.GOLD),
                factories = listOf(
                    Factory("yem_f1", "YEM", "مصفاة عدن التاريخية", FactoryType.OIL_REFINERY, level = 1, efficiencyPercent = 60),
                    Factory("yem_f2", "YEM", "مطاحن ومجمع صوامع الغلال بالحديدة", FactoryType.FOOD_PROCESSING, level = 1, efficiencyPercent = 65)
                )
            )
        )

        // 3. UAE (ARE)
        list.add(
            createCountry(
                id = "ARE",
                iso2 = "AE",
                nameAr = "الإمارات العربية المتحدة",
                nameEn = "United Arab Emirates",
                capital = "أبوظبي",
                continent = Continent.MIDDLE_EAST,
                regionAr = "الخليج العربي",
                flag = "🇦🇪",
                currencyNameAr = "درهم إماراتي",
                currencySymbol = "د.إ",
                populationMillions = 10.2,
                gdpBillions = 510.0,
                gdpGrowthPercent = 4.5,
                treasuryBillions = 95.0,
                sovereignDebtBillions = 140.0,
                inflationRate = 2.4,
                unemploymentRate = 2.8,
                stability = 92,
                archetype = CountryArchetype.TRADE_HUB,
                tier = EconomicTier.LARGE,
                mapX = 0.61f,
                mapY = 0.45f,
                taxProfile = TaxProfile(incomeTaxRate = 0.0, corporateTaxRate = 0.09, vatRate = 0.05, importTariffRate = 0.04),
                primaryResources = listOf(ResourceType.OIL, ResourceType.NATURAL_GAS, ResourceType.SILICON),
                factories = listOf(
                    Factory("are_f1", "ARE", "مصفاة الرويس العالمية", FactoryType.OIL_REFINERY, level = 4, efficiencyPercent = 96),
                    Factory("are_f2", "ARE", "مسبك الإمارات العالمية للألمنيوم والسبائك", FactoryType.STEEL_MILL, level = 3, efficiencyPercent = 92)
                )
            )
        )

        // 4. QATAR (QAT)
        list.add(
            createCountry(
                id = "QAT",
                iso2 = "QA",
                nameAr = "دولة قطر",
                nameEn = "Qatar",
                capital = "الدوحة",
                continent = Continent.MIDDLE_EAST,
                regionAr = "الخليج العربي",
                flag = "🇶🇦",
                currencyNameAr = "ريال قطري",
                currencySymbol = "ر.ق",
                populationMillions = 2.9,
                gdpBillions = 230.0,
                gdpGrowthPercent = 3.6,
                treasuryBillions = 68.0,
                sovereignDebtBillions = 85.0,
                inflationRate = 1.9,
                unemploymentRate = 0.5,
                stability = 90,
                archetype = CountryArchetype.RESOURCE_RICH,
                tier = EconomicTier.LARGE,
                mapX = 0.605f,
                mapY = 0.445f,
                taxProfile = TaxProfile(incomeTaxRate = 0.0, corporateTaxRate = 0.10, vatRate = 0.0, importTariffRate = 0.05),
                primaryResources = listOf(ResourceType.NATURAL_GAS, ResourceType.OIL, ResourceType.SILICON),
                factories = listOf(
                    Factory("qat_f1", "QAT", "مجمع قطر للطاقة للغاز المسال", FactoryType.FERTILIZER_PLANT, level = 4, efficiencyPercent = 98)
                )
            )
        )

        // 5. KUWAIT (KWT)
        list.add(
            createCountry(
                id = "KWT",
                iso2 = "KW",
                nameAr = "دولة الكويت",
                nameEn = "Kuwait",
                capital = "مدينة الكويت",
                continent = Continent.MIDDLE_EAST,
                regionAr = "شمال الخليج العربي",
                flag = "🇰🇼",
                currencyNameAr = "دينار كويتي",
                currencySymbol = "د.ك",
                populationMillions = 4.3,
                gdpBillions = 160.0,
                gdpGrowthPercent = 2.5,
                treasuryBillions = 45.0,
                sovereignDebtBillions = 32.0,
                inflationRate = 2.8,
                unemploymentRate = 2.2,
                stability = 85,
                archetype = CountryArchetype.RESOURCE_RICH,
                tier = EconomicTier.LARGE,
                mapX = 0.595f,
                mapY = 0.435f,
                primaryResources = listOf(ResourceType.OIL, ResourceType.NATURAL_GAS),
                factories = listOf(
                    Factory("kwt_f1", "KWT", "مصفاة ميناء الأحمدي ومصفاة الزور", FactoryType.OIL_REFINERY, level = 4, efficiencyPercent = 92)
                )
            )
        )

        // 6. OMAN (OMN)
        list.add(
            createCountry(
                id = "OMN",
                iso2 = "OM",
                nameAr = "سلطنة عُمان",
                nameEn = "Oman",
                capital = "مسقط",
                continent = Continent.MIDDLE_EAST,
                regionAr = "بحر العرب ومدخل الخليج",
                flag = "🇴🇲",
                currencyNameAr = "ريال عماني",
                currencySymbol = "ر.ع",
                populationMillions = 4.6,
                gdpBillions = 110.0,
                gdpGrowthPercent = 3.2,
                treasuryBillions = 14.5,
                sovereignDebtBillions = 42.0,
                inflationRate = 1.8,
                unemploymentRate = 3.8,
                stability = 88,
                archetype = CountryArchetype.TRADE_HUB,
                tier = EconomicTier.MEDIUM,
                mapX = 0.62f,
                mapY = 0.465f,
                primaryResources = listOf(ResourceType.OIL, ResourceType.NATURAL_GAS, ResourceType.COPPER),
                factories = listOf(
                    Factory("omn_f1", "OMN", "مجمع صحار للبتروكيماويات والحديد", FactoryType.STEEL_MILL, level = 2, efficiencyPercent = 86)
                )
            )
        )

        // 7. EGYPT (EGY)
        list.add(
            createCountry(
                id = "EGY",
                iso2 = "EG",
                nameAr = "جمهورية مصر العربية",
                nameEn = "Egypt",
                capital = "القاهرة",
                continent = Continent.MIDDLE_EAST,
                regionAr = "شمال إفريقيا وحوض النيل",
                flag = "🇪🇬",
                currencyNameAr = "جنيه مصري",
                currencySymbol = "ج.م",
                populationMillions = 110.0,
                gdpBillions = 430.0,
                gdpGrowthPercent = 4.2,
                treasuryBillions = 35.0,
                sovereignDebtBillions = 165.0,
                inflationRate = 12.5,
                unemploymentRate = 6.9,
                stability = 74,
                archetype = CountryArchetype.EMERGING_GIANT,
                tier = EconomicTier.LARGE,
                mapX = 0.54f,
                mapY = 0.42f,
                primaryResources = listOf(ResourceType.NATURAL_GAS, ResourceType.GOLD, ResourceType.COTTON, ResourceType.WHEAT),
                factories = listOf(
                    Factory("egy_f1", "EGY", "مجمع حلوان للحديد والصلب", FactoryType.STEEL_MILL, level = 3, efficiencyPercent = 82),
                    Factory("egy_f2", "EGY", "مجمع كفر الدوار للصناعات النسيجية والقطنية", FactoryType.FOOD_PROCESSING, level = 3, efficiencyPercent = 85),
                    Factory("egy_f3", "EGY", "مجمع أبو قير للأسمدة والصناعات الكيماوية", FactoryType.FERTILIZER_PLANT, level = 3, efficiencyPercent = 88)
                )
            )
        )

        // 8. IRAQ (IRQ)
        list.add(
            createCountry(
                id = "IRQ",
                iso2 = "IQ",
                nameAr = "جمهورية العراق",
                nameEn = "Iraq",
                capital = "بغداد",
                continent = Continent.MIDDLE_EAST,
                regionAr = "بلاد الرافدين والخليج",
                flag = "🇮🇶",
                currencyNameAr = "دينار عراقي",
                currencySymbol = "د.ع",
                populationMillions = 44.0,
                gdpBillions = 260.0,
                gdpGrowthPercent = 3.5,
                treasuryBillions = 30.0,
                sovereignDebtBillions = 90.0,
                inflationRate = 4.5,
                unemploymentRate = 14.0,
                stability = 62,
                archetype = CountryArchetype.RESOURCE_RICH,
                tier = EconomicTier.MEDIUM,
                mapX = 0.58f,
                mapY = 0.41f,
                primaryResources = listOf(ResourceType.OIL, ResourceType.NATURAL_GAS, ResourceType.WHEAT),
                factories = listOf(
                    Factory("irq_f1", "IRQ", "مصفاة بيجي والبصرة", FactoryType.OIL_REFINERY, level = 2, efficiencyPercent = 75)
                )
            )
        )

        // 9. JORDAN (JOR)
        list.add(
            createCountry(
                id = "JOR",
                iso2 = "JO",
                nameAr = "المملكة الأردنية الهاشمية",
                nameEn = "Jordan",
                capital = "عَمّان",
                continent = Continent.MIDDLE_EAST,
                regionAr = "بلاد الشام",
                flag = "🇯🇴",
                currencyNameAr = "دينار أردني",
                currencySymbol = "د.أ",
                populationMillions = 11.2,
                gdpBillions = 52.0,
                gdpGrowthPercent = 2.6,
                treasuryBillions = 3.8,
                sovereignDebtBillions = 45.0,
                inflationRate = 2.9,
                unemploymentRate = 18.0,
                stability = 78,
                archetype = CountryArchetype.DEVELOPING,
                tier = EconomicTier.SMALL,
                mapX = 0.56f,
                mapY = 0.425f,
                primaryResources = listOf(ResourceType.NATURAL_GAS, ResourceType.COPPER),
                factories = listOf(
                    Factory("jor_f1", "JOR", "مجمع الفوسفات والأسمدة بالعقبة", FactoryType.FERTILIZER_PLANT, level = 2, efficiencyPercent = 84)
                )
            )
        )

        // 10. ALGERIA (DZA)
        list.add(
            createCountry(
                id = "DZA",
                iso2 = "DZ",
                nameAr = "الجمهورية الجزائرية الديمقراطية الشعبية",
                nameEn = "Algeria",
                capital = "الجزائر",
                continent = Continent.AFRICA,
                regionAr = "شمال إفريقيا والمغرب العربي",
                flag = "🇩🇿",
                currencyNameAr = "دينار جزائري",
                currencySymbol = "د.ج",
                populationMillions = 45.0,
                gdpBillions = 220.0,
                gdpGrowthPercent = 3.4,
                treasuryBillions = 28.0,
                sovereignDebtBillions = 98.0,
                inflationRate = 7.5,
                unemploymentRate = 12.0,
                stability = 72,
                archetype = CountryArchetype.RESOURCE_RICH,
                tier = EconomicTier.MEDIUM,
                mapX = 0.47f,
                mapY = 0.41f,
                primaryResources = listOf(ResourceType.NATURAL_GAS, ResourceType.OIL, ResourceType.IRON_ORE),
                factories = listOf(
                    Factory("dza_f1", "DZA", "مجمع سوناطراك للغاز والبتروكيماويات", FactoryType.OIL_REFINERY, level = 3, efficiencyPercent = 88),
                    Factory("dza_f2", "DZA", "مجمع الحجار للحديد والصلب", FactoryType.STEEL_MILL, level = 2, efficiencyPercent = 78)
                )
            )
        )

        // 11. MOROCCO (MAR)
        list.add(
            createCountry(
                id = "MAR",
                iso2 = "MA",
                nameAr = "المملكة المغربية",
                nameEn = "Morocco",
                capital = "الرباط",
                continent = Continent.AFRICA,
                regionAr = "شمال إفريقيا والمغرب العربي",
                flag = "🇲🇦",
                currencyNameAr = "درهم مغربي",
                currencySymbol = "د.م",
                populationMillions = 37.5,
                gdpBillions = 150.0,
                gdpGrowthPercent = 3.6,
                treasuryBillions = 16.0,
                sovereignDebtBillions = 105.0,
                inflationRate = 4.2,
                unemploymentRate = 11.5,
                stability = 80,
                archetype = CountryArchetype.INDUSTRIAL,
                tier = EconomicTier.MEDIUM,
                mapX = 0.44f,
                mapY = 0.41f,
                primaryResources = listOf(ResourceType.NATURAL_GAS, ResourceType.COPPER, ResourceType.SILICON),
                factories = listOf(
                    Factory("mar_f1", "MAR", "مجمع المكتب الشريف للفوسفاط (OCP)", FactoryType.FERTILIZER_PLANT, level = 4, efficiencyPercent = 94),
                    Factory("mar_f2", "MAR", "مجمع طنجة لتجميع وتصنيع السيارات", FactoryType.AUTOMOTIVE_ASSEMBLY, level = 3, efficiencyPercent = 90)
                )
            )
        )

        // 12. UNITED STATES (USA)
        list.add(
            createCountry(
                id = "USA",
                iso2 = "US",
                nameAr = "الولايات المتحدة الأمريكية",
                nameEn = "United States",
                capital = "واشنطن",
                continent = Continent.AMERICAS,
                regionAr = "أمريكا الشمالية",
                flag = "🇺🇸",
                currencyNameAr = "دولار أمريكي",
                currencySymbol = "$",
                populationMillions = 340.0,
                gdpBillions = 27500.0,
                gdpGrowthPercent = 2.4,
                treasuryBillions = 480.0,
                sovereignDebtBillions = 33000.0,
                inflationRate = 3.1,
                unemploymentRate = 3.9,
                stability = 85,
                archetype = CountryArchetype.SUPERPOWER,
                tier = EconomicTier.GLOBAL_POWER,
                mapX = 0.22f,
                mapY = 0.36f,
                primaryResources = listOf(ResourceType.OIL, ResourceType.NATURAL_GAS, ResourceType.COAL, ResourceType.WHEAT, ResourceType.SILICON),
                factories = listOf(
                    Factory("usa_f1", "USA", "مجمع تكساس للرقائق والسيليكون", FactoryType.SEMICONDUCTOR_FAB, level = 5, efficiencyPercent = 96),
                    Factory("usa_f2", "USA", "مصانع نيفادا العملاقة للبطاريات", FactoryType.BATTERY_GIGAFACTORY, level = 5, efficiencyPercent = 94),
                    Factory("usa_f3", "USA", "مجمعات ديترويت للآليات الثقيلة والمركبات", FactoryType.AUTOMOTIVE_ASSEMBLY, level = 4, efficiencyPercent = 90)
                )
            )
        )

        // 13. CHINA (CHN)
        list.add(
            createCountry(
                id = "CHN",
                iso2 = "CN",
                nameAr = "جمهورية الصين الشعبية",
                nameEn = "China",
                capital = "بكين",
                continent = Continent.ASIA,
                regionAr = "شرق آسيا",
                flag = "🇨🇳",
                currencyNameAr = "يوان صيني",
                currencySymbol = "¥",
                populationMillions = 1410.0,
                gdpBillions = 18000.0,
                gdpGrowthPercent = 4.8,
                treasuryBillions = 650.0,
                sovereignDebtBillions = 14000.0,
                inflationRate = 1.2,
                unemploymentRate = 5.1,
                stability = 86,
                archetype = CountryArchetype.SUPERPOWER,
                tier = EconomicTier.GLOBAL_POWER,
                mapX = 0.78f,
                mapY = 0.38f,
                primaryResources = listOf(ResourceType.RARE_EARTHS, ResourceType.COAL, ResourceType.LITHIUM, ResourceType.IRON_ORE, ResourceType.SILICON),
                factories = listOf(
                    Factory("chn_f1", "CHN", "مجمع شنتشن للرقائق الإلكترونية", FactoryType.SEMICONDUCTOR_FAB, level = 5, efficiencyPercent = 95),
                    Factory("chn_f2", "CHN", "مصانع تشنغدو للبطاريات المتقدمة", FactoryType.BATTERY_GIGAFACTORY, level = 5, efficiencyPercent = 98),
                    Factory("chn_f3", "CHN", "مجمع باوستيل للحديد والصلب", FactoryType.STEEL_MILL, level = 5, efficiencyPercent = 94)
                )
            )
        )

        // 14. GERMANY (DEU)
        list.add(
            createCountry(
                id = "DEU",
                iso2 = "DE",
                nameAr = "جمهورية ألمانيا الاتحادية",
                nameEn = "Germany",
                capital = "برلين",
                continent = Continent.EUROPE,
                regionAr = "أوروبا الوسطى",
                flag = "🇩🇪",
                currencyNameAr = "يورو",
                currencySymbol = "€",
                populationMillions = 84.0,
                gdpBillions = 4400.0,
                gdpGrowthPercent = 0.8,
                treasuryBillions = 110.0,
                sovereignDebtBillions = 2800.0,
                inflationRate = 2.6,
                unemploymentRate = 5.7,
                stability = 88,
                archetype = CountryArchetype.INDUSTRIAL,
                tier = EconomicTier.MAJOR,
                mapX = 0.51f,
                mapY = 0.28f,
                primaryResources = listOf(ResourceType.COAL, ResourceType.SILICON, ResourceType.COPPER),
                factories = listOf(
                    Factory("deu_f1", "DEU", "مجمع فولفسبورغ للسيارات والآليات", FactoryType.AUTOMOTIVE_ASSEMBLY, level = 5, efficiencyPercent = 94),
                    Factory("deu_f2", "DEU", "مجمع دريسدن لأشباه الموصلات", FactoryType.SEMICONDUCTOR_FAB, level = 4, efficiencyPercent = 92)
                )
            )
        )

        // 15. UNITED KINGDOM (GBR)
        list.add(
            createCountry(
                id = "GBR",
                iso2 = "GB",
                nameAr = "المملكة المتحدة",
                nameEn = "United Kingdom",
                capital = "لندن",
                continent = Continent.EUROPE,
                regionAr = "أوروبا الغربية",
                flag = "🇬🇧",
                currencyNameAr = "جنيه إسترليني",
                currencySymbol = "£",
                populationMillions = 68.0,
                gdpBillions = 3300.0,
                gdpGrowthPercent = 1.4,
                treasuryBillions = 75.0,
                sovereignDebtBillions = 3100.0,
                inflationRate = 3.2,
                unemploymentRate = 4.2,
                stability = 84,
                archetype = CountryArchetype.TRADE_HUB,
                tier = EconomicTier.MAJOR,
                mapX = 0.47f,
                mapY = 0.27f,
                primaryResources = listOf(ResourceType.OIL, ResourceType.NATURAL_GAS),
                factories = listOf(
                    Factory("gbr_f1", "GBR", "مجمع شيفيلد للمنتجات المعدنية المتقدمة", FactoryType.STEEL_MILL, level = 3, efficiencyPercent = 88)
                )
            )
        )

        // 16. JAPAN (JPN)
        list.add(
            createCountry(
                id = "JPN",
                iso2 = "JP",
                nameAr = "اليابان",
                nameEn = "Japan",
                capital = "طوكيو",
                continent = Continent.ASIA,
                regionAr = "شرق آسيا",
                flag = "🇯🇵",
                currencyNameAr = "ين ياباني",
                currencySymbol = "¥",
                populationMillions = 124.0,
                gdpBillions = 4200.0,
                gdpGrowthPercent = 1.2,
                treasuryBillions = 140.0,
                sovereignDebtBillions = 9500.0,
                inflationRate = 2.5,
                unemploymentRate = 2.6,
                stability = 94,
                archetype = CountryArchetype.INDUSTRIAL,
                tier = EconomicTier.MAJOR,
                mapX = 0.88f,
                mapY = 0.37f,
                primaryResources = listOf(ResourceType.SILICON, ResourceType.RARE_EARTHS),
                factories = listOf(
                    Factory("jpn_f1", "JPN", "مسبك كيوشو للرقائق وأشباه الموصلات", FactoryType.SEMICONDUCTOR_FAB, level = 5, efficiencyPercent = 96),
                    Factory("jpn_f2", "JPN", "مجمع تويوتا للآليات والمركبات الذكية", FactoryType.AUTOMOTIVE_ASSEMBLY, level = 5, efficiencyPercent = 95)
                )
            )
        )

        // 17. INDIA (IND)
        list.add(
            createCountry(
                id = "IND",
                iso2 = "IN",
                nameAr = "جمهورية الهند",
                nameEn = "India",
                capital = "نيودلهي",
                continent = Continent.ASIA,
                regionAr = "جنوب آسيا",
                flag = "🇮🇳",
                currencyNameAr = "روبية هندية",
                currencySymbol = "₹",
                populationMillions = 1428.0,
                gdpBillions = 3700.0,
                gdpGrowthPercent = 6.8,
                treasuryBillions = 125.0,
                sovereignDebtBillions = 3100.0,
                inflationRate = 5.2,
                unemploymentRate = 7.1,
                stability = 78,
                archetype = CountryArchetype.EMERGING_GIANT,
                tier = EconomicTier.MAJOR,
                mapX = 0.71f,
                mapY = 0.44f,
                primaryResources = listOf(ResourceType.COAL, ResourceType.IRON_ORE, ResourceType.COTTON, ResourceType.WHEAT),
                factories = listOf(
                    Factory("ind_f1", "IND", "مجمع تاتا للصلب والحديد", FactoryType.STEEL_MILL, level = 4, efficiencyPercent = 90),
                    Factory("ind_f2", "IND", "مجمع بنغالور للمعدات التقنية", FactoryType.SEMICONDUCTOR_FAB, level = 3, efficiencyPercent = 85)
                )
            )
        )

        // 18. RUSSIA (RUS)
        list.add(
            createCountry(
                id = "RUS",
                iso2 = "RU",
                nameAr = "روسيا الاتحادية",
                nameEn = "Russia",
                capital = "موسكو",
                continent = Continent.EUROPE,
                regionAr = "أوراسيا",
                flag = "🇷🇺",
                currencyNameAr = "روبل روسي",
                currencySymbol = "₽",
                populationMillions = 144.0,
                gdpBillions = 2000.0,
                gdpGrowthPercent = 2.1,
                treasuryBillions = 140.0,
                sovereignDebtBillions = 380.0,
                inflationRate = 7.4,
                unemploymentRate = 3.0,
                stability = 75,
                archetype = CountryArchetype.RESOURCE_RICH,
                tier = EconomicTier.LARGE,
                mapX = 0.65f,
                mapY = 0.24f,
                primaryResources = listOf(ResourceType.OIL, ResourceType.NATURAL_GAS, ResourceType.URANIUM, ResourceType.WHEAT, ResourceType.GOLD),
                factories = listOf(
                    Factory("rus_f1", "RUS", "مجمع أورال للصلب والمعادن الثقيلة", FactoryType.STEEL_MILL, level = 4, efficiencyPercent = 88)
                )
            )
        )

        // 19. BRAZIL (BRA)
        list.add(
            createCountry(
                id = "BRA",
                iso2 = "BR",
                nameAr = "جمهورية البرازيل الاتحادية",
                nameEn = "Brazil",
                capital = "برازيليا",
                continent = Continent.AMERICAS,
                regionAr = "أمريكا الجنوبية",
                flag = "🇧🇷",
                currencyNameAr = "ريال برازيلي",
                currencySymbol = "R$",
                populationMillions = 215.0,
                gdpBillions = 2100.0,
                gdpGrowthPercent = 2.9,
                treasuryBillions = 75.0,
                sovereignDebtBillions = 1600.0,
                inflationRate = 4.1,
                unemploymentRate = 7.6,
                stability = 76,
                archetype = CountryArchetype.EMERGING_GIANT,
                tier = EconomicTier.LARGE,
                mapX = 0.35f,
                mapY = 0.68f,
                primaryResources = listOf(ResourceType.IRON_ORE, ResourceType.COFFEE, ResourceType.WHEAT, ResourceType.TIMBER),
                factories = listOf(
                    Factory("bra_f1", "BRA", "مجمع فالي لمعالجة خام الحديد", FactoryType.STEEL_MILL, level = 3, efficiencyPercent = 86)
                )
            )
        )

        // 20. TURKEY (TUR)
        list.add(
            createCountry(
                id = "TUR",
                iso2 = "TR",
                nameAr = "الجمهورية التركية",
                nameEn = "Turkey",
                capital = "أنقرة",
                continent = Continent.MIDDLE_EAST,
                regionAr = "الشرق الأوسط وأوراسيا",
                flag = "🇹🇷",
                currencyNameAr = "ليرة تركية",
                currencySymbol = "₺",
                populationMillions = 85.0,
                gdpBillions = 1100.0,
                gdpGrowthPercent = 4.1,
                treasuryBillions = 40.0,
                sovereignDebtBillions = 480.0,
                inflationRate = 45.0,
                unemploymentRate = 8.8,
                stability = 74,
                archetype = CountryArchetype.INDUSTRIAL,
                tier = EconomicTier.LARGE,
                mapX = 0.55f,
                mapY = 0.37f,
                primaryResources = listOf(ResourceType.IRON_ORE, ResourceType.COPPER, ResourceType.COTTON),
                factories = listOf(
                    Factory("tur_f1", "TUR", "مجمع بورصة للسيارات والصناعات الدفاعية", FactoryType.AUTOMOTIVE_ASSEMBLY, level = 3, efficiencyPercent = 89)
                )
            )
        )

        // Build mapping with ISO3 as primary key, plus legacy alias mapping (e.g. "SA" points to "SAU")
        val map = mutableMapOf<String, Country>()
        for (c in list) {
            map[c.id] = c
            // Legacy alias support
            c.definition?.let { def ->
                if (def.iso2.isNotEmpty() && def.iso2 != def.iso3) {
                    map[def.iso2] = c
                }
            }
        }
        return map
    }

    private fun createCountry(
        id: String,
        iso2: String,
        nameAr: String,
        nameEn: String,
        capital: String,
        continent: Continent,
        regionAr: String,
        flag: String,
        currencyNameAr: String,
        currencySymbol: String,
        populationMillions: Double,
        gdpBillions: Double,
        gdpGrowthPercent: Double,
        treasuryBillions: Double,
        sovereignDebtBillions: Double,
        inflationRate: Double,
        unemploymentRate: Double,
        stability: Int,
        archetype: CountryArchetype,
        tier: EconomicTier,
        mapX: Float,
        mapY: Float,
        taxProfile: TaxProfile = TaxProfile(),
        primaryResources: List<ResourceType> = emptyList(),
        factories: List<Factory> = emptyList()
    ): Country {
        val def = CountryDefinition(
            id = id,
            iso2 = iso2,
            iso3 = id,
            nameAr = nameAr,
            nameEn = nameEn,
            capital = capital,
            continent = continent,
            regionAr = regionAr,
            flag = flag,
            currencyNameAr = currencyNameAr,
            currencySymbol = currencySymbol,
            archetype = archetype,
            tier = tier,
            primaryResources = primaryResources,
            mapCenterX = mapX,
            mapCenterY = mapY
        )

        // Build resource map with realistic reserves and capacities
        val resMap = mutableMapOf<String, CountryResource>()
        for (r in primaryResources) {
            val baseReserve = when (r) {
                ResourceType.OIL -> if (id in listOf("SAU", "IRQ", "KWT", "ARE", "RUS", "USA")) 260_000.0 else 12_000.0
                ResourceType.NATURAL_GAS -> if (id in listOf("QAT", "RUS", "IRN", "USA", "DZA")) 180_000.0 else 8_000.0
                ResourceType.GOLD -> 450.0
                ResourceType.RARE_EARTHS -> if (id == "CHN") 44_000.0 else 5_000.0
                ResourceType.LITHIUM -> 18_000.0
                ResourceType.IRON_ORE -> 60_000.0
                ResourceType.WHEAT -> 35_000.0
                else -> 10_000.0
            }
            val baseCapacity = (baseReserve * 0.003).coerceAtLeast(10.0)
            resMap[r.name] = CountryResource(
                resourceType = r,
                reservesUnits = baseReserve,
                monthlyCapacityUnits = baseCapacity,
                currentProductionUnits = baseCapacity * 0.85,
                extractionCostPerUnit = r.defaultBasePrice * 0.35,
                governmentSharePercent = if (id in listOf("SAU", "QAT", "ARE", "KWT", "DZA", "EGY")) 0.70 else 0.40
            )
        }

        val cState = CountryState(
            countryId = id,
            treasuryBillions = treasuryBillions,
            gdpBillions = gdpBillions,
            gdpGrowthPercent = gdpGrowthPercent,
            populationMillions = populationMillions,
            inflationRate = inflationRate,
            unemploymentRate = unemploymentRate,
            sovereignDebtBillions = sovereignDebtBillions,
            taxProfile = taxProfile,
            stability = stability,
            resources = resMap,
            factories = factories
        )

        return Country(
            id = id,
            nameAr = nameAr,
            nameEn = nameEn,
            capital = capital,
            flag = flag,
            populationMillions = populationMillions,
            gdpBillions = gdpBillions,
            gdpGrowthPercent = gdpGrowthPercent,
            treasuryBillions = treasuryBillions,
            sovereignDebtBillions = sovereignDebtBillions,
            inflationRate = inflationRate,
            unemploymentRate = unemploymentRate,
            taxRate = taxProfile.effectiveTaxRate,
            stability = stability,
            mainResources = primaryResources.map { it.nameAr },
            aiArchetype = archetype,
            mapX = mapX,
            mapY = mapY,
            iso3 = id,
            continent = continent,
            tier = tier,
            taxProfile = taxProfile,
            resources = resMap,
            factories = factories,
            definition = def,
            countryState = cState
        )
    }

    fun getInitialMarketCommodities(): Map<String, MarketCommodity> {
        val map = mutableMapOf<String, MarketCommodity>()
        for (type in ResourceType.values()) {
            map[type.name] = MarketCommodity(
                resourceType = type,
                currentPrice = type.defaultBasePrice,
                basePrice = type.defaultBasePrice,
                priceHistory = listOf(
                    type.defaultBasePrice * 0.96,
                    type.defaultBasePrice * 0.98,
                    type.defaultBasePrice * 1.01,
                    type.defaultBasePrice
                )
            )
        }
        return map
    }

    fun getInitialCompanies(playerCountryId: String): List<Company> {
        val list = mutableListOf<Company>()
        when (playerCountryId) {
            "SAU", "SA" -> {
                list.add(Company("sau_1", "أرامكو السعودية للطاقة", "الطاقة والنفط", "SAU", 2100.0, 120000.0, 0.90, 70000, true, "🛢️"))
                list.add(Company("sau_2", "سابك للصناعات البتروكيماوية", "البتروكيماويات", "SAU", 85.0, 6200.0, 0.70, 31000, true, "🧪"))
                list.add(Company("sau_3", "مجموعة الاتصالات السعودية (stc)", "الاتصالات والتقنية", "SAU", 65.0, 3400.0, 0.64, 25000, true, "📡"))
                list.add(Company("sau_4", "شركة معادن الوطنية", "التعدين والموارد", "SAU", 42.0, 2100.0, 0.67, 18000, true, "⛏️"))
            }
            "EGY", "EG" -> {
                list.add(Company("egy_1", "هيئة قناة السويس اللوجستية", "الملاحة واللوجستيات", "EGY", 90.0, 9500.0, 1.0, 35000, true, "🚢"))
                list.add(Company("egy_2", "المصرية للاتصالات (وي)", "الاتصالات", "EGY", 18.0, 1200.0, 0.80, 40000, true, "📡"))
                list.add(Company("egy_3", "مصر للأسمدة والكيماويات", "الصناعة", "EGY", 12.0, 850.0, 0.51, 15000, true, "🏭"))
            }
            "ARE", "AE" -> {
                list.add(Company("are_1", "موانئ دبي العالمية (DP World)", "الموانئ والتجارة العالمية", "ARE", 60.0, 4800.0, 0.80, 50000, true, "🚢"))
                list.add(Company("are_2", "مجموعة إي آند للتكنولوجيا (e&)", "الاتصالات والذكاء الاصطناعي", "ARE", 55.0, 3900.0, 0.60, 45000, true, "🤖"))
                list.add(Company("are_3", "أدنوك للتوزيع والطاقة", "الطاقة", "ARE", 48.0, 3200.0, 0.75, 28000, true, "⚡"))
            }
            else -> {
                list.add(Company("gen_1", "الشركة الوطنية القابضة للاستثمار", "الاستثمار والمالية", playerCountryId, 50.0, 3500.0, 0.65, 20000, true, "🏛️"))
                list.add(Company("gen_2", "الشبكة الوطنية للطاقة والمرافق", "الطاقة", playerCountryId, 40.0, 2800.0, 0.70, 18000, true, "⚡"))
                list.add(Company("gen_3", "مجمع التصنيع الوطني المتقدم", "الصناعة", playerCountryId, 30.0, 1900.0, 0.51, 15000, true, "🏭"))
            }
        }
        return list
    }

    fun getInitialTradeContracts(playerCountryId: String): List<TradeContract> {
        val list = mutableListOf<TradeContract>()
        when (playerCountryId) {
            "SAU", "SA" -> {
                list.add(
                    TradeContract(
                        id = "tc_sau_chn_1",
                        buyerCountryId = "CHN",
                        sellerCountryId = "SAU",
                        resourceType = ResourceType.OIL,
                        monthlyQuantity = 120.0,
                        agreedUnitPrice = 76.0,
                        durationMonths = 24,
                        remainingMonths = 20
                    )
                )
                list.add(
                    TradeContract(
                        id = "tc_sau_ind_1",
                        buyerCountryId = "IND",
                        sellerCountryId = "SAU",
                        resourceType = ResourceType.NATURAL_GAS,
                        monthlyQuantity = 80.0,
                        agreedUnitPrice = 3.3,
                        durationMonths = 12,
                        remainingMonths = 11
                    )
                )
            }
            "EGY", "EG" -> {
                list.add(
                    TradeContract(
                        id = "tc_egy_sau_1",
                        buyerCountryId = "EGY",
                        sellerCountryId = "SAU",
                        resourceType = ResourceType.OIL,
                        monthlyQuantity = 40.0,
                        agreedUnitPrice = 74.0,
                        durationMonths = 12,
                        remainingMonths = 8
                    )
                )
            }
            else -> {}
        }
        return list
    }

    fun getInitialInfluenceNetwork(playerCountryId: String, allCountryIds: List<String>): List<InfluenceEntry> {
        val entries = mutableListOf<InfluenceEntry>()
        for (target in allCountryIds) {
            if (target != playerCountryId) {
                entries.add(
                    InfluenceEntry(
                        sourceCountryId = playerCountryId,
                        targetCountryId = target,
                        economicInfluence = (18..35).random(),
                        commercialInfluence = (15..32).random(),
                        diplomaticInfluence = (20..42).random(),
                        strategicInfluence = (12..28).random()
                    )
                )
                entries.add(
                    InfluenceEntry(
                        sourceCountryId = "USA",
                        targetCountryId = target,
                        economicInfluence = 48,
                        commercialInfluence = 40,
                        diplomaticInfluence = 52,
                        strategicInfluence = 50
                    )
                )
                entries.add(
                    InfluenceEntry(
                        sourceCountryId = "CHN",
                        targetCountryId = target,
                        economicInfluence = 50,
                        commercialInfluence = 48,
                        diplomaticInfluence = 38,
                        strategicInfluence = 32
                    )
                )
            }
        }
        return entries
    }
}
