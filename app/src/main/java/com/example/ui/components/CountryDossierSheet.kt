package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProjectTemplates
import com.example.model.Country
import com.example.model.GameState
import com.example.model.Project
import com.example.ui.theme.CommandBorder
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
fun CountryDossierSheet(
    country: Country,
    state: GameState,
    onDismiss: () -> Unit,
    onInvestInProject: (Project) -> Unit,
    onImproveRelations: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val player = state.playerCountry
    val isPlayer = (country.id == state.playerCountryId)

    // Player's influence in this country
    val playerInfluence = state.influenceNetwork.find {
        it.sourceCountryId == state.playerCountryId && it.targetCountryId == country.id
    }

    // Top foreign influencer in this country
    val foreignInfluencers = state.influenceNetwork.filter {
        it.targetCountryId == country.id && it.sourceCountryId != state.playerCountryId
    }.sortedByDescending { it.totalScore }
    val topForeign = foreignInfluencers.firstOrNull()
    val topForeignCountry = topForeign?.let { state.countries[it.sourceCountryId] }

    var showInvestmentCatalog by remember { mutableStateOf(false) }
    val foreignProjects: List<Project> = remember(country.id, state.playerCountryId) {
        val pid = state.playerCountryId
        if (!isPlayer && pid != null) {
            ProjectTemplates.getForeignProjectOpportunitiesForCountry(country.id, pid)
        } else {
            emptyList()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(CommandSurface)
            .border(1.dp, CommandBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("country_dossier_sheet")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = country.flag, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = country.nameAr,
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "العاصمة: ${country.capital} • ${country.aiArchetype.titleAr}",
                        color = TacticalCyan,
                        fontSize = 11.sp
                    )
                }
            }
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp).testTag("close_dossier_btn")
            ) {
                Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Key Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatBox(
                title = "الناتج المحلي",
                value = "$${String.format(Locale.US, "%.0f", country.gdpBillions)}B",
                subtitle = "+${String.format(Locale.US, "%.1f", country.gdpGrowthPercent)}%",
                accentColor = TacticalCyan,
                modifier = Modifier.weight(1f)
            )
            StatBox(
                title = "السكان",
                value = "${String.format(Locale.US, "%.1f", country.populationMillions)}M",
                subtitle = "نسمة",
                accentColor = SovereignGold,
                modifier = Modifier.weight(1f)
            )
            StatBox(
                title = "قوة الدولة",
                value = "⭐ ${country.nationalPowerScore}",
                subtitle = "/ 100",
                accentColor = GrowthGreen,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatBox(
                title = "الخزينة السيادية",
                value = "$${String.format(Locale.US, "%.1f", country.treasuryBillions)}B",
                subtitle = "احتياطي",
                accentColor = SovereignGold,
                modifier = Modifier.weight(1f)
            )
            StatBox(
                title = "الدين العام",
                value = "$${String.format(Locale.US, "%.0f", country.sovereignDebtBillions)}B",
                subtitle = "سندات",
                accentColor = CrisisRed,
                modifier = Modifier.weight(1f)
            )
            StatBox(
                title = "الاستقرار العام",
                value = "%${country.stability}",
                subtitle = "داخلي",
                accentColor = TacticalCyan,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Strategic Resources Badges
        Text(
            text = "الموارد والقطاعات الرئيسية",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            country.mainResources.forEach { res ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CommandSurfaceElevated)
                        .border(1.dp, CommandBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = res, color = TextPrimary, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Influence & Relations (for other nations)
        if (!isPlayer) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CommandSurfaceElevated)
                    .border(1.dp, CommandBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "العلاقات الثنائية مع دولتك",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = when {
                                country.relationsWithPlayer >= 75 -> "حليف استراتيجي (${country.relationsWithPlayer}%)"
                                country.relationsWithPlayer >= 50 -> "علاقات ودية متوازنة (${country.relationsWithPlayer}%)"
                                else -> "توتر ومنافسة (${country.relationsWithPlayer}%)"
                            },
                            color = if (country.relationsWithPlayer >= 50) GrowthGreen else CrisisRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Player influence breakdown
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🌐 نفوذك في هذه الدولة:",
                            color = TacticalCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "إجمالي ${playerInfluence?.totalScore ?: 0} نقطة",
                            color = TacticalCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "💰 اقتصادي: ${playerInfluence?.economicInfluence ?: 0} | 🏭 تجاري: ${playerInfluence?.commercialInfluence ?: 0} | 🤝 دبلوماسي: ${playerInfluence?.diplomaticInfluence ?: 0}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    if (topForeignCountry != null && topForeign != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "الأقوى تأثيراً أجنبياً: ${topForeignCountry.flag} ${topForeignCountry.nameAr} (${topForeign.totalScore} نقطة)",
                            color = SovereignGold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showInvestmentCatalog = !showInvestmentCatalog },
                    colors = ButtonDefaults.buttonColors(containerColor = TacticalCyan),
                    modifier = Modifier.weight(1f).testTag("invest_foreign_btn")
                ) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (showInvestmentCatalog) "إخفاء الفرص" else "استثمار خارجي", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { onImproveRelations(country.id) },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DiplomaticPurple),
                    modifier = Modifier.weight(1f).testTag("boost_relations_btn")
                ) {
                    Icon(Icons.Default.Handshake, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "تعزيز العلاقات ($0.5B)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Investment Opportunities Catalog
            if (showInvestmentCatalog && foreignProjects.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "فرص الاستثمار المتاحة في ${country.nameAr}",
                    color = TacticalCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                foreignProjects.forEach { proj ->
                    ForeignProjectCard(
                        project = proj,
                        playerTreasury = player?.treasuryBillions ?: 0.0,
                        onInvest = { onInvestInProject(proj) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun StatBox(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(CommandSurfaceElevated)
            .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
            .padding(8.dp)
    ) {
        Column {
            Text(text = title, color = TextSecondary, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = accentColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = TextSecondary, fontSize = 10.sp)
        }
    }
}

@Composable
private fun ForeignProjectCard(
    project: Project,
    playerTreasury: Double,
    onInvest: () -> Unit
) {
    val canAfford = playerTreasury >= project.totalCostBillions

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(CommandSurfaceElevated)
            .border(1.dp, TacticalCyan.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${project.category.icon} ${project.titleAr}",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "التكلفة: $${project.totalCostBillions}B",
                    color = SovereignGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = project.descriptionAr,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "العائد: ${project.annualReturnPercent}% • مدة: ${project.durationMonths} أشهر • النفوذ: +${project.influenceGain}",
                    color = GrowthGreen,
                    fontSize = 11.sp
                )
                Button(
                    onClick = onInvest,
                    enabled = canAfford,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TacticalCyan,
                        disabledContainerColor = CommandBorder
                    ),
                    modifier = Modifier.testTag("invest_action_${project.id}")
                ) {
                    Text(
                        text = if (canAfford) "استثمار وتملك" else "سيولة غير كافية",
                        color = if (canAfford) Color.Black else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
