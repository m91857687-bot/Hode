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
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.model.Country
import com.example.model.GameState
import com.example.model.InfluenceEntry
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

@Composable
fun InfluenceScreen(
    state: GameState,
    onImproveRelations: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val player = state.playerCountry ?: return

    val playerEntries = state.influenceNetwork.filter { it.sourceCountryId == player.id }
    val totalScore = playerEntries.sumOf { it.totalScore }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CommandDarkBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("influence_screen")
    ) {
        // Global Influence Header Card with 5 Pillars
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CommandSurfaceElevated)
                .border(1.2.dp, DiplomaticPurple.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🌐 شبكة النفوذ الجيوسياسي والدبلوماسي",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "القوة الشاملة: ${player.nationalPowerScore}/100 • القوة الناعمة: ${player.softPowerScore}/100",
                            color = TacticalCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(DiplomaticPurple.copy(alpha = 0.2f))
                            .border(1.dp, DiplomaticPurple, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "$totalScore نقطة تأثير",
                            color = SovereignGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 5 Pillars Breakdown
                Text(
                    text = "الأركان الخمسة للنفوذ الدولي لدولتك:",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    PillarBadge(name = "اقتصادي", value = player.economicInfluence, color = TacticalCyan, modifier = Modifier.weight(1f))
                    PillarBadge(name = "تجاري", value = player.tradeInfluence, color = SovereignGold, modifier = Modifier.weight(1f))
                    PillarBadge(name = "دبلوماسي", value = player.diplomaticInfluence, color = DiplomaticPurple, modifier = Modifier.weight(1f))
                    PillarBadge(name = "استراتيجي", value = player.strategicInfluence, color = GrowthGreen, modifier = Modifier.weight(1f))
                    PillarBadge(name = "ثقافي", value = player.culturalInfluence, color = Color(0xFFFF4081), modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // History Track summary
        if (state.influenceHistory.isNotEmpty()) {
            val lastPoints = state.influenceHistory.takeLast(4)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CommandSurface)
                    .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "📈 مسار تطور النفوذ مؤخراً:", color = TextSecondary, fontSize = 11.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        lastPoints.forEach { pt ->
                            Text(
                                text = "${pt.month}/${pt.year.toString().takeLast(2)}: ${pt.totalScore}",
                                color = SovereignGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        Text(
            text = "مصفوفة النفوذ والعلاقات الثنائية مع دول العالم",
            color = TacticalCyan,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        state.countries.values.filter { it.id != player.id }.forEach { country ->
            val entry = playerEntries.find { it.targetCountryId == country.id }
            BilateralCountryCard(
                country = country,
                influence = entry,
                canAfford = player.treasuryBillions >= 0.6,
                onImprove = { onImproveRelations(country.id) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun PillarBadge(
    name: String,
    value: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CommandDarkBg)
            .border(0.8.dp, color.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(vertical = 6.dp, horizontal = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = name, color = TextSecondary, fontSize = 9.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "$value", color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun BilateralCountryCard(
    country: Country,
    influence: InfluenceEntry?,
    canAfford: Boolean,
    onImprove: () -> Unit
) {
    val total = influence?.totalScore ?: 0
    val econ = influence?.economicInfluence ?: 0
    val comm = influence?.commercialInfluence ?: 0
    val dipl = influence?.diplomaticInfluence ?: 0
    val strat = influence?.strategicInfluence ?: 0
    val cult = influence?.culturalInfluence ?: 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CommandBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = country.flag, fontSize = 26.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = country.nameAr,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${country.capital} • ${country.aiArchetype.titleAr}",
                            color = TacticalCyan,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CommandSurfaceElevated)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "نفوذك: $total نقطة",
                        color = SovereignGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Relations progress bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "مستوى العلاقات الثنائية:", color = TextSecondary, fontSize = 11.sp)
                Text(
                    text = "${country.relationStatus.titleAr} (${country.relationsWithPlayer}%)",
                    color = Color(country.relationStatus.colorHex),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { (country.relationsWithPlayer / 100f).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp)),
                color = Color(country.relationStatus.colorHex),
                trackColor = CommandBorder
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Influence 5 pillars display
            Text(
                text = "💰 اقتصادي: $econ | 🚢 تجاري: $comm | 🤝 دبلوماسي: $dipl | 🛡️ استراتيجي: $strat | 🎭 ثقافي: $cult",
                color = TextSecondary,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onImprove,
                    enabled = canAfford && country.relationsWithPlayer < 95,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DiplomaticPurple,
                        disabledContainerColor = CommandBorder
                    ),
                    modifier = Modifier.testTag("improve_relations_${country.id}")
                ) {
                    Icon(Icons.Default.Handshake, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "تعزيز العلاقات واستثمار النفوذ ($0.6B)", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
