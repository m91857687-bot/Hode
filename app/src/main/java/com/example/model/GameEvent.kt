package com.example.model

data class EventChoice(
    val id: String,
    val titleAr: String,
    val descriptionAr: String,
    val costBillions: Double = 0.0,
    val treasuryBonusBillions: Double = 0.0,
    val gdpChangePercent: Double = 0.0,
    val stabilityChange: Int = 0,
    val inflationChange: Double = 0.0
)

data class GameEvent(
    val id: String,
    val titleAr: String,
    val descriptionAr: String,
    val icon: String,
    val categoryText: String,
    val choices: List<EventChoice>,
    val timestampMonthYear: String = ""
)
