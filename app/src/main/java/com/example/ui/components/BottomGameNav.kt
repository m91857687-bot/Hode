package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.NavigationTab
import com.example.ui.theme.CommandBorder
import com.example.ui.theme.CommandSurface
import com.example.ui.theme.TacticalCyan
import com.example.ui.theme.TextSecondary

@Composable
fun BottomGameNav(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryTabs = listOf(
        NavigationTab.MAP,
        NavigationTab.GOVERNMENT,
        NavigationTab.ECONOMY,
        NavigationTab.INDUSTRY,
        NavigationTab.RESEARCH,
        NavigationTab.INVESTMENTS,
        NavigationTab.MILITARY,
        NavigationTab.DIPLOMACY_TRADE
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(CommandSurface)
            .border(1.dp, CommandBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .horizontalScroll(rememberScrollState())
            .testTag("bottom_game_nav"),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        primaryTabs.forEach { tab ->
            val isSelected = (currentTab == tab)
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) TacticalCyan.copy(alpha = 0.15f) else Color.Transparent)
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("nav_tab_${tab.name}"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = tab.icon, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = tab.titleAr,
                        color = if (isSelected) TacticalCyan else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
