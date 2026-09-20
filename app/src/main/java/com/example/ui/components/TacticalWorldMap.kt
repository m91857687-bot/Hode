package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.map.CountryHitTester
import com.example.map.WorldMapRenderer
import com.example.map.WorldMapRepository
import com.example.model.Country
import com.example.model.GameState
import com.example.model.MapFilterMode
import com.example.ui.theme.CommandBorder
import com.example.ui.theme.CommandDarkBg
import com.example.ui.theme.CommandSurfaceElevated
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.TacticalCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TacticalWorldMap(
    state: GameState,
    onCountryClick: (Country) -> Unit,
    onFilterModeChange: ((MapFilterMode) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1.0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val polygons = remember { WorldMapRepository.getAllPolygons() }

    val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(0.7f, 3.8f)
        offset += offsetChange
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.88f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CommandDarkBg)
            .testTag("tactical_world_map_container")
    ) {
        // 1. Interactive Vector Canvas Map
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = transformState)
                .pointerInput(state.countries, scale, offset) {
                    detectTapGestures { tapOffset ->
                        // Reverse transform screen offset to normalized (0..1) world space
                        val worldX = (tapOffset.x - offset.x) / (size.width * scale)
                        val worldY = (tapOffset.y - offset.y) / (size.height * scale)

                        val hitIso3 = CountryHitTester.findHitCountryIso3(
                            normalizedTap = Offset(worldX, worldY),
                            polygons = polygons
                        )

                        if (hitIso3 != null) {
                            val country = state.countries[hitIso3]
                            if (country != null) {
                                onCountryClick(country)
                            }
                        }
                    }
                }
        ) {
            WorldMapRenderer.render(
                scope = this,
                polygons = polygons,
                state = state,
                selectedCountryId = state.selectedCountryId,
                scale = scale,
                offset = offset,
                pulseAlpha = pulseAlpha
            )
        }

        // 2. Map Filter Mode Bar (Top Horizontal Scroll)
        if (onFilterModeChange != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState())
                    .testTag("map_filter_bar")
            ) {
                MapFilterMode.values().forEach { mode ->
                    val isSelected = state.mapFilterMode == mode
                    FilterChip(
                        selected = isSelected,
                        onClick = { onFilterModeChange(mode) },
                        label = {
                            Text(
                                text = mode.labelAr,
                                fontSize = 11.sp,
                                color = if (isSelected) TacticalCyan else TextSecondary
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = CommandSurfaceElevated.copy(alpha = 0.85f),
                            selectedContainerColor = TacticalCyan.copy(alpha = 0.20f)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) TacticalCyan else CommandBorder,
                            selectedBorderColor = TacticalCyan,
                            enabled = true,
                            selected = isSelected
                        ),
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
            }
        }

        // 3. Floating Map Controls (Zoom In, Zoom Out, Reset Center)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { scale = (scale * 1.25f).coerceAtMost(3.8f) },
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(CommandSurfaceElevated)
                    .border(1.dp, CommandBorder, CircleShape)
                    .testTag("map_zoom_in_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = "تكبير", tint = TacticalCyan)
            }
            Spacer(modifier = Modifier.height(8.dp))
            IconButton(
                onClick = { scale = (scale / 1.25f).coerceAtLeast(0.7f) },
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(CommandSurfaceElevated)
                    .border(1.dp, CommandBorder, CircleShape)
                    .testTag("map_zoom_out_btn")
            ) {
                Icon(Icons.Default.Remove, contentDescription = "تصغير", tint = TacticalCyan)
            }
            Spacer(modifier = Modifier.height(8.dp))
            IconButton(
                onClick = {
                    scale = 1.0f
                    offset = Offset.Zero
                },
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(CommandSurfaceElevated)
                    .border(1.dp, CommandBorder, CircleShape)
                    .testTag("map_reset_center_btn")
            ) {
                Icon(Icons.Default.CenterFocusStrong, contentDescription = "إعادة ضبط", tint = SovereignGold)
            }
        }
    }
}
