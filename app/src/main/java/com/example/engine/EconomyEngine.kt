package com.example.engine

import com.example.model.Company
import com.example.model.Country
import com.example.model.CountryResource
import com.example.model.Factory
import com.example.model.MarketCommodity
import com.example.model.MonthlyBudget
import com.example.model.OutboundInvestment
import com.example.model.OutboundStatus
import com.example.model.Project
import com.example.model.TaxProfile
import com.example.model.TradeContract
import kotlin.math.max
import kotlin.math.min

object EconomyEngine {

    fun calculateMonthlyBudget(
        country: Country,
        companies: List<Company>,
        foreignProjects: List<Project>,
        outboundInvestments: List<OutboundInvestment> = emptyList(),
        tradeContracts: List<TradeContract> = emptyList(),
        marketCommodities: Map<String, MarketCommodity> = emptyMap()
    ): MonthlyBudget {
        val monthlyGdp = country.gdpBillions / 12.0
        val tax = country.taxProfile

        // --- 1. REVENUES ---
        // A. Taxes
        val laborIncomeBase = monthlyGdp * 0.45
        val incomeTaxRev = laborIncomeBase * tax.incomeTaxRate

        val corporateProfitBase = monthlyGdp * 0.28
        val corporateTaxRev = corporateProfitBase * tax.corporateTaxRate

        val consumerSpendingBase = monthlyGdp * 0.55
        val vatRev = consumerSpendingBase * tax.vatRate

        val generalTaxes = incomeTaxRev

        // B. State Enterprise Dividends
        val stateCompanyProfits = companies
            .filter { it.originCountryId == country.id && it.isStateOwned }
            .sumOf { it.stateMonthlyDividendsMillions / 1000.0 }

        // C. Factory Dividends
        val factoryProfits = country.factories
            .filter { !it.isPaused && it.isStateOwned }
            .sumOf { it.monthlyStateDividendsMillions / 1000.0 }

        // D. Customs and Tariffs (Trade contracts + baseline trade volume)
        val contractTariffs = tradeContracts
            .filter { it.buyerCountryId == country.id && !it.isPaused }
            .sumOf { it.monthlyTariffRevenueMillions / 1000.0 }
        val baselineTradeVolume = (monthlyGdp * 0.18) * tax.importTariffRate
        val customsAndTariffs = max(0.05, contractTariffs + baselineTradeVolume)

        // E. Resource Royalties & Exports
        var resourceRev = 0.0
        for ((_, res) in country.resources) {
            val commodity = marketCommodities[res.resourceType.name]
            val price = commodity?.currentPrice ?: res.resourceType.defaultBasePrice
            resourceRev += (res.calculateMonthlyGovernmentRevenue(price) / 1000.0)
        }
        val resourceExports = max(resourceRev, ((country.energyProduction * 0.15) + (country.agriculturalProduction * 0.06)) / 12.0)

        // F. Foreign & Outbound Investment Dividends
        val foreignProjectDividends = foreignProjects
            .filter { it.isCompleted && it.investorCountryId == country.id }
            .sumOf { it.monthlyReturnBillions }

        val outboundAssetDividends = outboundInvestments
            .filter { it.status == OutboundStatus.ACTIVE }
            .sumOf { it.monthlyDividendsBillions }

        val totalForeignDividends = foreignProjectDividends + outboundAssetDividends

        // G. Tourism & Strategic Services
        val tourismAndServices = ((country.stability * 0.04) + (country.infrastructureIndex * 0.035) + (country.culturalInfluence * 0.025)) / 12.0

        // --- 2. EXPENSES ---
        val popScale = country.populationMillions / 10.0

        val education = (popScale * 0.08 * (country.educationIndex / 100.0)) / 12.0
        val health = (popScale * 0.09 * (country.healthIndex / 100.0)) / 12.0
        val military = (country.militaryIndex * (monthlyGdp * 0.035) / 100.0)
        val police = (popScale * 0.04 * ((120 - country.stability) / 100.0)) / 12.0
        val infraMaintenance = (monthlyGdp * 0.025 * (country.infrastructureIndex / 100.0))
        val energyUtilities = (monthlyGdp * 0.015 * (country.energyIndex / 100.0))
        val subsidies = (popScale * 0.06 * ((110 - country.stability) / 100.0) * (country.taxProfile.vatRate + 0.05)) / 12.0
        val rnd = (country.industrialProduction * 0.04 + country.educationIndex * 0.03) / 12.0
        val adminPayroll = (monthlyGdp * 0.020)

        // Tiered Debt Service
        val debtRatio = country.debtToGdpRatioPercent
        val interestAnnualRate = when {
            debtRatio > 120.0 -> 0.095
            debtRatio > 80.0 -> 0.065
            debtRatio > 50.0 -> 0.045
            else -> 0.032
        }
        val debtInterest = (country.sovereignDebtBillions * interestAnnualRate) / 12.0

        return MonthlyBudget(
            taxRevenues = generalTaxes,
            corporateTaxRevenue = corporateTaxRev,
            vatRevenue = vatRev,
            stateCompanyProfits = stateCompanyProfits,
            factoryProfits = factoryProfits,
            customsAndTariffs = customsAndTariffs,
            resourceExports = resourceExports,
            foreignInvestmentsDividends = totalForeignDividends,
            tourismAndServices = tourismAndServices,
            educationBudget = education,
            healthcareBudget = health,
            infrastructureMaintenance = infraMaintenance,
            militaryAndSecurity = military,
            policeAndInterior = police,
            publicAdministration = adminPayroll,
            energyAndUtilities = energyUtilities,
            socialSubsidies = subsidies,
            researchAndDev = rnd,
            debtInterestService = debtInterest
        )
    }

