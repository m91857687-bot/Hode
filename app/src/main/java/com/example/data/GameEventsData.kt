package com.example.data

import com.example.model.EventChoice
import com.example.model.GameEvent

object GameEventsData {

    fun generateEvent(playerCountryId: String, currentYear: Int, currentMonth: Int): GameEvent {
        val pool = listOf(
            GameEvent(
                id = "evt_energy_boom_${System.currentTimeMillis()}",
                titleAr = "طفرة أسواق الطاقة والنفط العالمية ⚡",
                descriptionAr = "شهدت أسواق السلع الأساسية ارتفاعاً حاداً في الطلب العالمي على الطاقة بنسبة 25%. هذه الطفرة توفر فوائض مالية ضخمة للدول المصدرة.",
                icon = "⚡",
                categoryText = "أسواق الطاقة والسلع العالمية",
                choices = listOf(
                    EventChoice(
                        id = "c1",
                        titleAr = "خيار أ: إيداع العوائد بالكامل في الاحتياطي السيادي",
                        descriptionAr = "تعزيز السيولة والأمان المالي للدولة لمواجهة تقلبات المستقبل.",
                        treasuryBonusBillions = 8.0,
                        stabilityChange = 4
                    ),
                    EventChoice(
                        id = "c2",
                        titleAr = "خيار ب: دعم تكاليف الطاقة للمصانع والقطاع الخاص",
                        descriptionAr = "تحفيز الإنتاج الصناعي المحلي وزيادة تنافسية الصادرات.",
                        treasuryBonusBillions = 2.5,
                        gdpChangePercent = 1.4,
                        stabilityChange = 5
                    ),
                    EventChoice(
                        id = "c3",
                        titleAr = "خيار ج: إطلاق برنامج تحول للطاقة النظيفة والهيدروجين",
                        descriptionAr = "استثمار الفوائض في مشاريع استدامة وتقنيات المستقبل.",
                        costBillions = 3.0,
                        gdpChangePercent = 2.0,
                        stabilityChange = 6
                    ),
                    EventChoice(
                        id = "c4",
                        titleAr = "خيار د: توزيع حزمة علاوات معيشية للمواطنين",
                        descriptionAr = "رفع القوة الشرائية الشعبية والاستقرار الاجتماعي الفوري.",
                        costBillions = 2.0,
                        stabilityChange = 8,
                        inflationChange = 0.5
                    )
                ),
                timestampMonthYear = "$currentMonth / $currentYear"
            ),
            GameEvent(
                id = "evt_tech_ai_boom_${System.currentTimeMillis()}",
                titleAr = "ثورة الذكاء الاصطناعي والتسابق على الرقائق الرقمية 💻",
                descriptionAr = "تتسابق كبرى التحالفات التكنولوجية العالمية لتأمين مراكز حوسبة سحابية وبنية تحتية رقمية فائقة السرعة.",
                icon = "💻",
                categoryText = "التقنية والاقتصاد الرقمي",
                choices = listOf(
                    EventChoice(
                        id = "c1",
                        titleAr = "خيار أ: تقديم إعفاءات ضريبية وحزم أراضي مجانية لجذب الشركات",
                        descriptionAr = "استقطاب عمالقة التكنولوجيا العالميين لافتتاح مقرات إقليمية.",
                        costBillions = 1.5,
                        gdpChangePercent = 2.2,
                        stabilityChange = 3
                    ),
                    EventChoice(
                        id = "c2",
                        titleAr = "خيار ب: تأسيس شركة وطنية حكومية لتطوير نماذج الذكاء الاصطناعي",
                        descriptionAr = "حماية السيادة الرقمية وتوطين خوارزميات الحوسبة.",
                        costBillions = 2.8,
                        gdpChangePercent = 1.6,
                        stabilityChange = 4
                    ),
                    EventChoice(
                        id = "c3",
                        titleAr = "خيار ج: فرض ضرائب رقمية على مبيعات الشركات العالمية بالدولة",
                        descriptionAr = "تحقيق عائدات فورية للخزينة وحماية المتاجر والشركات المحلية.",
                        treasuryBonusBillions = 3.2,
                        stabilityChange = -1
                    ),
                    EventChoice(
                        id = "c4",
                        titleAr = "خيار د: عقد تحالف استراتيجي لتبادل الرقائق مع دولة حليفة",
                        descriptionAr = "تأمين سلاسل الإمداد التقنية ورفع النفوذ الدبلوماسي المشترك.",
                        costBillions = 1.0,
                        gdpChangePercent = 1.2,
                        stabilityChange = 5
                    )
                ),
                timestampMonthYear = "$currentMonth / $currentYear"
            )
        )
        return pool.random()
    }

