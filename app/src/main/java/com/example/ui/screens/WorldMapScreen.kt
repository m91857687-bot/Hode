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
import androidx.compose.foundation.layout.size
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
import com.example.model.Country
import com.example.model.GameState
import com.example.model.MapFilterMode
import com.example.model.Project
import com.example.ui.components.CountryDossierSheet
import com.example.ui.components.TacticalWorldMap
import com.example.ui.theme.CommandBorder
import com.example.ui.theme.CommandSurface
import com.example.ui.theme.TacticalCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WorldMapScreen(
    state: GameState,
    onCountryClick: (Country) -> Unit,
    onFilterChange: (MapFilterMode) -> Unit,
    onDismissDossier: () -> Unit,
    onInvestInProject: (Project) -> Unit,
    onImproveRelations: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCountry = state.selectedCountryIdForDossier?.let { state.countries[it] }

    Box(modifier = modifier.fillMaxSize().testTag("world_map_screen")) {
        // Tactical Map Canvas
        TacticalWorldMap(
            state = state,
            onCountryClick = onCountryClick,
            modifier = Modifier.fillMaxSize()
        )

        // Map Filter Layer Chips (Top Overlay below HUD)
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(CommandSurface.copy(alpha = 0.90f))
                .border(1.dp, CommandBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 6.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MapFilterMode.values().forEach { mode ->
                val isSelected = (state.mapFilterMode == mode)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) TacticalCyan else Color.Transparent)
                        .clickable { onFilterChange(mode) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("map_filter_${mode.name}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mode.titleAr,
                        color = if (isSelected) Color.Black else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Country Dossier Sheet Overlay
        if (selectedCountry != null) {
            CountryDossierSheet(
                country = selectedCountry,
                state = state,
                onDismiss = onDismissDossier,
                onInvestInProject = onInvestInProject,
                onImproveRelations = onImproveRelations,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}