    fun calculateNewGdpGrowth(
        country: Country,
        budget: MonthlyBudget,
        activeFactoriesCount: Int
    ): Double {
        val baseGrowth = country.tier.baseGdpScale * 2.5
        val taxDrag = (country.taxProfile.investmentPenaltyScore * 0.02)
        val stabilityBoost = (country.stability - 60) * 0.035
        val industryBoost = (activeFactoriesCount * 0.15)
        val budgetSurplusFactor = if (budget.netCashflow > 0) 0.3 else -0.4

        return (baseGrowth - taxDrag + stabilityBoost + industryBoost + budgetSurplusFactor)
            .coerceIn(-5.0, 10.0)
    }

    fun calculateNewInflation(country: Country, budget: MonthlyBudget): Double {
        val deficitPressure = if (budget.netCashflow < -0.5) (-budget.netCashflow * 0.4) else -0.2
        val vatPressure = country.taxProfile.vatRate * 8.0
        val baseInflation = country.inflationRate * 0.90 + (deficitPressure + vatPressure) * 0.10
        return baseInflation.coerceIn(0.5, 35.0)
    }

    fun calculateNewUnemployment(country: Country, gdpGrowth: Double, totalWorkers: Int): Double {
        val growthEffect = if (gdpGrowth > 2.5) -0.15 else 0.15
        val workersEffect = if (totalWorkers > 10000) -0.10 else 0.0
        return (country.unemploymentRate + growthEffect + workersEffect).coerceIn(1.5, 30.0)
    }

    fun calculateNewStability(country: Country, budget: MonthlyBudget): Int {
        var delta = 0
        if (country.inflationRate > 10.0) delta -= 1
        if (country.unemploymentRate > 12.0) delta -= 1
        if (budget.netCashflow < -1.0) delta -= 1
        if (country.taxProfile.publicDiscontentScore > 25.0) delta -= 1
        if (budget.socialSubsidies > 0.5) delta += 1
        if (country.gdpGrowthPercent > 3.0) delta += 1
        return (country.stability + delta).coerceIn(15, 99)
    }

    /**
     * "What-If" Policy Simulation Preview (Section 58)
     */
    fun previewPolicyChange(
        country: Country,
        policyNameAr: String,
        rateDeltaPercent: Double
    ): com.example.model.PolicyImpactPreview {
        val gdpBase = country.gdpBillions
        val revDeltaMillions = (gdpBase * (rateDeltaPercent / 100.0) * 0.35) * 1000.0 / 12.0
        val investmentDelta = if (rateDeltaPercent > 0) -(rateDeltaPercent * 0.8) else (-(rateDeltaPercent) * 0.6)
        val gdpDelta = if (rateDeltaPercent > 0) -(rateDeltaPercent * 0.04) else (-(rateDeltaPercent) * 0.03)
        val empDelta = if (rateDeltaPercent > 0) -(rateDeltaPercent * 0.05) else (-(rateDeltaPercent) * 0.04)
        val satDelta = if (rateDeltaPercent > 0) -(rateDeltaPercent * 0.6).toInt() else (-(rateDeltaPercent) * 0.5).toInt()

        val revText = if (revDeltaMillions >= 0) "+${String.format("%.1f", revDeltaMillions)}M$" else "${String.format("%.1f", revDeltaMillions)}M$"
        val invText = if (investmentDelta >= 0) "+${String.format("%.1f", investmentDelta)}%" else "${String.format("%.1f", investmentDelta)}%"
        val gdpText = if (gdpDelta >= 0) "+${String.format("%.2f", gdpDelta)}%" else "${String.format("%.2f", gdpDelta)}%"
        val empText = if (empDelta >= 0) "+${String.format("%.2f", empDelta)}%" else "${String.format("%.2f", empDelta)}%"
        val satText = if (satDelta >= 0) "+$satDelta نقطة" else "$satDelta نقطة"

        val verdict = if (rateDeltaPercent > 5.0) {
            "تحذير: زيادة كبيرة قد تضغط على الاستثمارات والرضا الشعبي رغم رفع الإيرادات"
        } else if (rateDeltaPercent > 0) {
            "إجراء متوازن: يدعم الخزينة العامة بتأثير طفيف ومقبول على النمو"
        } else {
            "تحفيز اقتصادي: ينعش الاستثمارات ومؤشرات الرضا بتكلفة تنازل عن جزء من الإيراد"
        }

        return com.example.model.PolicyImpactPreview(
            policyTitleAr = policyNameAr,
            revenueImpactDescriptionAr = revText,
            gdpImpactDescriptionAr = gdpText,
            employmentImpactDescriptionAr = empText,
            satisfactionImpactDescriptionAr = satText,
            netVerdictAr = verdict
        )
    }
}