    fun createInflationCrisisEvent(year: Int, month: Int): GameEvent {
        return GameEvent(
            id = "evt_inflation_crisis_${System.currentTimeMillis()}",
            titleAr = "تحذير: تصاعد معدلات التضخم وارتفاع تكاليف المعيشة 📈",
            descriptionAr = "تجاوز معدل التضخم حاجز الأمان مما أدى إلى ضغوط على القوة الشرائية للمواطنين والمصانع. الحكومة مطالبة باتخاذ قرار نقدي ومالي حاسم.",
            icon = "📈",
            categoryText = "الأزمات الاقتصادية والنقدية",
            choices = listOf(
                EventChoice(
                    id = "c1",
                    titleAr = "خيار أ: تشديد السياسة المالية ورفع أسعار الفائدة وخفض الإنفاق",
                    descriptionAr = "كبح جماح التضخم سريعاً مع احتمال تباطؤ طفيف في النمو الاقتصادي.",
                    inflationChange = -2.5,
                    gdpChangePercent = -0.6,
                    stabilityChange = -2
                ),
                EventChoice(
                    id = "c2",
                    titleAr = "خيار ب: دعم السلع الغذائية والمواد الأساسية من الخزينة",
                    descriptionAr = "تثبيت أسعار السلع وحماية الفئات الهشة بتكلفة مالية حكومية.",
                    costBillions = 2.4,
                    inflationChange = -1.2,
                    stabilityChange = 6
                ),
                EventChoice(
                    id = "c3",
                    titleAr = "خيار ج: خفض الرسوم الجمركية على الواردات لتشجيع العرض",
                    descriptionAr = "إغراق السوق بسلع بديلة رخيصة لكسر ارتفاع الأسعار.",
                    treasuryBonusBillions = -1.0,
                    inflationChange = -1.8,
                    stabilityChange = 3
                ),
                EventChoice(
                    id = "c4",
                    titleAr = "خيار د: فرض سقف إلزامي لأسعار السلع الاستراتيجية",
                    descriptionAr = "إجراءات رقابية صارمة لمنع جشع التجار واحتكار السلع.",
                    inflationChange = -1.0,
                    stabilityChange = 2
                )
            ),
            timestampMonthYear = "$month / $year"
        )
    }

    fun createUnemploymentReliefEvent(year: Int, month: Int): GameEvent {
        return GameEvent(
            id = "evt_unemployment_relief_${System.currentTimeMillis()}",
            titleAr = "مبادرة التوظيف الوطني وتأهيل الكوادر الشابة 👷",
            descriptionAr = "رصدت وزارة الاقتصاد ارتفاعاً في معدل البطالة وتخرج دفعات جامعية جديدة تبحث عن فرص عمل استثمارية.",
            icon = "👷",
            categoryText = "سوق العمل والتنمية البشرية",
            choices = listOf(
                EventChoice(
                    id = "c1",
                    titleAr = "خيار أ: إطلاق صندوق دعم رواتب التوظيف في القطاع الخاص",
                    descriptionAr = "تحمل الحكومة 40% من رواتب الموظفين الجدد لتشجيع الشركات على التعيين.",
                    costBillions = 1.8,
                    gdpChangePercent = 1.1,
                    stabilityChange = 7
                ),
                EventChoice(
                    id = "c2",
                    titleAr = "خيار ب: تدشين مشاريع بنية تحتية سريعة كثيفة العمالة",
                    descriptionAr = "بناء طرق وجسور وحدائق عامة لاستيعاب 50 ألف باحث عن عمل.",
                    costBillions = 2.5,
                    gdpChangePercent = 1.5,
                    stabilityChange = 8
                ),
                EventChoice(
                    id = "c3",
                    titleAr = "خيار ج: فرض نسبة توطين إلزامية (كوتة) صارمة على الشركات الأجنبية",
                    descriptionAr = "إلزام المستثمرين الأجانب بتوظيف 60% من العمالة المحلية.",
                    costBillions = 0.0,
                    stabilityChange = 4
                ),
                EventChoice(
                    id = "c4",
                    titleAr = "خيار د: تمويل حاضنات أعمال ومنح قروض ميسرة للمشاريع الناشئة",
                    descriptionAr = "تحويل الشباب إلى رواد أعمال وأصحاب مشاريع منتجة.",
                    costBillions = 1.2,
                    gdpChangePercent = 0.8,
                    stabilityChange = 5
                )
            ),
            timestampMonthYear = "$month / $year"
        )
    }

