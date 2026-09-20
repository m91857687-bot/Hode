package com.example.model

enum class GameSpeed(val multiplier: Long, val label: String) {
    PAUSED(0, "إيقاف ⏸"),
    SPEED_1X(3000L, "1x ▶"),
    SPEED_2X(1500L, "2x ⏩"),
    SPEED_5X(600L, "5x ⏭"),
    SPEED_10X(250L, "10x ⚡")
}

enum class NavigationTab(val titleAr: String, val icon: String) {
    MAP("العالم", "🌍"),
    GOVERNMENT("الحكومة", "🏛️"),
    ECONOMY("الاقتصاد", "💰"),
    INDUSTRY("الصناعة", "🏭"),
    RESEARCH("الأبحاث", "🔬"),
    INVESTMENTS("الاستثمار", "📈"),
    MILITARY("الدفاع", "🪖"),
    DIPLOMACY_TRADE("العلاقات", "🤝"),
    STATS("الإحصائيات", "📊"),

    // Compatibility aliases
    STATE("الحكومة", "🏛️"),
    COMPANIES("الشركات", "🏢"),
    INFLUENCE("النفوذ", "🌐")
}

data class GameState(
    val playerCountryId: String? = null,
    val playerGems: Int = 120,
    val gameYear: Int = 2026,
    val gameMonth: Int = 1,
    val gameSpeed: GameSpeed = GameSpeed.PAUSED,
    val countries: Map<String, Country> = emptyMap(),
    val domesticProjects: List<Project> = emptyList(),
    val foreignInvestments: List<Project> = emptyList(), // Projects player funded in foreign nations
    val outboundInvestments: List<OutboundInvestment> = emptyList(), // Concessions and corporate stakes
    val foreignOffers: List<ForeignOffer> = emptyList(),
    val companies: List<Company> = emptyList(),
    val tradeContracts: List<TradeContract> = emptyList(),
    val marketCommodities: Map<String, MarketCommodity> = emptyMap(),
    val resourceInventory: Map<String, Double> = emptyMap(), // Resource code to quantity in thousands of units
    val energyGrid: EnergyGridState? = null,
    val waterGrid: WaterGridState? = null,
    val governmentServices: GovernmentServicesState? = null,
    val laborMarket: LaborMarketState? = null,
    val researchState: ResearchState? = null,
    val militaryState: MilitaryState? = null,
    val monthlyReport: MonthlyReport? = null,
    val activePolicyPreview: PolicyImpactPreview? = null,
    val isInventoryDialogOpen: Boolean = false,
    val isMonthlyReportOpen: Boolean = false,
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
    val saveVersion: Int = 3,
    val lastSaveTimeMillis: Long = System.currentTimeMillis()
) {
    val playerCountry: Country?
        get() = playerCountryId?.let { countries[it] }

    val selectedCountryId: String?
        get() = selectedCountryIdForDossier

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
    ECONOMIC_TIER("الوضع الاقتصادي"),
    RESOURCES("الموارد"),
    STABILITY("الاستقرار"),
    PLAYER_INFLUENCE("نفوذ دولتك"),
    RELATIONS("العلاقات الدولية");

    val labelAr: String get() = titleAr
}
