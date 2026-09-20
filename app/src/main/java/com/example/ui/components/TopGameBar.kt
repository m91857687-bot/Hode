package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.model.GameSpeed
import com.example.model.GameState
import com.example.ui.theme.CommandBorder
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
fun TopGameBar(
    state: GameState,
    onSpeedChange: (GameSpeed) -> Unit,
    onAdvanceMonth: () -> Unit,
    onOpenDossier: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val player = state.playerCountry ?: return
    val budget = GameEngine.calculateMonthlyBudget(player, state.companies, state.foreignInvestments)
    val monthlyCashflow = budget.netCashflow

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CommandSurface)
            .border(1.dp, CommandBorder, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("top_game_bar")
    ) {
        // Upper row: Country & National stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player Country Flag & Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(CommandSurfaceElevated)
                    .clickable { onOpenDossier(player.id) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("player_country_badge")
            ) {
                Text(text = player.flag, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = player.nameAr,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = "⭐ قوة الدولة: ${player.nationalPowerScore}",
                        color = SovereignGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Date & Calendar
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(TacticalCyan.copy(alpha = 0.12f))
                    .border(1.dp, TacticalCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "🗓️ ${state.dateFormatted}",
                    color = TacticalCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Lower row: Economy HUD & Speed controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Economic Indicators (Treasury & GDP)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Treasury
                Column(modifier = Modifier.padding(end = 12.dp)) {
                    Text(
                        text = "الخزينة",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$${String.format(Locale.US, "%.1f", player.treasuryBillions)}B",
                            color = SovereignGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (monthlyCashflow >= 0) "+$${String.format(Locale.US, "%.1f", monthlyCashflow)}" else "-$${String.format(Locale.US, "%.1f", -monthlyCashflow)}",
                            color = if (monthlyCashflow >= 0) GrowthGreen else CrisisRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // GDP
                Column {
                    Text(
                        text = "الناتج المحلي (GDP)",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$${String.format(Locale.US, "%.0f", player.gdpBillions)}B",
                            color = TacticalCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+${String.format(Locale.US, "%.1f", player.gdpGrowthPercent)}%",
                            color = GrowthGreen,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            // Speed Control Buttons (Pause, 1x, 2x, 5x, Next Month manual)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(CommandSurfaceElevated)
                    .border(1.dp, CommandBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                SpeedButton(
                    isSelected = state.gameSpeed == GameSpeed.PAUSED,
                    label = "⏸",
                    onClick = { onSpeedChange(GameSpeed.PAUSED) },
                    testTag = "speed_pause_btn"
                )
                SpeedButton(
                    isSelected = state.gameSpeed == GameSpeed.SPEED_1X,
                    label = "1x",
                    onClick = { onSpeedChange(GameSpeed.SPEED_1X) },
                    testTag = "speed_1x_btn"
                )
                SpeedButton(
                    isSelected = state.gameSpeed == GameSpeed.SPEED_2X,
                    label = "2x",
                    onClick = { onSpeedChange(GameSpeed.SPEED_2X) },
                    testTag = "speed_2x_btn"
                )
                SpeedButton(
                    isSelected = state.gameSpeed == GameSpeed.SPEED_5X,
                    label = "5x",
                    onClick = { onSpeedChange(GameSpeed.SPEED_5X) },
                    testTag = "speed_5x_btn"
                )
                // Step month button
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable { onAdvanceMonth() }
                        .padding(4.dp)
                        .testTag("step_month_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "التقدم شهر",
                        tint = TacticalCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SpeedButton(
    isSelected: Boolean,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) TacticalCyan else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.Black else TextSecondary
        )
    }
}
