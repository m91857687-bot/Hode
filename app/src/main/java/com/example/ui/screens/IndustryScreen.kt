package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.model.FactoryType
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
fun IndustryScreen(
    state: GameState,
    onUpgradeFactory: (String) -> Unit,
    onPrivatizeFactory: (String) -> Unit,
    onBuildFactory: (FactoryType) -> Unit,
    modifier: Modifier = Modifier
) {
    val player = state.playerCountry ?: return
    val energy = state.energyGrid
    val water = state.waterGrid
    val labor = state.laborMarket

    var selectedSegment by remember { mutableStateOf(0) } // 0: Factories & Production, 1: Energy & Water, 2: Labor Market

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CommandDarkBg)
            .padding(14.dp)
            .verticalScroll(rememberScrollState())
            .testTag("industry_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "🏭 مجمع الصناعات الوطنية والطاقة",
                    color = SovereignGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "التصنيع المتقدم، شبكات الطاقة والمياه، وسوق العمل",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
            Text(
                text = "مؤشر الصناعة: ${player.industryIndex}/100",
                color = TacticalCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Segment Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(CommandSurfaceElevated)
                .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IndustryTabButton("المصانع والإنتاج", selectedSegment == 0) { selectedSegment = 0 }
            IndustryTabButton("الطاقة والمياه", selectedSegment == 1) { selectedSegment = 1 }
            IndustryTabButton("سوق العمل والوظائف", selectedSegment == 2) { selectedSegment = 2 }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedSegment) {
            0 -> {
                // Factories List
                val factories = player.factories
                if (factories.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CommandSurface)
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "لا توجد مصانع نشطة حالياً. يمكنك تأسيس أول مجمع صناعي!", color = TextSecondary, fontSize = 12.sp)
                    }
                } else {
                    factories.forEach { factory ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(CommandSurface)
                                .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = factory.type.icon, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = factory.nameAr,
                                                color = TextPrimary,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "مستوى ${factory.level} • عمال: ${factory.actualWorkers} • كفاءة: ${factory.efficiencyPercent}%",
                                                color = TextSecondary,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }

                                    Text(
                                        text = "+$${String.format(Locale.US, "%.1f", factory.monthlyGrossProfitMillions)}M",
                                        color = GrowthGreen,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedButton(
                                        onClick = { onPrivatizeFactory(factory.id) },
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text(text = "خصخصة 49%", color = SovereignGold, fontSize = 10.sp)
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Button(
                                        onClick = { onUpgradeFactory(factory.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = TacticalCyan),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text(text = "ترقية المصنع", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                // Quick Build Factory Buttons
                Text(text = "🏗️ تأسيس مجمعات تصنيع جديدة:", color = SovereignGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = { onBuildFactory(FactoryType.SEMICONDUCTOR_FAB) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CommandSurfaceElevated),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "⚡ رقائق", fontSize = 10.sp, color = TextPrimary)
                    }
                    Button(
                        onClick = { onBuildFactory(FactoryType.STEEL_MILL) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CommandSurfaceElevated),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "🏗️ صلب", fontSize = 10.sp, color = TextPrimary)
                    }
                    Button(
                        onClick = { onBuildFactory(FactoryType.OIL_REFINERY) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CommandSurfaceElevated),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "🛢️ تكرير", fontSize = 10.sp, color = TextPrimary)
                    }
                }
            }
            1 -> {
                // Energy & Water Status
                energy?.let { e ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CommandSurface)
                            .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "⚡ شبكة الكهرباء الوطنية", color = TacticalCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "الإنتاج: ${(e.currentProductionGw).toInt()} GW", color = TextPrimary, fontSize = 12.sp)
                                Text(text = "الطلب: ${(e.totalDemandGw).toInt()} GW", color = TextSecondary, fontSize = 12.sp)
                                Text(
                                    text = if (e.hasShortage) "عجز ${(e.shortagePercentage).toInt()}%" else "فائض آمن",
                                    color = if (e.hasShortage) CrisisRed else GrowthGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            LinearProgressIndicator(
                                progress = (e.currentProductionGw / (e.totalDemandGw.coerceAtLeast(1.0))).toFloat().coerceIn(0f, 1f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (e.hasShortage) CrisisRed else TacticalCyan,
                                trackColor = CommandSurfaceElevated
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                water?.let { w ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CommandSurface)
                            .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(text = "💧 الأمن المائي والتحلية", color = SovereignGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "الإمداد: ${(w.totalWaterSupplyDaily).toInt()} MCM", color = TextPrimary, fontSize = 12.sp)
                                Text(text = "الطلب: ${(w.totalDemandDaily).toInt()} MCM", color = TextSecondary, fontSize = 12.sp)
                                Text(
                                    text = if (w.hasWaterShortage) "شح مائي" else "مستقر",
                                    color = if (w.hasWaterShortage) CrisisRed else GrowthGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
            2 -> {
                // Labor Market
                labor?.let { l ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CommandSurface)
                            .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "👥 مؤشرات سوق العمل والعمالة", color = SovereignGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "القوة العاملة: ${String.format(Locale.US, "%.1f", l.demographics.laborForceMillions)}M نسمة", color = TextSecondary, fontSize = 12.sp)
                                Text(text = "معدل البطالة: ${String.format(Locale.US, "%.1f", l.demographics.unemploymentRatePercent)}%", color = if (l.demographics.unemploymentRatePercent > 10) CrisisRed else GrowthGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "متوسط الأجور: $${l.nationalAverageWage.toInt()}/شهر", color = TextPrimary, fontSize = 12.sp)
                                Text(text = "رضا العمال: ${l.demographics.overallSatisfactionPercent}%", color = TacticalCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IndustryTabButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) TacticalCyan else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.Black else TextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
