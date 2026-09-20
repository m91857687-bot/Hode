package com.example.model

enum class EnergySourceType(
    val titleAr: String,
    val icon: String,
    val baseCostPerGw: Double,
    val monthlyMaintenancePerGw: Double,
    val pollutionIndex: Int
) {
    OIL_THERMAL("محطات النفط الحرارية", "🛢️", 45.0, 1.2, 85),
    NATURAL_GAS_TURBINE("توربينات الغاز المركبة", "🔥", 35.0, 0.8, 45),
    COAL_POWER("محطات الفحم الحجري", "⛏️", 30.0, 1.0, 95),
    NUCLEAR("المفاعلات النووية السلمية", "☢️", 120.0, 2.5, 5),
    SOLAR("مزارع الطاقة الشمسية الكهروضوئية", "☀️", 50.0, 0.3, 0),
    WIND("توربينات الرياح البرية والبحرية", "💨", 55.0, 0.4, 0),
    HYDRO("السدود والمحطات الكهرومائية", "💧", 90.0, 0.6, 10)
}

data class EnergyPlant(
    val id: String,
    val type: EnergySourceType,
    val capacityGw: Double,
    val isOperational: Boolean = true
) {
    val monthlyCostMillions: Double
        get() = if (isOperational) capacityGw * type.monthlyMaintenancePerGw else 0.1
}

data class EnergyGridState(
    val plants: List<EnergyPlant>,
    val totalCapacityGw: Double,
    val currentProductionGw: Double,
    val totalDemandGw: Double,
    val gridStabilityPercent: Int = 100
) {
    val surplusOrDeficitGw: Double
        get() = currentProductionGw - totalDemandGw

    val hasShortage: Boolean
        get() = surplusOrDeficitGw < -0.1

    val shortagePercentage: Double
        get() = if (totalDemandGw > 0.0 && hasShortage) ((-surplusOrDeficitGw) / totalDemandGw * 100.0).coerceIn(0.0, 100.0) else 0.0
}

data class WaterGridState(
    val desalinationCapacityM3Daily: Double, // Millions of cubic meters
    val riverAndGroundwaterDaily: Double,
    val totalWaterSupplyDaily: Double,
    val populationConsumptionDaily: Double,
    val agriculturalConsumptionDaily: Double,
    val industrialConsumptionDaily: Double,
    val totalDemandDaily: Double
) {
    val netDailyBalance: Double
        get() = totalWaterSupplyDaily - totalDemandDaily

    val hasWaterShortage: Boolean
        get() = netDailyBalance < -0.05

    val shortagePercentage: Double
        get() = if (totalDemandDaily > 0.0 && hasWaterShortage) ((-netDailyBalance) / totalDemandDaily * 100.0).coerceIn(0.0, 100.0) else 0.0
}