    fun createDebtRestructuringEvent(year: Int, month: Int): GameEvent {
        return GameEvent(
            id = "evt_debt_restructure_${System.currentTimeMillis()}",
            titleAr = "تحذير وكالات التصنيف الائتماني من تنامي الدين السيادي ⚠️",
            descriptionAr = "تجاوز الدين العام مستويات مرتفعة، ووكالات التصنيف الدولية تهدد بخفض الجدارة الائتمانية ما سيرفع تكلفة الاقتراض المستقبلي.",
            icon = "⚠️",
            categoryText = "الاستقرار المالي وإدارة الديون",
            choices = listOf(
                EventChoice(
                    id = "c1",
                    titleAr = "خيار أ: خطة تقشف حكومية وخفض نفقات الوزارات لخفض العجز",
                    descriptionAr = "إرسال إشارة طمأنة قوية لأسواق المال والمستثمرين الدوليين.",
                    treasuryBonusBillions = 4.0,
                    gdpChangePercent = -0.8,
                    stabilityChange = -4
                ),
                EventChoice(
                    id = "c2",
                    titleAr = "خيار ب: طرح سندات وصكوك سيادية طويلة الأجل بضمان أصول الدولة",
                    descriptionAr = "إعادة جدولة الديون العاجلة إلى فترات سداد مريحة.",
                    treasuryBonusBillions = 5.5,
                    stabilityChange = 2
                ),
                EventChoice(
                    id = "c3",
                    titleAr = "خيار ج: خصخصة جزئية لشركات وموانئ حكومية لسداد الديون",
                    descriptionAr = "توفير سيولة نقدية فورية ضخمة وإشراك القطاع الخاص في الإدارة.",
                    treasuryBonusBillions = 7.0,
                    stabilityChange = 1
                ),
                EventChoice(
                    id = "c4",
                    titleAr = "خيار د: التفاوض مع البنوك الدولية لشطب جزء من الفوائد",
                    descriptionAr = "استخدام الوزن الجيوسياسي للدولة للحصول على شروط تفضيلية.",
                    treasuryBonusBillions = 2.0,
                    stabilityChange = 4
                )
            ),
            timestampMonthYear = "$month / $year"
        )
    }

    fun createGlobalHeadquartersBiddingEvent(year: Int, month: Int): GameEvent {
        return GameEvent(
            id = "evt_hq_bidding_${System.currentTimeMillis()}",
            titleAr = "تنافس عالمي لافتتاح المقرات الإقليمية للشركات الدولية 🌐",
            descriptionAr = "ارتفعت جاذبية الاستثمار في دولتك إلى مستويات عالمية استثنائية، وعشرات الشركات متعددة الجنسيات تطلب افتتاح مقراتها الإقليمية لديك.",
            icon = "🌐",
            categoryText = "الاستثمار الأجنبي والريادة الاقتصادية",
            choices = listOf(
                EventChoice(
                    id = "c1",
                    titleAr = "خيار أ: إطلاق حي مالي عالمي مع إعفاءات لـ 10 سنوات للمقرات الكبرى",
                    descriptionAr = "تحويل العاصمة لمركز مالي واستثماري إقليمي رئيسي.",
                    costBillions = 2.0,
                    gdpChangePercent = 3.2,
                    stabilityChange = 8
                ),
                EventChoice(
                    id = "c2",
                    titleAr = "خيار ب: اشتراط فتح مراكز أبحاث وتطوير وتدريب مهندسين محليين",
                    descriptionAr = "الاستفادة القصوى من نقل المعرفة والتكنولوجيا المتقدمة.",
                    costBillions = 0.5,
                    gdpChangePercent = 2.5,
                    stabilityChange = 7
                ),
                EventChoice(
                    id = "c3",
                    titleAr = "خيار ج: فرض رسوم ترخيص سنوية رمزية وتخصيص استثماراتهم للشركات المحلية",
                    descriptionAr = "ربط رؤوس الأموال العالمية بإنعاش الشركات الوطنية الناشئة.",
                    treasuryBonusBillions = 4.5,
                    gdpChangePercent = 1.8,
                    stabilityChange = 6
                ),
                EventChoice(
                    id = "c4",
                    titleAr = "خيار د: توقيع اتفاقيات ثنائية مع دول المقرات لتعزيز النفوذ الدبلوماسي",
                    descriptionAr = "توظيف الحضور التجاري العالمي كأوراق ضغط ونفوذ سياسي.",
                    costBillions = 0.8,
                    gdpChangePercent = 1.5,
                    stabilityChange = 9
                )
            ),
            timestampMonthYear = "$month / $year"
        )
    }
}
