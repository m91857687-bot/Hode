package com.example.engine

import com.example.model.Country
import com.example.model.GovernmentServicesState
import com.example.model.PublicServiceStatus
import com.example.model.PublicServiceType
import com.example.model.SocialWelfarePrograms

object GovernmentEngine {

    fun initializeGovernmentServices(country: Country): GovernmentServicesState {
        val pop = country.populationMillions
        val priceIndex = EconomyScaleEngine.calculatePriceIndex(country)
        val servicesMap = mutableMapOf<PublicServiceType, PublicServiceStatus>()
        var totalEmployees = 0

        for (type in PublicServiceType.values()) {
            val empCount = (type.baseEmployeesPerMillionPop * pop).toInt().coerceAtLeast(150)
            totalEmployees += empCount

            val wagePerEmployee = when (type) {
                PublicServiceType.HEALTHCARE -> 2500.0
                PublicServiceType.EDUCATION -> 2200.0
                PublicServiceType.POLICE_SECURITY -> 2000.0
                PublicServiceType.ADMINISTRATION -> 2100.0
                else -> 1700.0
            }

            val scaledWage = EconomyScaleEngine.scaleSectorWage(wagePerEmployee, country)
            val wagesCost = (empCount * scaledWage) / 1_000_000.0
            val opCost = (wagesCost * 0.45 * priceIndex).coerceAtLeast(0.1)

            servicesMap[type] = PublicServiceStatus(
                type = type,
                fundingLevelPercent = 100,
                employeesCount = empCount,
                monthlyOperatingCostMillions = opCost,
                monthlyWagesCostMillions = wagesCost,
                efficiencyPercent = 85
            )
        }

        val baseWelfare = (pop * 0.40 * priceIndex).coerceAtLeast(0.5)
        val welfare = SocialWelfarePrograms(
            pensionsSpendingMonthlyMillions = baseWelfare * 0.40,
            unemploymentBenefitMonthlyMillions = baseWelfare * 0.20,
            foodSubsidyMonthlyMillions = baseWelfare * 0.20,
            energySubsidyMonthlyMillions = baseWelfare * 0.20
        )

        return GovernmentServicesState(
            services = servicesMap,
            welfare = welfare,
            totalCivilServantsCount = totalEmployees,
            publicSatisfactionBonus = 4
        )
    }

    fun processMonthlyGovernmentTick(
        current: GovernmentServicesState,
        country: Country
    ): GovernmentServicesState {
        var satisfactionBonus = 0

        val updatedServices = current.services.mapValues { (type, status) ->
            val efficiency = (status.fundingLevelPercent * 0.85).toInt().coerceIn(40, 120)
            if (status.fundingLevelPercent >= 110) {
                satisfactionBonus += 1
            } else if (status.fundingLevelPercent < 80) {
                satisfactionBonus -= 2
            }
            status.copy(efficiencyPercent = efficiency)
        }

        // Welfare impact on satisfaction
        if (current.welfare.totalMonthlyCostMillions > 0.0) {
            satisfactionBonus += 3
        }

        return current.copy(
            services = updatedServices,
            publicSatisfactionBonus = satisfactionBonus
        )
    }
}
