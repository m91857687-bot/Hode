package com.example.model

enum class GameSpeed(val multiplier: Long, val label: String) {
    PAUSED(0, "إيقاف ⏸"),
    SPEED_1X(3000L, "1x ▶"),
    SPEED_2X(1500L, "2x ⏩"),
    SPEED_5X(600L, "5x ⏭")
}

enum class NavigationTab(val titleAr: String, val icon: String) {
    MAP("العالم", "🌍"),
    STATE("الدولة", "🏛️"),
    ECONOMY("الاقتصاد", "💰"),
    INVESTMENTS("الاستثمار", "📈"),
    COMPANIES("الشركات", "🏢"),
    INFLUENCE("النفوذ", "🌐")
}

data class GameState(
    val playerCountryId: String? = null,
    val gameYear: Int = 2026,
    val gameMonth: Int = 1,
    val gameSpeed: GameSpeed = GameSpeed.PAUSED,
    val countries: Map<String, Country> = emptyMap(),
    val domesticProjects: List<Project> = emptyList(),
    val foreignInvestments: List<Project> = emptyList(), // Projects player funded in foreign nations
    val outboundInvestments: List<OutboundInvestment> = emptyList(), // Concessions and corporate stakes
    val foreignOffers: List<ForeignOffer> = emptyList(),
    val companies: List<Company> = emptyList(),
    val influenceNetwork: List<InfluenceEntry> = emptyList(),
    val influenceHistory: List<InfluenceHistoryPoint> = emptyList(),
    val gdpHistory: List<Double> = emptyList(),
    val treasuryHistory: List<Double> = emptyList(),
    val inflationHistory: List<Double> = emptyList(),
    val eventHistory: List<GameEvent> = emptyList(),
    val activePendingEvent: GameEvent? = null,
    val activeNegotiationOffer: ForeignOffer? = null,
    val selectedCountryIdForDossier: String? = null,
    val currentTab: NavigationTab = NavigationTab.MAP,
    val mapFilterMode: MapFilterMode = MapFilterMode.POLITICAL,
    val lastSaveTimeMillis: Long = System.currentTimeMillis()
) {
    val playerCountry: Country?
        get() = playerCountryId?.let { countries[it] }

    val dateFormatted: String
        get() {
            val months = listOf(
                "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
                "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
            )
            val mName = months.getOrElse(gameMonth - 1) { "شهر $gameMonth" }
            return "$mName $gameYear"
        }

    val totalOutboundValuation: Double
        get() = outboundInvestments.filter { it.status == OutboundStatus.ACTIVE }.sumOf { it.investedCapitalBillions }

    val monthlyOutboundDividends: Double
        get() = outboundInvestments.filter { it.status == OutboundStatus.ACTIVE }.sumOf { it.monthlyDividendsBillions }
}

enum class MapFilterMode(val titleAr: String) {
    POLITICAL("سياسي"),
    GDP("الناتج المحلي"),
    PLAYER_INFLUENCE("نفوذ دولتك"),
    RELATIONS("العلاقات الدولية")
}
