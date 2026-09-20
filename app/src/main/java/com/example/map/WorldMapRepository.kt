package com.example.map

object WorldMapRepository {

    private val cachedPolygons: MutableList<CountryPolygon> = mutableListOf()

    fun getAllCountryPolygons(): List<CountryPolygon> {
        if (cachedPolygons.isEmpty()) {
            cachedPolygons.addAll(loadPolygons())
        }
        return cachedPolygons
    }

    fun getAllPolygons(): List<CountryPolygon> = getAllCountryPolygons()

    private fun loadPolygons(): List<CountryPolygon> {
        val list = mutableListOf<CountryPolygon>()

        // 1. SAUDI ARABIA (SAU)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "SAU",
                nameAr = "المملكة العربية السعودية",
                rawRingsLonLat = listOf(
                    listOf(
                        36.5 to 29.3, 39.2 to 32.1, 42.4 to 31.0, 46.5 to 29.1,
                        48.5 to 30.0, 50.1 to 26.3, 51.5 to 24.2, 55.2 to 22.8,
                        55.7 to 19.5, 52.0 to 19.0, 47.0 to 17.0, 43.0 to 16.4,
                        42.7 to 17.5, 41.0 to 19.5, 39.5 to 22.5, 38.0 to 25.0,
                        35.0 to 28.0, 36.5 to 29.3
                    )
                )
            )
        )

        // 2. YEMEN (YEM)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "YEM",
                nameAr = "اليمن",
                rawRingsLonLat = listOf(
                    listOf(
                        42.6 to 16.5, 43.5 to 17.2, 47.0 to 17.0, 52.0 to 19.0,
                        53.1 to 16.6, 51.5 to 15.2, 48.5 to 14.0, 45.0 to 12.6,
                        43.4 to 12.7, 42.8 to 14.5, 42.6 to 16.5
                    )
                )
            )
        )

        // 3. OMAN (OMN)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "OMN",
                nameAr = "سلطنة عُمان",
                rawRingsLonLat = listOf(
                    listOf(
                        52.0 to 19.0, 55.7 to 19.5, 56.4 to 22.5, 58.7 to 23.6,
                        59.8 to 22.5, 57.5 to 19.8, 54.0 to 17.0, 52.0 to 19.0
                    )
                )
            )
        )

        // 4. UAE (ARE)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "ARE",
                nameAr = "الإمارات العربية المتحدة",
                rawRingsLonLat = listOf(
                    listOf(
                        51.6 to 24.2, 53.0 to 24.0, 55.2 to 22.8, 56.0 to 24.5,
                        56.3 to 25.8, 55.0 to 25.0, 53.5 to 24.5, 51.6 to 24.2
                    )
                )
            )
        )

        // 5. QATAR (QAT)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "QAT",
                nameAr = "قطر",
                rawRingsLonLat = listOf(
                    listOf(
                        50.8 to 24.8, 51.3 to 24.6, 51.6 to 25.5, 51.4 to 26.2,
                        50.8 to 25.8, 50.8 to 24.8
                    )
                )
            )
        )

        // 6. KUWAIT (KWT)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "KWT",
                nameAr = "الكويت",
                rawRingsLonLat = listOf(
                    listOf(
                        46.6 to 29.1, 48.0 to 28.5, 48.4 to 29.5, 48.0 to 30.1,
                        46.9 to 30.0, 46.6 to 29.1
                    )
                )
            )
        )

        // 7. IRAQ (IRQ)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "IRQ",
                nameAr = "العراق",
                rawRingsLonLat = listOf(
                    listOf(
                        38.8 to 33.3, 42.4 to 37.3, 44.5 to 37.1, 46.2 to 35.5,
                        48.5 to 30.0, 47.0 to 30.0, 44.0 to 29.1, 41.5 to 31.0,
                        38.8 to 33.3
                    )
                )
            )
        )

        // 8. JORDAN (JOR)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "JOR",
                nameAr = "الأردن",
                rawRingsLonLat = listOf(
                    listOf(
                        35.0 to 29.5, 35.6 to 31.0, 35.6 to 32.5, 37.0 to 32.5,
                        39.2 to 32.1, 37.0 to 30.5, 36.0 to 29.3, 35.0 to 29.5
                    )
                )
            )
        )

        // 9. SYRIA (SYR)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "SYR",
                nameAr = "سوريا",
                rawRingsLonLat = listOf(
                    listOf(
                        35.7 to 35.9, 36.6 to 36.8, 42.4 to 37.3, 42.0 to 35.0,
                        38.8 to 33.3, 36.8 to 32.3, 36.0 to 33.5, 35.7 to 35.9
                    )
                )
            )
        )

        // 10. LEBANON (LBN)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "LBN",
                nameAr = "لبنان",
                rawRingsLonLat = listOf(
                    listOf(
                        35.1 to 33.1, 35.3 to 33.9, 36.0 to 34.6, 36.4 to 34.4,
                        35.8 to 33.3, 35.1 to 33.1
                    )
                )
            )
        )

        // 11. PALESTINE (PSE)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "PSE",
                nameAr = "فلسطين",
                rawRingsLonLat = listOf(
                    listOf(
                        34.3 to 31.3, 34.5 to 31.6, 35.5 to 32.5, 35.5 to 31.8,
                        35.0 to 31.3, 34.3 to 31.3
                    )
                )
            )
        )

        // 12. EGYPT (EGY)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "EGY",
                nameAr = "مصر",
                rawRingsLonLat = listOf(
                    listOf(
                        25.0 to 31.5, 31.5 to 31.5, 34.3 to 31.3, 34.9 to 29.5,
                        34.0 to 27.5, 35.5 to 24.0, 36.9 to 22.0, 25.0 to 22.0,
                        25.0 to 31.5
                    )
                )
            )
        )

        // 13. SUDAN (SDN)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "SDN",
                nameAr = "السودان",
                rawRingsLonLat = listOf(
                    listOf(
                        22.0 to 20.0, 25.0 to 22.0, 36.9 to 22.0, 38.5 to 18.0,
                        36.0 to 14.5, 34.0 to 10.0, 29.0 to 10.0, 24.0 to 10.0,
                        22.0 to 14.0, 22.0 to 20.0
                    )
                )
            )
        )

        // 14. LIBYA (LBY)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "LBY",
                nameAr = "ليبيا",
                rawRingsLonLat = listOf(
                    listOf(
                        11.5 to 33.1, 15.0 to 31.0, 20.0 to 32.2, 25.0 to 31.5,
                        25.0 to 22.0, 24.0 to 19.5, 15.0 to 20.0, 10.0 to 23.5,
                        10.0 to 30.2, 11.5 to 33.1
                    )
                )
            )
        )

        // 15. TUNISIA (TUN)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "TUN",
                nameAr = "تونس",
                rawRingsLonLat = listOf(
                    listOf(
                        8.5 to 36.9, 11.0 to 37.0, 11.0 to 35.0, 11.5 to 33.1,
                        10.0 to 30.2, 8.5 to 32.0, 8.2 to 34.5, 8.5 to 36.9
                    )
                )
            )
        )

        // 16. ALGERIA (DZA)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "DZA",
                nameAr = "الجزائر",
                rawRingsLonLat = listOf(
                    listOf(
                        -2.0 to 35.1, 4.0 to 36.9, 8.5 to 36.9, 8.5 to 32.0,
                        10.0 to 23.5, 5.0 to 19.0, 0.0 to 21.0, -5.0 to 26.0,
                        -8.5 to 28.5, -2.0 to 35.1
                    )
                )
            )
        )

        // 17. MOROCCO (MAR)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "MAR",
                nameAr = "المملكة المغربية",
                rawRingsLonLat = listOf(
                    listOf(
                        -6.0 to 35.8, -2.0 to 35.1, -2.0 to 32.0, -5.0 to 29.0,
                        -12.0 to 27.5, -10.0 to 30.0, -9.0 to 32.0, -6.0 to 35.8
                    )
                )
            )
        )

        // 18. TURKEY (TUR)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "TUR",
                nameAr = "تركيا",
                rawRingsLonLat = listOf(
                    listOf(
                        26.0 to 41.5, 30.0 to 42.0, 36.0 to 42.0, 42.0 to 41.5,
                        44.5 to 39.5, 42.4 to 37.3, 36.0 to 36.0, 30.0 to 36.5,
                        26.0 to 38.5, 26.0 to 41.5
                    )
                )
            )
        )

        // 19. IRAN (IRN)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "IRN",
                nameAr = "إيران",
                rawRingsLonLat = listOf(
                    listOf(
                        44.5 to 39.5, 48.0 to 38.5, 54.0 to 37.5, 61.0 to 36.0,
                        61.5 to 31.0, 62.0 to 25.0, 56.5 to 26.5, 50.0 to 30.0,
                        46.2 to 35.5, 44.5 to 39.5
                    )
                )
            )
        )

        // 20. CHINA (CHN)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "CHN",
                nameAr = "الصين",
                rawRingsLonLat = listOf(
                    listOf(
                        74.0 to 39.0, 80.0 to 45.0, 87.0 to 49.0, 110.0 to 42.0,
                        120.0 to 50.0, 131.0 to 45.0, 122.0 to 39.0, 122.0 to 30.0,
                        117.0 to 23.5, 108.0 to 21.5, 101.0 to 21.0, 97.0 to 27.0,
                        88.0 to 28.0, 78.0 to 35.0, 74.0 to 39.0
                    )
                )
            )
        )

        // 21. INDIA (IND)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "IND",
                nameAr = "الهند",
                rawRingsLonLat = listOf(
                    listOf(
                        74.0 to 36.0, 78.0 to 35.0, 88.0 to 27.5, 96.0 to 28.0,
                        92.0 to 21.0, 85.0 to 19.0, 80.0 to 12.0, 77.5 to 8.2,
                        73.0 to 15.0, 70.0 to 22.0, 68.5 to 24.0, 72.0 to 31.0,
                        74.0 to 36.0
                    )
                )
            )
        )

        // 22. UNITED STATES (USA)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "USA",
                nameAr = "الولايات المتحدة",
                rawRingsLonLat = listOf(
                    listOf(
                        -124.5 to 49.0, -95.0 to 49.0, -82.0 to 45.0, -67.0 to 45.0,
                        -71.0 to 42.0, -75.0 to 35.0, -80.0 to 25.0, -85.0 to 30.0,
                        -97.0 to 26.0, -106.0 to 31.5, -117.0 to 32.5, -124.0 to 40.0,
                        -124.5 to 49.0
                    )
                )
            )
        )

        // 23. RUSSIA (RUS)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "RUS",
                nameAr = "روسيا",
                rawRingsLonLat = listOf(
                    listOf(
                        28.0 to 59.0, 40.0 to 67.0, 60.0 to 73.0, 100.0 to 75.0,
                        140.0 to 72.0, 175.0 to 65.0, 160.0 to 55.0, 135.0 to 45.0,
                        110.0 to 50.0, 85.0 to 54.0, 60.0 to 55.0, 38.0 to 50.0,
                        30.0 to 55.0, 28.0 to 59.0
                    )
                )
            )
        )

        // 24. GERMANY (DEU)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "DEU",
                nameAr = "ألمانيا",
                rawRingsLonLat = listOf(
                    listOf(
                        6.0 to 51.0, 8.5 to 54.5, 14.0 to 54.0, 14.8 to 51.0,
                        13.0 to 48.0, 9.5 to 47.5, 7.5 to 49.0, 6.0 to 51.0
                    )
                )
            )
        )

        // 25. UNITED KINGDOM (GBR)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "GBR",
                nameAr = "المملكة المتحدة",
                rawRingsLonLat = listOf(
                    listOf(
                        -5.0 to 50.0, 1.5 to 51.0, 0.0 to 54.0, -2.0 to 58.0,
                        -5.0 to 58.5, -5.0 to 55.0, -3.5 to 52.0, -5.0 to 50.0
                    )
                )
            )
        )

        // 26. FRANCE (FRA)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "FRA",
                nameAr = "فرنسا",
                rawRingsLonLat = listOf(
                    listOf(
                        -4.5 to 48.5, 1.5 to 50.5, 7.5 to 49.0, 7.0 to 44.0,
                        3.0 to 42.5, -1.5 to 43.5, -1.0 to 47.0, -4.5 to 48.5
                    )
                )
            )
        )

        // 27. JAPAN (JPN)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "JPN",
                nameAr = "اليابان",
                rawRingsLonLat = listOf(
                    listOf(
                        130.0 to 33.0, 135.0 to 34.5, 140.0 to 36.0, 141.0 to 41.0,
                        144.0 to 44.0, 141.0 to 45.0, 137.0 to 38.0, 131.0 to 34.0,
                        130.0 to 33.0
                    )
                )
            )
        )

        // 28. BRAZIL (BRA)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "BRA",
                nameAr = "البرازيل",
                rawRingsLonLat = listOf(
                    listOf(
                        -70.0 to -4.0, -60.0 to 4.0, -50.0 to 1.0, -35.0 to -6.0,
                        -37.0 to -15.0, -42.0 to -22.0, -50.0 to -30.0, -58.0 to -22.0,
                        -60.0 to -12.0, -70.0 to -10.0, -70.0 to -4.0
                    )
                )
            )
        )

        // 29. AUSTRALIA (AUS)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "AUS",
                nameAr = "أستراليا",
                rawRingsLonLat = listOf(
                    listOf(
                        114.0 to -22.0, 125.0 to -15.0, 138.0 to -16.0, 145.0 to -12.0,
                        153.0 to -28.0, 150.0 to -37.0, 140.0 to -38.0, 128.0 to -32.0,
                        115.0 to -34.0, 114.0 to -22.0
                    )
                )
            )
        )

        // 30. SOUTH AFRICA (ZAF)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "ZAF",
                nameAr = "جنوب إفريقيا",
                rawRingsLonLat = listOf(
                    listOf(
                        17.0 to -29.0, 25.0 to -26.0, 31.0 to -23.0, 32.5 to -27.0,
                        29.0 to -32.0, 24.0 to -34.0, 18.5 to -34.5, 17.0 to -29.0
                    )
                )
            )
        )

        // 31. NIGERIA (NGA)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "NGA",
                nameAr = "نيجيريا",
                rawRingsLonLat = listOf(
                    listOf(
                        3.0 to 6.5, 4.0 to 13.5, 13.5 to 13.0, 14.0 to 10.0,
                        9.0 to 4.5, 6.0 to 4.5, 3.0 to 6.5
                    )
                )
            )
        )

        // 32. CANADA (CAN)
        list.add(
            CountryPolygon.fromCoordinates(
                iso3 = "CAN",
                nameAr = "كندا",
                rawRingsLonLat = listOf(
                    listOf(
                        -135.0 to 60.0, -95.0 to 60.0, -65.0 to 55.0, -55.0 to 48.0,
                        -67.0 to 45.0, -82.0 to 45.0, -95.0 to 49.0, -124.5 to 49.0,
                        -135.0 to 60.0
                    )
                )
            )
        )

        return list
    }
}
