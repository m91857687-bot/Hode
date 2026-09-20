package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.window.Dialog
import com.example.model.MonthlyReport
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
fun MonthlyReportDialog(
    report: MonthlyReport,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 620.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.2.dp, SovereignGold.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                .testTag("monthly_report_dialog"),
            colors = CardDefaults.cardColors(containerColor = CommandDarkBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📜 التقرير الشهري لمجلس الوزراء",
                            color = SovereignGold,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "تحليل الأداء الاقتصادي والأسباب والنتائج - ${report.monthNumber} / ${report.year}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Primary Financial Overview Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CommandSurfaceElevated)
                        .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "الإيرادات الشهرية", color = TextSecondary, fontSize = 11.sp)
                                Text(
                                    text = "+$${String.format(Locale.US, "%.1f", report.monthlyRevenueMillions)}M",
                                    color = GrowthGreen,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text(text = "المصروفات العامة", color = TextSecondary, fontSize = 11.sp)
                                Text(
                                    text = "-$${String.format(Locale.US, "%.1f", report.monthlyExpenseMillions)}M",
                                    color = CrisisRed,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text(text = "صافي الشهر", color = TextSecondary, fontSize = 11.sp)
                                val net = report.netSurplusOrDeficitMillions
                                Text(
                                    text = if (net >= 0) "+$${String.format(Locale.US, "%.1f", net)}M" else "-$${String.format(Locale.US, "%.1f", -net)}M",
                                    color = if (net >= 0) GrowthGreen else CrisisRed,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = CommandBorder, thickness = 0.8.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        // GDP Growth & Breakdown
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "نمو الناتج المحلي الإجمالي:", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text(
                                text = "${if (report.gdpGrowthPercent >= 0) "+" else ""}${String.format(Locale.US, "%.2f", report.gdpGrowthPercent)}%",
                                color = if (report.gdpGrowthPercent >= 0) GrowthGreen else CrisisRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "أسباب حركة الناتج:", color = SovereignGold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        report.gdpCauses.forEach { cause ->
                            Text(text = "• $cause", color = TextSecondary, fontSize = 10.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Employment & Production highlights
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CommandSurface)
                            .border(1.dp, CommandBorder, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(text = "💼 وظائف جديدة", color = TextSecondary, fontSize = 10.sp)
                            Text(
                                text = "+${report.newJobsCount}",
                                color = TacticalCyan,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CommandSurface)
                            .border(1.dp, CommandBorder, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(text = "🏭 نمو صناعي", color = TextSecondary, fontSize = 10.sp)
                            Text(
                                text = "+${String.format(Locale.US, "%.1f", report.industrialGrowthPercent)}%",
                                color = SovereignGold,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CommandSurface)
                            .border(1.dp, CommandBorder, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(text = "😊 مؤشر الرضا", color = TextSecondary, fontSize = 10.sp)
                            val sat = report.satisfactionChangePoints
                            Text(
                                text = if (sat >= 0) "+$sat" else "$sat",
                                color = if (sat >= 0) GrowthGreen else CrisisRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Key Strategic Highlights
                Text(
                    text = "🌟 أبرز المؤشرات والقرارات التنفيذية:",
                    color = TacticalCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                report.topHighlights.forEach { hl ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(CommandSurface)
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(text = "✓ $hl", color = TextPrimary, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = TacticalCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "متابعة وإغلاق التقرير",
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
