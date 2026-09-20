package com.example.engine

import com.example.model.Country
import com.example.model.MilitaryBranch
import com.example.model.MilitaryState
import com.example.model.MilitaryUnitType

object MilitaryEngine {

    fun initializeMilitaryState(country: Country): MilitaryState {
        val tier = EconomyScaleEngine.getCountryScaleTier(country)
        val soldierRatio = when (tier) {
            com.example.model.CountryScaleTier.TINY -> 0.003
            com.example.model.CountryScaleTier.SMALL -> 0.005
            com.example.model.CountryScaleTier.MEDIUM -> 0.007
            com.example.model.CountryScaleTier.LARGE -> 0.006
            com.example.model.CountryScaleTier.MAJOR -> 0.005
            com.example.model.CountryScaleTier.GLOBAL_POWER -> 0.0045
        }

        val soldiers = (country.populationMillions * 1_000_000 * soldierRatio).toInt().coerceAtLeast(3000)
        val avgSoldierSalary = EconomyScaleEngine.scaleSectorWage(2300.0, country)
        val personnelBudgetMillions = (soldiers * avgSoldierSalary) / 1_000_000.0

        val unitsMap = mapOf(
            MilitaryBranch.INFANTRY to MilitaryUnitType(MilitaryBranch.INFANTRY, "ألوية المشاة الآلية", (soldiers / 3000).coerceAtLeast(2), 12.0, 150),
            MilitaryBranch.ARMOR to MilitaryUnitType(MilitaryBranch.ARMOR, "كتائب الدبابات والمدرعات", (soldiers / 6000).coerceAtLeast(1), 35.0, 320),
            MilitaryBranch.ARTILLERY to MilitaryUnitType(MilitaryBranch.ARTILLERY, "أفواج المدفعية وراجمات الصواريخ", (soldiers / 8000).coerceAtLeast(1), 22.0, 200),
            MilitaryBranch.AIR_DEFENSE to MilitaryUnitType(MilitaryBranch.AIR_DEFENSE, "بطاريات الدفاع الجوي والرادار", (soldiers / 9000).coerceAtLeast(1), 40.0, 350),
            MilitaryBranch.AIR_FORCE to MilitaryUnitType(MilitaryBranch.AIR_FORCE, "أسراب المقاتلات والطيران الحربي", (soldiers / 12000).coerceAtLeast(1), 85.0, 580),
            MilitaryBranch.NAVY to MilitaryUnitType(MilitaryBranch.NAVY, "قطع الأسطول والفرقاطات البحرية", if (country.continent != com.example.model.Continent.EUROPE) 4 else 8, 90.0, 450),
            MilitaryBranch.SPECIAL_FORCES to MilitaryUnitType(MilitaryBranch.SPECIAL_FORCES, "مجموعات المهام الخاصة", 3, 18.0, 250),
            MilitaryBranch.STRATEGIC_DETERRENCE to MilitaryUnitType(MilitaryBranch.STRATEGIC_DETERRENCE, "منظومات الردع الاستراتيجي", if (country.militaryIndex > 75) 2 else 0, 110.0, 600)
        )

        val maintenanceBudget = (personnelBudgetMillions * 0.70).coerceAtLeast(0.5)
        val trainingBudget = (personnelBudgetMillions * 0.25).coerceAtLeast(0.2)

        return MilitaryState(
            activeSoldiersCount = soldiers,
            units = unitsMap,
            readinessPercent = (country.militaryIndex * 0.90 + 5.0).toInt().coerceIn(30, 98),
            trainingLevelPercent = (country.militaryIndex * 0.85 + 10.0).toInt().coerceIn(30, 95),
            monthlyPersonnelBudgetMillions = personnelBudgetMillions,
            monthlyMaintenanceBudgetMillions = maintenanceBudget,
            monthlyTrainingBudgetMillions = trainingBudget,
            strategicDeterrenceScore = (country.militaryIndex * 0.6).toInt(),
            militaryEquipmentStockpile = (soldiers / 100).coerceAtLeast(50)
        )
    }

    fun processMonthlyMilitaryTick(
        current: MilitaryState,
        country: Country
    ): MilitaryState {
        // Calculate needed maintenance
        val requiredMaintenance = current.units.values.sumOf { (it.activeCount * it.baseMonthlyMaintenanceCostPerUnit) / 1000.0 }
        val maintenanceRatio = if (requiredMaintenance > 0) (current.monthlyMaintenanceBudgetMillions / requiredMaintenance).coerceIn(0.2, 1.5) else 1.0

        // If maintenance is underfunded, readiness decays
        val readinessDelta = when {
            maintenanceRatio >= 1.0 -> +1
            maintenanceRatio >= 0.8 -> 0
            else -> -3
        }

        val updatedReadiness = (current.readinessPercent + readinessDelta).coerceIn(20, 100)

        // Training effect
        val trainingDelta = if (current.monthlyTrainingBudgetMillions > current.monthlyPersonnelBudgetMillions * 0.2) +1 else 0
        val updatedTraining = (current.trainingLevelPercent + trainingDelta).coerceIn(30, 100)

        return current.copy(
            readinessPercent = updatedReadiness,
            trainingLevelPercent = updatedTraining
        )
    }
}
