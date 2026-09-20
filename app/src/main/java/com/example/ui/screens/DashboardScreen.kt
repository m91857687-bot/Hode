package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.model.NavigationTab
import com.example.ui.theme.CommandBorder
import com.example.ui.theme.CommandDarkBg
import com.example.ui.theme.CommandSurface
import com.example.ui.theme.CommandSurfaceElevated
import com.example.ui.theme.CrisisRed
import com.example.ui.theme.DiplomaticPurple
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.TacticalCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    state: GameState,
    onNavigateTab: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val player = state.playerCountry ?: return

    // Total player global influence
    val totalPlayerInfluence = state.influenceNetwork
        .filter { it.sourceCountryId == player.id }
        .sumOf { it.totalScore }

    val activeDomesticCount = state.domesticProjects.count { !it.isCompleted }
    val completedDomesticCount = state.domesticProjects.count { it.isCompleted }
    val foreignInvestmentsCount = state.foreignInvestments.count { it.isCompleted }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CommandDarkBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("dashboard_screen")
    ) {
        // Hero Card: Country Crest & National Power Index
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(CommandSurfaceElevated, Color(0xFF1E293B))
                    )
                )
                .border(1.2.dp, TacticalCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = player.flag, fontSize = 36.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = player.nameAr,
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "العاصمة: ${player.capital} • ${player.aiArchetype.titleAr}",
                                color = TacticalCyan,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // National Power Circle Gauge
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(CommandDarkBg)
                            .border(2.dp, SovereignGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "⭐", fontSize = 11.sp)
                            Text(
                                text = "${player.nationalPowerScore}",
                                color = SovereignGold,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatPill(
                        label = "الناتج المحلي (GDP)",
                        value = "$${String.format(Locale.US, "%.0f", player.gdpBillions)}B",
                        sub = "+${String.format(Locale.US, "%.1f", player.gdpGrowthPercent)}%",
                        accent = TacticalCyan,
                        modifier = Modifier.weight(1f)
                    )
                    StatPill(
                        label = "الخزينة المتاحة",
                        value = "$${String.format(Locale.US, "%.1f", player.treasuryBillions)}B",
                        sub = "سيولة",
                        accent = SovereignGold,
                        modifier = Modifier.weight(1f)
                    )
                    StatPill(
                        label = "النفوذ العالمي",
                        value = "$totalPlayerInfluence",
                        sub = "نقطة",
                        accent = DiplomaticPurple,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Navigation Tactical Action Grid
        Text(
            text = "القطاعات والإدارة السيادية",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionTile(
                icon = "💰",
                title = "الميزانية والضرائب",
                subtitle = "التحكم بالسياسة المالية",
                onClick = { onNavigateTab(NavigationTab.ECONOMY) },
                modifier = Modifier.weight(1f),
                testTag = "dash_nav_economy"
            )
            ActionTile(
                icon = "📈",
                title = "المشاريع والاستثمار",
                subtitle = "$activeDomesticCount مشاريع جارية",
                onClick = { onNavigateTab(NavigationTab.INVESTMENTS) },
                modifier = Modifier.weight(1f),
                testTag = "dash_nav_investments"
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionTile(
                icon = "🏢",
                title = "الشركات الوطنية",
                subtitle = "${state.companies.size} كيانات اقتصادية",
                onClick = { onNavigateTab(NavigationTab.COMPANIES) },
                modifier = Modifier.weight(1f),
                testTag = "dash_nav_companies"
            )
            ActionTile(
                icon = "🌐",
                title = "النفوذ والدبلوماسية",
                subtitle = "العلاقات مع 10 دول",
                onClick = { onNavigateTab(NavigationTab.INFLUENCE) },
                modifier = Modifier.weight(1f),
                testTag = "dash_nav_influence"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // National Pillars Indices (الصناعة، الطاقة، البنية، التعليم...)
        Text(
            text = "مؤشرات القطاعات الحيوية (الأركان الوطنية)",
            color = TextSecondary,
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
            Column(modifier = Modifier.padding(14.dp)) {
                SectorIndexBar(name = "🏭 الصناعة والتصنيع", score = player.industryIndex, color = TacticalCyan)
                Spacer(modifier = Modifier.height(8.dp))
                SectorIndexBar(name = "⚡ الطاقة والكهرباء", score = player.energyIndex, color = SovereignGold)
                Spacer(modifier = Modifier.height(8.dp))
                SectorIndexBar(name = "🚄 البنية التحتية واللوجستيات", score = player.infrastructureIndex, color = TacticalCyan)
                Spacer(modifier = Modifier.height(8.dp))
                SectorIndexBar(name = "🌾 الزراعة والأمن الغذائي", score = player.agricultureIndex, color = GrowthGreen)
                Spacer(modifier = Modifier.height(8.dp))
                SectorIndexBar(name = "🎓 التعليم والبحث والابتكار", score = player.educationIndex, color = DiplomaticPurple)
                Spacer(modifier = Modifier.height(8.dp))
                SectorIndexBar(name = "🛡️ القوة العسكرية والدفاعية", score = player.militaryIndex, color = CrisisRed)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Macroeconomic Health Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CommandSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CommandBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "📊 الصحة الاقتصادية ومؤشرات الاستقرار",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "معدل التضخم:", color = TextSecondary, fontSize = 12.sp)
                    Text(
                        text = "${String.format(Locale.US, "%.1f", player.inflationRate)}%",
                        color = if (player.inflationRate < 5.0) GrowthGreen else CrisisRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "معدل البطالة:", color = TextSecondary, fontSize = 12.sp)
                    Text(
                        text = "${String.format(Locale.US, "%.1f", player.unemploymentRate)}%",
                        color = if (player.unemploymentRate < 6.0) GrowthGreen else SovereignGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "الجاذبية الاستثمارية (FDI):", color = TextSecondary, fontSize = 12.sp)
                    Text(
                        text = "${player.investmentAttractiveness} / 100",
                        color = TacticalCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "الميزان التجاري:", color = TextSecondary, fontSize = 12.sp)
                    Text(
                        text = if (player.tradeSurplusBillions >= 0) "+$${String.format(Locale.US, "%.1f", player.tradeSurplusBillions)}B (فائض)" else "-$${String.format(Locale.US, "%.1f", -player.tradeSurplusBillions)}B (عجز)",
                        color = if (player.tradeSurplusBillions >= 0) GrowthGreen else CrisisRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun StatPill(
    label: String,
    value: String,
    sub: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(CommandDarkBg.copy(alpha = 0.7f))
            .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
            .padding(8.dp)
    ) {
        Column {
            Text(text = label, color = TextSecondary, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = accent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = sub, color = TextSecondary, fontSize = 9.sp)
        }
    }
}

@Composable
private fun ActionTile(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, CommandBorder, RoundedCornerShape(12.dp))
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = CommandSurfaceElevated),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = TextSecondary, fontSize = 10.sp)
        }
    }
}

@Composable
private fun SectorIndexBar(name: String, score: Int, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = name, color = TextPrimary, fontSize = 11.sp)
            Text(text = "$score / 100", color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { (score / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = CommandBorder
        )
    }
}
