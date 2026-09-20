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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.model.GameState
import com.example.model.ResourceType
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
fun ResourceInventorySheet(
    state: GameState,
    onDismiss: () -> Unit,
    onBuyCommodity: (ResourceType, Double) -> Unit
) {
    val player = state.playerCountry ?: return

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 620.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.2.dp, TacticalCyan.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                .testTag("resource_inventory_dialog"),
            colors = CardDefaults.cardColors(containerColor = CommandDarkBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📦 المخزون الاستراتيجي والموارد",
                            color = SovereignGold,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "إدارة المخزونات القومية وتأمين سلاسل الإمداد",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    Text(
                        text = "الخزينة: $${String.format(Locale.US, "%.1f", player.treasuryBillions)}B",
                        color = TacticalCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Energy & Water Quick Summary Pill
                state.energyGrid?.let { energy ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CommandSurfaceElevated)
                            .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚡ الكهرباء: ${(energy.currentProductionGw).toInt()} GW / الطلب: ${(energy.totalDemandGw).toInt()} GW",
                                color = if (energy.hasShortage) Color(0xFFEF4444) else GrowthGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            state.waterGrid?.let { water ->
                                Text(
                                    text = "💧 المياه: ${(water.totalWaterSupplyDaily).toInt()} MCM إمداد",
                                    color = TacticalCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Resources list
                val resourceList = ResourceType.values().toList()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(resourceList) { res ->
                        val currentStockpile = state.resourceInventory[res.code] ?: 150.0
                        val countryRes = player.resources[res.name]
                        val monthlyProduction = countryRes?.currentProductionUnits ?: 0.0
                        val marketCommodity = state.marketCommodities[res.name]
                        val price = marketCommodity?.currentPrice ?: res.defaultBasePrice

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CommandSurface)
                                .border(1.dp, CommandBorder, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = res.icon, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = res.nameAr,
                                            color = TextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "المخزون: ${String.format(Locale.US, "%.0f", currentStockpile)}k ${res.unitLabelAr} • إنتاج: +${String.format(Locale.US, "%.0f", monthlyProduction)}k/شهر",
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "$${String.format(Locale.US, "%.1f", price)}",
                                            color = SovereignGold,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "السعر الفوري",
                                            color = TextSecondary,
                                            fontSize = 9.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = { onBuyCommodity(res, 50.0) },
                                        colors = ButtonDefaults.buttonColors(containerColor = TacticalCyan),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text(
                                            text = "+شراء",
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

                Spacer(modifier = Modifier.height(10.dp))

                // Close button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = CommandSurfaceElevated),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "إغلاق نافذة المخزون",
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
