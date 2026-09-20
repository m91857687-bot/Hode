package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.GameEngine
import com.example.model.GameState
import com.example.ui.theme.CommandBorder
import com.example.ui.theme.CommandDarkBg
import com.example.ui.theme.CommandSurface
import com.example.ui.theme.CommandSurfaceElevated
import com.example.ui.theme.CrisisRed
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.TacticalCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun EconomyScreen(
    state: GameState,
    onTaxRateChange: (Double) -> Unit,
    onPayDownDebt: (Double) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val player = state.playerCountry ?: return
    val budget = GameEngine.calculateMonthlyBudget(
        player,
        state.companies,
        state.foreignInvestments,
        state.outboundInvestments
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CommandDarkBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("economy_screen")
    ) {
        // Macroeconomic Health Indicators (Inflation, Unemployment, Reserves, Production)
        Text(
            text = "المؤشرات الاقتصادية الكلية (Macro Dashboard)",
            color = TacticalCyan,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Inflation
            MacroIndicatorCard(
                title = "التضخم السنوي",
                value = "${String.format(Locale.US, "%.1f", player.inflationRate)}%",
                status = when {
                    player.inflationRate < 3.0 -> "طبيعي مستقر"
                    player.inflationRate < 6.0 -> "مرتفع معتدل"
                    player.inflationRate < 10.0 -> "تضخم مرتفع"
                    else -> "أزمة غلاء"
                },
                color = when {
                    player.inflationRate < 3.0 -> GrowthGreen
                    player.inflationRate < 6.0 -> SovereignGold
                    else -> CrisisRed
                },
                modifier = Modifier.weight(1f)
            )

            // Unemployment
            MacroIndicatorCard(
                title = "معدل البطالة",
                value = "${String.format(Locale.US, "%.1f", player.unemploymentRate)}%",
                status = when {
                    player.unemploymentRate < 4.5 -> "تشغيل كامل"
                    player.unemploymentRate < 7.0 -> "معدل صحي"
                    else -> "بطالة مقلقة"
                },
                color = when {
                    player.unemploymentRate < 4.5 -> GrowthGreen
                    player.unemploymentRate < 7.0 -> SovereignGold
                    else -> CrisisRed
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Foreign Reserves
            MacroIndicatorCard(
                title = "الاحتياطي النقدي والذهب",
                value = "$${String.format(Locale.US, "%.1f", player.foreignReservesBillions)}B",
                status = "صمام الأمان والسيادة",
                color = SovereignGold,
                modifier = Modifier.weight(1f)
            )

            // Production Score
            MacroIndicatorCard(
                title = "مؤشر الإنتاج الوطني",
                value = "${player.industryProductionScore}/100",
                status = "الطاقة الصناعية",
                color = TacticalCyan,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Cashflow Summary Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CommandSurfaceElevated)
                .border(1.2.dp, if (budget.netCashflow >= 0) GrowthGreen.copy(alpha = 0.5f) else CrisisRed.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "الميزان المالي الشهري (Cashflow)",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (budget.netCashflow >= 0) "+$${String.format(Locale.US, "%.2f", budget.netCashflow)}B / شهر (فائض)" else "-$${String.format(Locale.US, "%.2f", -budget.netCashflow)}B / شهر (عجز)",
                        color = if (budget.netCashflow >= 0) GrowthGreen else CrisisRed,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "إجمالي الإيرادات: $${String.format(Locale.US, "%.2f", budget.totalRevenue)}B",
                        color = GrowthGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "إجمالي النفقات: $${String.format(Locale.US, "%.2f", budget.totalExpense)}B",
                        color = CrisisRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Interactive Tax Policy Lever Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CommandSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, TacticalCyan.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🏛️ السياسة الضريبية العامة",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${(player.taxRate * 100).toInt()}%",
                        color = TacticalCyan,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "رفع الضرائب يزيد دخل الخزينة الفوري، لكنه قد يخفض رضا الشركات والجاذبية الاستثمارية.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = player.taxRate.toFloat(),
                    onValueChange = { onTaxRateChange(it.toDouble()) },
                    valueRange = 0.08f..0.45f,
                    colors = SliderDefaults.colors(
                        thumbColor = TacticalCyan,
                        activeTrackColor = TacticalCyan,
                        inactiveTrackColor = CommandBorder
                    ),
                    modifier = Modifier.testTag("tax_rate_slider")
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "ملاذ استثماري (8%)", color = GrowthGreen, fontSize = 10.sp)
                    Text(text = "متوازن (20%)", color = SovereignGold, fontSize = 10.sp)
                    Text(text = "أعباء ضريبية قصوى (45%)", color = CrisisRed, fontSize = 10.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Revenues Breakdown
        Text(
            text = "مصادر الإيرادات الشهرية (الدخل الحكومي)",
            color = GrowthGreen,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CommandSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CommandBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                BudgetItem(title = "ضرائب الشركات والدخل", value = budget.taxRevenues, isRevenue = true)
                BudgetItem(title = "أرباح الشركات الحكومية السيادية", value = budget.stateCompanyProfits, isRevenue = true)
                BudgetItem(title = "صادرات الطاقة والموارد الطبيعية", value = budget.resourceExports, isRevenue = true)
                BudgetItem(title = "الجمارك ورسوم التجارة والترانزيت", value = budget.customsAndTariffs, isRevenue = true)
                BudgetItem(title = "عوائد الاستثمارات بالخارج وأصول الدولة", value = budget.foreignInvestmentsDividends, isRevenue = true)
                BudgetItem(title = "السياحة والخدمات والرسوم", value = budget.tourismAndServices, isRevenue = true)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Expenditures Breakdown
        Text(
            text = "بنود الإنفاق والمصروفات الشهرية",
            color = CrisisRed,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CommandSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CommandBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                BudgetItem(title = "التعليم ورأس المال البشري", value = budget.educationBudget, isRevenue = false)
                BudgetItem(title = "الرعاية الصحية والمستشفيات", value = budget.healthcareBudget, isRevenue = false)
                BudgetItem(title = "صيانة وتطوير البنية التحتية والمشاريع", value = budget.infrastructureMaintenance, isRevenue = false)
                BudgetItem(title = "الدفاع والجيش والأمن الداخلي", value = budget.militaryAndSecurity, isRevenue = false)
                BudgetItem(title = "الدعم الاجتماعي وشبكات الأمان", value = budget.socialSubsidies, isRevenue = false)
                BudgetItem(title = "البحث العلمي والابتكار والتطوير", value = budget.researchAndDev, isRevenue = false)
                BudgetItem(title = "خدمة الدين العام والفوائد السيادية", value = budget.debtInterestService, isRevenue = false)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Sovereign Debt & Solvency
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CommandSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CommandBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "🏦 الملاءة المالية والدين السيادي",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "إجمالي الدين العام:", color = TextSecondary, fontSize = 12.sp)
                    Text(
                        text = "$${String.format(Locale.US, "%.0f", player.sovereignDebtBillions)}B",
                        color = CrisisRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "نسبة الدين إلى الناتج (Debt-to-GDP):", color = TextSecondary, fontSize = 12.sp)
                    val ratio = (player.sovereignDebtBillions / player.gdpBillions) * 100.0
                    Text(
                        text = "${String.format(Locale.US, "%.1f", ratio)}%",
                        color = if (ratio < 60) GrowthGreen else if (ratio < 100) SovereignGold else CrisisRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Pay down debt action
                val canPayDebt = player.treasuryBillions >= 5.0 && player.sovereignDebtBillions > 0.0
                Button(
                    onClick = { onPayDownDebt(5.0) },
                    enabled = canPayDebt,
                    colors = ButtonDefaults.buttonColors(containerColor = TacticalCyan),
                    modifier = Modifier.fillMaxWidth().testTag("pay_debt_btn")
                ) {
                    Icon(Icons.Default.Payments, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (canPayDebt) "سداد $5.0B من الدين العام لخفض الفوائد" else "سداد الدين يتطلب سيولة 5B+ بالخزينة",
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun MacroIndicatorCard(
    title: String,
    value: String,
    status: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(CommandSurface)
            .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(text = title, color = TextSecondary, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = color, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = status, color = TextSecondary, fontSize = 9.sp)
        }
    }
}

@Composable
private fun BudgetItem(title: String, value: Double, isRevenue: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = TextSecondary, fontSize = 11.sp)
        Text(
            text = "${if (isRevenue) "+" else "-"}$${String.format(Locale.US, "%.2f", value)}B",
            color = if (isRevenue) GrowthGreen else CrisisRed,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
