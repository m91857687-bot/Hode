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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.TechNode
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
fun ResearchScreen(
    state: GameState,
    onStartResearch: (String) -> Unit,
    onRecruitScientists: () -> Unit,
    modifier: Modifier = Modifier
) {
    val research = state.researchState
    val activeTech = research?.activeResearchTech

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CommandDarkBg)
            .padding(14.dp)
            .testTag("research_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "🔬 مجمع الأبحاث والتقنية الوطنية",
                    color = SovereignGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "شجرة التقنيات، براءات الاختراع، ومختبرات التطوير",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
            Text(
                text = "العلماء: ${research?.scientistsCount ?: 0}",
                color = TacticalCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Active Research Card
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
                    Text(
                        text = "📡 البحث العلمي الجاري حالياً:",
                        color = SovereignGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "ميزانية: $${String.format(Locale.US, "%.1f", research?.monthlyResearchBudgetMillions ?: 15.0)}M/شهر",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (activeTech != null) {
                    val remaining = (activeTech.requiredMonths - activeTech.currentProgressMonths).coerceAtLeast(1)
                    Text(
                        text = "${activeTech.field.icon} ${activeTech.titleAr}",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = activeTech.descriptionAr,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = (activeTech.progressPercent / 100f).coerceIn(0f, 1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = TacticalCyan,
                        trackColor = CommandSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "إنجاز ${activeTech.progressPercent}% • باقي تقريباً $remaining شهر",
                        color = TacticalCyan,
                        fontSize = 10.sp
                    )
                } else {
                    Text(
                        text = "لا يوجد مشروع بحث نشط حالياً. اختر تقنية من الشجرة أدناه للبدء.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Action: Recruit Scientists
        Button(
            onClick = onRecruitScientists,
            colors = ButtonDefaults.buttonColors(containerColor = CommandSurfaceElevated),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "🧑‍🔬 توظيف باحثين وعلماء إضافيين (+200)",
                color = SovereignGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "📚 شجرة التقنيات والمشاريع المتاحة:",
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        val techTree = research?.techTree ?: emptyList()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(techTree) { tech ->
                val isCurrent = (tech.id == activeTech?.id)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (tech.isCompleted) Color(0xFF132A1C) else CommandSurface)
                        .border(
                            1.dp,
                            if (tech.isCompleted) GrowthGreen else if (isCurrent) TacticalCyan else CommandBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Text(text = tech.field.icon, fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = tech.titleAr,
                                    color = if (tech.isCompleted) GrowthGreen else TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${tech.descriptionAr} (${tech.requiredMonths} شهر)",
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        if (tech.isCompleted) {
                            Text(
                                text = "مكتملة ✓",
                                color = GrowthGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else if (isCurrent) {
                            Text(
                                text = "قيد البحث...",
                                color = TacticalCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Button(
                                onClick = { onStartResearch(tech.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = TacticalCyan),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text(
                                    text = "بدء البحث",
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
