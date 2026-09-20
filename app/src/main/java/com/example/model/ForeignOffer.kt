package com.example.model

enum class OfferStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    NEGOTIATING
}

enum class InvestorType(val titleAr: String, val philosophyAr: String) {
    AggressiveGrowth("نمو هجومي وسريع", "يركز على العائد المالي السريع والحصة السوقية العالية"),
    LongTerm("مستثمر استراتيجي طويل الأجل", "يقدر الاستقرار والبنية التحتية والاستدامة"),
    ResourceFocused("مركز على الموارد والطاقة", "يهتم بتأمين الإمدادات والمواد الخام وسلاسل التوريد"),
    TechnologyFocused("رائد تقني وابتكاري", "يركز على نقل التكنولوجيا ومراكز الأبحاث والمهارات العالية"),
    LowRisk("متحفظ ومنخفض المخاطر", "يطلب ضمانات استقرار وإعفاءات ضريبية وحماية قانونية"),
    GovernmentBacked("صندوق سيادي حكومي", "يتحالف مع الدولة ومستعد للشراكات المتكافئة والتمويل المشترك"),
    Strategic("جيوسياسي استراتيجي", "يركز على الموانئ واللوجستيات والممرات التجارية والنفوذ")
}

data class ForeignOffer(
    val id: String,
    val proposingCountryId: String,
    val foreignCompanyName: String,
    val projectTitleAr: String,
    val descriptionAr: String,
    val category: ProjectCategory,
    val investmentValueBillions: Double,
    val jobsOffered: Int,
    val projectDurationMonths: Int,
    val investorType: InvestorType = InvestorType.LongTerm,
    
    // Initial proposal terms
    val initialForeignEquity: Double = 0.60,
    val initialStateEquity: Double = 0.40,
    val initialTaxRate: Double = 0.15,
    val initialLocalJobQuota: Double = 0.50,
    val initialTaxHolidayYears: Int = 2,
    val initialContractYears: Int = 15,
    val initialLocalProductionRatio: Double = 0.40,
    val initialExportRatio: Double = 0.50,

    // Current negotiated terms
    val negotiatedStateEquity: Double = 0.40,
    val negotiatedTaxRate: Double = 0.15,
    val negotiatedLocalJobQuota: Double = 0.50,
    val negotiatedTaxHolidayYears: Int = 2,
    val negotiatedContractYears: Int = 15,
    val negotiatedLocalProductionRatio: Double = 0.40,
    val negotiatedExportRatio: Double = 0.50,
    val hasTechTransferClause: Boolean = true,
    val hasLocalRdCenter: Boolean = false,
    val hasLocalSuppliersCommitment: Boolean = true,
    
    val environmentalImpactScore: Int = 15, // Lower is cleaner
    val strategicInfluenceBoost: Int = 4,

    val status: OfferStatus = OfferStatus.PENDING,
    val aiAcceptanceLikelihood: Int = 75,
    val aiFeedbackMessage: String = ""
) {
    val expectedAnnualTaxesMillions: Double
        get() {
            val annualRevenueEstimate = investmentValueBillions * 0.25 * 1000.0 // millions
            return annualRevenueEstimate * negotiatedTaxRate
        }

    val negotiatedForeignEquity: Double
        get() = (1.0 - negotiatedStateEquity).coerceIn(0.0, 1.0)
}
