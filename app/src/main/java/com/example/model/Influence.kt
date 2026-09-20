package com.example.model

data class InfluenceEntry(
    val sourceCountryId: String,
    val targetCountryId: String,
    val economicInfluence: Int,
    val commercialInfluence: Int, // Also trade influence
    val diplomaticInfluence: Int,
    val strategicInfluence: Int,
    val culturalInfluence: Int = 10
) {
    val totalScore: Int
        get() = economicInfluence + commercialInfluence + diplomaticInfluence + strategicInfluence + culturalInfluence

    val softPowerScore: Int
        get() = ((culturalInfluence * 0.5) + (diplomaticInfluence * 0.5)).toInt()
}

data class InfluenceHistoryPoint(
    val year: Int,
    val month: Int,
    val totalScore: Int,
    val economicPart: Int,
    val tradePart: Int,
    val diplomaticPart: Int,
    val strategicPart: Int,
    val culturalPart: Int
)

data class InfluenceBreakdown(
    val countryId: String,
    val entries: List<InfluenceEntry>
)
