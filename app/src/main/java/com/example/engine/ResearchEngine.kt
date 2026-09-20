package com.example.engine

import com.example.model.Country
import com.example.model.ResearchField
import com.example.model.ResearchState
import com.example.model.TechNode

object ResearchEngine {

    fun initializeResearchState(country: Country): ResearchState {
        val tier = EconomyScaleEngine.getCountryScaleTier(country)
        val scientists = when (tier) {
            com.example.model.CountryScaleTier.TINY -> 250
            com.example.model.CountryScaleTier.SMALL -> 800
            com.example.model.CountryScaleTier.MEDIUM -> 3000
            com.example.model.CountryScaleTier.LARGE -> 12000
            com.example.model.CountryScaleTier.MAJOR -> 45000
            com.example.model.CountryScaleTier.GLOBAL_POWER -> 160000
        }

        val budgetMillions = EconomyScaleEngine.scaleResearchCost(2.5, country)

        val defaultTechs = listOf(
            TechNode(
                id = "tech_precision_agri",
                titleAr = "الزراعة الدقيقة والمحمية",
                descriptionAr = "زيادة إنتاجية المحاصيل الغذائية بنسبة 25% وخفض استهلاك المياه الزراعية.",
                field = ResearchField.AGRICULTURE,
                baseCostMillions = 3.0,
                requiredMonths = 6,
                requiredScientists = 300,
                isCompleted = country.agricultureIndex > 70,
                productivityBonusPercent = 0.05
            ),
            TechNode(
                id = "tech_automation",
                titleAr = "الأتمتة الصناعية والروبوتات",
                descriptionAr = "رفع كفاءة المصانع بنسبة 20% وخفض الاحتياج لعمالة التجميع اليدوية.",
                field = ResearchField.AI_ROBOTICS,
                baseCostMillions = 8.0,
                requiredMonths = 10,
                requiredScientists = 600,
                isCompleted = country.industryIndex > 75,
                factoryOutputBonusPercent = 0.15
            ),
            TechNode(
                id = "tech_green_grid",
                titleAr = "الشبكات الذكية وتخزين الطاقة",
                descriptionAr = "رفع كفاءة محطات الطاقة المتجددة بنسبة 30% وتقليل تكاليف الصيانة.",
                field = ResearchField.ENERGY,
                baseCostMillions = 6.0,
                requiredMonths = 8,
                requiredScientists = 450,
                isCompleted = country.energyIndex > 80,
                energyEfficiencyBonusPercent = 0.20
            ),
            TechNode(
                id = "tech_advanced_metallurgy",
                titleAr = "تعدين وسبائك الصلب فائق المتانة",
                descriptionAr = "إنتاج سبائك صلب وألمنيوم عالية الجودة لقطاعات البناء والمركبات والدفاع.",
                field = ResearchField.MATERIALS,
                baseCostMillions = 7.0,
                requiredMonths = 9,
                requiredScientists = 500,
                isCompleted = false,
                factoryOutputBonusPercent = 0.12
            ),
            TechNode(
                id = "tech_semiconductor_nano",
                titleAr = "معالجات النانو الدقيقة",
                descriptionAr = "تصميم رقائق إلكترونية ومعالجات متقدمة لرفع إيرادات التقنية.",
                field = ResearchField.ELECTRONICS,
                baseCostMillions = 16.0,
                requiredMonths = 14,
                requiredScientists = 1200,
                isCompleted = false,
                productivityBonusPercent = 0.10
            ),
            TechNode(
                id = "tech_telemedicine",
                titleAr = "الرعاية الصحية الرقمية المتقدمة",
                descriptionAr = "رفع جودة الخدمات الطبية وخفض تكاليف إدارة المستشفيات والحد من الأمراض.",
                field = ResearchField.MEDICINE,
                baseCostMillions = 5.0,
                requiredMonths = 7,
                requiredScientists = 400,
                isCompleted = country.healthIndex > 75,
                productivityBonusPercent = 0.04
            ),
            TechNode(
                id = "tech_logistics_ai",
                titleAr = "خوارزميات إدارة الشحن واللوجستيات",
                descriptionAr = "خفض تكاليف النقل في التجارة الدولية بنسبة 20% وزيادة سرعة الصادرات.",
                field = ResearchField.TRANSPORTATION,
                baseCostMillions = 4.0,
                requiredMonths = 6,
                requiredScientists = 350,
                isCompleted = false,
                tradeLogisticsBonusPercent = 0.20
            ),
            TechNode(
                id = "tech_quantum_materials",
                titleAr = "المواد الكمومية والبطاريات الصلبة",
                descriptionAr = "فتح الجيل الجديد من البطاريات العملاقة والصناعات الاستراتيجية المتقدمة.",
                field = ResearchField.MATERIALS,
                baseCostMillions = 25.0,
                requiredMonths = 18,
                requiredScientists = 2000,
                isCompleted = false,
                prerequisiteTechId = "tech_advanced_metallurgy"
            )
        )

        return ResearchState(
            scientistsCount = scientists,
            monthlyResearchBudgetMillions = budgetMillions,
            laboratoriesCount = (scientists / 150).coerceAtLeast(2),
            universitiesCount = (scientists / 600).coerceAtLeast(1),
            techTree = defaultTechs
        )
    }

    fun processMonthlyResearchTick(
        current: ResearchState,
        country: Country
    ): ResearchState {
        val activeTech = current.activeResearchTech ?: return current

        // Calculate progress rate: depends on budget, scientists available, and laboratories
        val hasEnoughScientists = current.scientistsCount >= activeTech.requiredScientists
        val progressDelta = if (hasEnoughScientists) 1 else 0

        val newProgressMonths = activeTech.currentProgressMonths + progressDelta
        val isNowCompleted = newProgressMonths >= activeTech.requiredMonths

        val updatedTree = current.techTree.map { node ->
            if (node.id == activeTech.id) {
                node.copy(
                    currentProgressMonths = newProgressMonths,
                    isCompleted = isNowCompleted,
                    isUnderActiveResearch = !isNowCompleted
                )
            } else node
        }

        return current.copy(techTree = updatedTree)
    }

    fun selectTechToResearch(current: ResearchState, techId: String): ResearchState {
        val target = current.techTree.find { it.id == techId } ?: return current
        if (target.isCompleted) return current

        val updatedTree = current.techTree.map { node ->
            when {
                node.id == techId -> node.copy(isUnderActiveResearch = true)
                node.isUnderActiveResearch -> node.copy(isUnderActiveResearch = false)
                else -> node
            }
        }
        return current.copy(techTree = updatedTree)
    }
}
