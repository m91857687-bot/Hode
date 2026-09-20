package com.example.model

enum class CountryArchetype(val titleAr: String) {
    SUPERPOWER("قوة عظمى عالمية"),
    INDUSTRIAL("عملاق صناعي وتصديري"),
    RESOURCE_RICH("قوة طاقة وموارد طبيعية"),
    TRADE_HUB("مركز تجاري ولوجستي عالمي"),
    EMERGING_GIANT("اقتصاد صاعد واعد"),
    DEVELOPING("اقتصاد نامٍ وسياحي")
}

enum class DiplomaticRelationStatus(val titleAr: String, val colorHex: Long) {
    FRIENDLY("حليف استراتيجي", 0xFF00E676),
    COOPERATIVE("تعاون وثيق", 0xFF00E5FF),
    NEUTRAL("علاقات متوازنة", 0xFFFFD700),
    TENSE("توتر وحذر", 0xFFFF9100),
    HOSTILE("خصومة ونزاع", 0xFFFF1744)
}

data class Country(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val capital: String,
    val flag: String,
    val populationMillions: Double,
    val gdpBillions: Double,
    val gdpGrowthPercent: Double,
    val treasuryBillions: Double,
    val sovereignDebtBillions: Double,
    val inflationRate: Double,
    val unemploymentRate: Double,
    val taxRate: Double = 0.20,
    val stability: Int = 75,
    
    // Core sector indexes (0 - 100)
    val industryIndex: Int = 60,
    val agricultureIndex: Int = 50,
    val energyIndex: Int = 65,
    val infrastructureIndex: Int = 70,
    val educationIndex: Int = 72,
    val healthIndex: Int = 70,
    val militaryIndex: Int = 65,
    
    // Monthly financials (Billions USD)
    val monthlyRevenue: Double = 0.0,
    val monthlyExpenses: Double = 0.0,
    
    // Production outputs (0 - 100 volume / performance index)
    val industrialProduction: Int = 60,
    val agriculturalProduction: Int = 50,
    val energyProduction: Int = 65,
    val tradePower: Double = 50.0,
    
    // 5 Distinct Pillars of Influence
    val economicInfluence: Int = 50,
    val tradeInfluence: Int = 50,
    val diplomaticInfluence: Int = 50,
    val strategicInfluence: Int = 50,
    val culturalInfluence: Int = 45,
    
    val tradeSurplusBillions: Double = 12.0,
    val mainResources: List<String> = listOf("طاقة", "صناعة"),
    val aiArchetype: CountryArchetype = CountryArchetype.INDUSTRIAL,
    val mapX: Float = 0.5f,
    val mapY: Float = 0.5f,
    val relationsWithPlayer: Int = 50, // 0 (hostile) to 100 (allied)
    val iso3: String = id,
    val continent: Continent = Continent.ASIA,
    val tier: EconomicTier = EconomicTier.MEDIUM,
    val taxProfile: TaxProfile = TaxProfile(corporateTaxRate = taxRate),
    val resources: Map<String, CountryResource> = emptyMap(),
    val factories: List<Factory> = emptyList(),
    val definition: CountryDefinition? = null,
    val countryState: CountryState? = null
) {
    // Aliases for compatibility
    val infrastructure: Int get() = infrastructureIndex
    val education: Int get() = educationIndex
    val healthcare: Int get() = healthIndex
    val militaryStrength: Int get() = militaryIndex
    val foreignReservesBillions: Double get() = treasuryBillions * 0.85 + (gdpBillions * 0.05)
    val industryProductionScore: Int get() = industrialProduction
    val activeFactories: List<Factory> get() = factories.filter { !it.isPaused }

    val nationalPowerScore: Int
        get() {
            val econPart = (gdpBillions / 400.0).coerceIn(5.0, 35.0)
            val stabPart = stability * 0.12
            val infraPart = infrastructureIndex * 0.12
            val indPart = industrialProduction * 0.12
            val eduPart = educationIndex * 0.09
            val milPart = militaryIndex * 0.10
            val infPart = (economicInfluence + strategicInfluence + tradeInfluence) * 0.10
            return (econPart + stabPart + infraPart + indPart + eduPart + milPart + infPart).toInt().coerceIn(1, 99)
        }

    val investmentAttractiveness: Int
        get() {
            val infraWeight = infrastructureIndex * 0.30
            val stabWeight = stability * 0.30
            val taxBonus = (0.35 - taxRate).coerceAtLeast(0.0) * 80.0
            val inflationPenalty = if (inflationRate > 6.0) (inflationRate - 6.0) * 1.5 else 0.0
            val base = (infraWeight + stabWeight + taxBonus - inflationPenalty).toInt()
            return base.coerceIn(10, 98)
        }

    val debtToGdpRatioPercent: Double
        get() = if (gdpBillions > 0) (sovereignDebtBillions / gdpBillions) * 100.0 else 0.0

    val relationStatus: DiplomaticRelationStatus
        get() = when {
            relationsWithPlayer >= 80 -> DiplomaticRelationStatus.FRIENDLY
            relationsWithPlayer >= 60 -> DiplomaticRelationStatus.COOPERATIVE
            relationsWithPlayer >= 40 -> DiplomaticRelationStatus.NEUTRAL
            relationsWithPlayer >= 20 -> DiplomaticRelationStatus.TENSE
            else -> DiplomaticRelationStatus.HOSTILE
        }

    val softPowerScore: Int
        get() = ((culturalInfluence * 0.4) + (diplomaticInfluence * 0.35) + (educationIndex * 0.25)).toInt().coerceIn(1, 99)
}
