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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.model.GameState
import com.example.ui.theme.CommandBorder
import com.example.ui.theme.CommandDarkBg
import com.example.ui.theme.CommandSurface
import com.example.ui.theme.CommandSurfaceElevated
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.TacticalCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun MilitaryScreen(
    state: GameState,
    onAdjustBudget: (Double, Double, Double) -> Unit,
    onRecruitSoldiers: () -> Unit,
    modifier: Modifier = Modifier
) {
    val player = state.playerCountry ?: return
    val military = state.militaryState

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CommandDarkBg)
            .padding(14.dp)
            .verticalScroll(rememberScrollState())
            .testTag("military_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "🪖 القوات المسلحة والردع الاستراتيجي",
                    color = SovereignGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "الجاهزية القتالية، الصيانة، والصناعات الدفاعية",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
            Text(
                text = "مؤشر الدفاع: ${player.militaryIndex}/100",
                color = TacticalCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Readiness & Deterrence Dashboard Card
        military?.let { m ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CommandSurfaceElevated)
                    .border(1.2.dp, TacticalCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🛡️ مستوى الجاهزية القتالية:", color = SovereignGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "${m.readinessPercent}%",
                            color = if (m.readinessPercent > 75) GrowthGreen else SovereignGold,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = (m.readinessPercent / 100f).coerceIn(0f, 1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (m.readinessPercent > 75) GrowthGreen else SovereignGold,
                        trackColor = CommandSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(text = "القوات العاملة", color = TextSecondary, fontSize = 10.sp)
                            Text(text = "${m.activeSoldiersCount} جندي", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text(text = "مخزون العتاد", color = TextSecondary, fontSize = 10.sp)
                            Text(text = "${m.militaryEquipmentStockpile} وحدة", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text(text = "الردع الاستراتيجي", color = TextSecondary, fontSize = 10.sp)
                            Text(text = "${m.strategicDeterrenceScore} نقطة", color = TacticalCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action: Recruit Soldiers
            Button(
                onClick = onRecruitSoldiers,
                colors = ButtonDefaults.buttonColors(containerColor = CommandSurfaceElevated),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🎖️ تجنيد وتدريب ألوية جديدة (+5,000 جندي)",
                    color = SovereignGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Budget Allocation Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CommandSurface)
                    .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "💰 بنود موازنة الدفاع الشهرية:", color = SovereignGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                    BudgetAdjustRow(
                        title = "رواتب وتسليح الأفراد",
                        amountMillions = m.monthlyPersonnelBudgetMillions,
                        onIncrease = { onAdjustBudget(5.0, 0.0, 0.0) },
                        onDecrease = { onAdjustBudget(-5.0, 0.0, 0.0) }
                    )

                    BudgetAdjustRow(
                        title = "صيانة العتاد والمعدات والأسطول",
                        amountMillions = m.monthlyMaintenanceBudgetMillions,
                        onIncrease = { onAdjustBudget(0.0, 4.0, 0.0) },
                        onDecrease = { onAdjustBudget(0.0, -4.0, 0.0) }
                    )

                    BudgetAdjustRow(
                        title = "التدريب الميداني والمناورات المشتركة",
                        amountMillions = m.monthlyTrainingBudgetMillions,
                        onIncrease = { onAdjustBudget(0.0, 0.0, 3.0) },
                        onDecrease = { onAdjustBudget(0.0, 0.0, -3.0) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Defense Branch overview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CommandSurface)
                    .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = "⚔️ أفرع القوات المسلحة:", color = TacticalCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = "• القوات البرية: جاهزية ${m.readinessPercent}% • دبابات ومدرعات قياسية", color = TextPrimary, fontSize = 11.sp)
                    Text(text = "• القوات الجوية: دفاع جوي متكامل وقواعد مقاتلات حديثة", color = TextPrimary, fontSize = 11.sp)
                    Text(text = "• القوات البحرية: حماية الموانئ والممرات الملاحية والتجارية", color = TextPrimary, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun BudgetAdjustRow(
    title: String,
    amountMillions: Double,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "$${String.format(Locale.US, "%.1f", amountMillions)}M/شهر", color = TacticalCyan, fontSize = 11.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = onDecrease,
                colors = ButtonDefaults.buttonColors(containerColor = CommandSurfaceElevated),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(28.dp)
            ) {
                Text(text = "- خفض", color = TextSecondary, fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(
                onClick = onIncrease,
                colors = ButtonDefaults.buttonColors(containerColor = TacticalCyan),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(28.dp)
            ) {
                Text(text = "+ دعم", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
