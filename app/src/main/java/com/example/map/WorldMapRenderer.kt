package com.example.map

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import com.example.model.Country
import com.example.model.DiplomaticRelationStatus
import com.example.model.EconomicTier
import com.example.model.GameState
import com.example.model.MapFilterMode
import com.example.ui.theme.CommandBorder
import com.example.ui.theme.CommandDarkBg
import com.example.ui.theme.CrisisRed
import com.example.ui.theme.DiplomaticPurple
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.TacticalCyan

object WorldMapRenderer {

    fun render(
        scope: DrawScope,
        polygons: List<CountryPolygon>,
        state: GameState,
        selectedCountryId: String?,
        scale: Float,
        offset: Offset,
        pulseAlpha: Float
    ) {
        val mapWidth = scope.size.width * scale
        val mapHeight = scope.size.height * scale
        val startX = offset.x
        val startY = offset.y

        // 1. Draw Grid Lines
        val gridStepX = mapWidth / 18f
        val gridStepY = mapHeight / 10f
        for (i in 0..18) {
            val x = startX + i * gridStepX
            scope.drawLine(
                color = CommandBorder.copy(alpha = 0.18f),
                start = Offset(x, startY),
                end = Offset(x, startY + mapHeight),
                strokeWidth = 1f
            )
        }
        for (j in 0..10) {
            val y = startY + j * gridStepY
            scope.drawLine(
                color = CommandBorder.copy(alpha = 0.18f),
                start = Offset(startX, y),
                end = Offset(startX + mapWidth, y),
                strokeWidth = 1f
            )
        }

        // 2. Render Country Polygons
        for (poly in polygons) {
            val country = state.countries[poly.iso3]
            val isSelected = poly.iso3 == selectedCountryId
            val isPlayer = poly.iso3 == state.playerCountryId

            val fillColor = getCountryColor(country, state.mapFilterMode, isPlayer)
            val borderColor = if (isSelected) {
                SovereignGold.copy(alpha = 0.95f)
            } else if (isPlayer) {
                TacticalCyan.copy(alpha = 0.90f)
            } else {
                CommandBorder.copy(alpha = 0.55f)
            }

            for (ring in poly.rings) {
                if (ring.isEmpty()) continue
                val path = Path().apply {
                    val first = ring[0]
                    moveTo(startX + first.x * mapWidth, startY + first.y * mapHeight)
                    for (k in 1 until ring.size) {
                        val pt = ring[k]
                        lineTo(startX + pt.x * mapWidth, startY + pt.y * mapHeight)
                    }
                    close()
                }

                // Fill polygon
                scope.drawPath(path = path, color = fillColor, style = Fill)

                // Stroke border
                val strokeWidth = if (isSelected) 3.5f else if (isPlayer) 2.5f else 1.2f
                scope.drawPath(
                    path = path,
                    color = borderColor,
                    style = Stroke(width = strokeWidth)
                )

                if (isSelected) {
                    // Pulsing selection glow
                    scope.drawPath(
                        path = path,
                        color = SovereignGold.copy(alpha = pulseAlpha * 0.4f),
                        style = Stroke(width = strokeWidth + 4f)
                    )
                }
            }
        }

        // 3. Render Labels on Centroids
        val labelPaint = Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = (11f * scale).coerceIn(12f, 22f)
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
            setShadowLayer(4f, 0f, 0f, android.graphics.Color.BLACK)
        }

        val flagPaint = Paint().apply {
            textSize = (14f * scale).coerceIn(14f, 28f)
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        for (poly in polygons) {
            val country = state.countries[poly.iso3] ?: continue
            val isSelected = poly.iso3 == selectedCountryId
            val isPlayer = poly.iso3 == state.playerCountryId

            val cx = startX + poly.centroid.x * mapWidth
            val cy = startY + poly.centroid.y * mapHeight

            // Only draw text if visible on screen and scale is adequate (or if selected / player)
            if (scale >= 1.2f || isSelected || isPlayer) {
                val flag = country.flag
                val name = country.nameAr

                scope.drawContext.canvas.nativeCanvas.drawText(flag, cx, cy - 6f, flagPaint)
                if (scale >= 1.5f || isSelected || isPlayer) {
                    scope.drawContext.canvas.nativeCanvas.drawText(name, cx, cy + 16f, labelPaint)
                }
            }
        }
    }

    private fun getCountryColor(country: Country?, mode: MapFilterMode, isPlayer: Boolean): Color {
        if (country == null) {
            return Color(0xFF1E2638).copy(alpha = 0.7f)
        }

        if (isPlayer && mode == MapFilterMode.POLITICAL) {
            return TacticalCyan.copy(alpha = 0.45f)
        }

        return when (mode) {
            MapFilterMode.POLITICAL -> {
                when (country.id) {
                    "SAU", "ARE", "QAT", "KWT", "OMN" -> SovereignGold.copy(alpha = 0.35f)
                    "YEM", "IRQ", "SYR", "LBN", "PSE" -> Color(0xFF7E57C2).copy(alpha = 0.35f)
                    "EGY", "LBY", "TUN", "DZA", "MAR", "SDN" -> Color(0xFF26A69A).copy(alpha = 0.35f)
                    "USA", "GBR", "FRA", "DEU" -> Color(0xFF42A5F5).copy(alpha = 0.35f)
                    "CHN", "RUS" -> CrisisRed.copy(alpha = 0.35f)
                    else -> Color(0xFF37474F).copy(alpha = 0.5f)
                }
            }
            MapFilterMode.GDP -> {
                when {
                    country.gdpBillions >= 2000.0 -> GrowthGreen.copy(alpha = 0.65f)
                    country.gdpBillions >= 800.0 -> GrowthGreen.copy(alpha = 0.45f)
                    country.gdpBillions >= 300.0 -> SovereignGold.copy(alpha = 0.45f)
                    country.gdpBillions >= 100.0 -> Color(0xFFFF9800).copy(alpha = 0.40f)
                    else -> Color(0xFF546E7A).copy(alpha = 0.45f)
                }
            }
            MapFilterMode.ECONOMIC_TIER -> {
                when (country.tier) {
                    EconomicTier.GLOBAL_POWER -> GrowthGreen.copy(alpha = 0.65f)
                    EconomicTier.MAJOR -> TacticalCyan.copy(alpha = 0.55f)
                    EconomicTier.LARGE -> SovereignGold.copy(alpha = 0.50f)
                    EconomicTier.MEDIUM -> DiplomaticPurple.copy(alpha = 0.45f)
                    EconomicTier.SMALL -> Color(0xFF78909C).copy(alpha = 0.45f)
                    EconomicTier.MICRO -> Color(0xFF455A64).copy(alpha = 0.45f)
                }
            }
            MapFilterMode.RESOURCES -> {
                if (country.energyIndex > 65) {
                    Color(0xFF00B0FF).copy(alpha = 0.50f) // Energy Oil/Gas
                } else if (country.industryIndex > 65) {
                    Color(0xFFFFB300).copy(alpha = 0.50f) // Metals/Industry
                } else if (country.agricultureIndex > 55) {
                    Color(0xFF66BB6A).copy(alpha = 0.50f) // Agriculture
                } else {
                    Color(0xFF546E7A).copy(alpha = 0.35f)
                }
            }
            MapFilterMode.STABILITY -> {
                when {
                    country.stability >= 75 -> GrowthGreen.copy(alpha = 0.55f)
                    country.stability >= 55 -> SovereignGold.copy(alpha = 0.45f)
                    country.stability >= 35 -> Color(0xFFFF9100).copy(alpha = 0.45f)
                    else -> CrisisRed.copy(alpha = 0.55f)
                }
            }
            MapFilterMode.PLAYER_INFLUENCE -> {
                val inf = country.economicInfluence + country.diplomaticInfluence
                when {
                    inf >= 130 -> TacticalCyan.copy(alpha = 0.65f)
                    inf >= 100 -> TacticalCyan.copy(alpha = 0.45f)
                    inf >= 70 -> DiplomaticPurple.copy(alpha = 0.40f)
                    else -> Color(0xFF263238).copy(alpha = 0.45f)
                }
            }
            MapFilterMode.RELATIONS -> {
                when (country.relationStatus) {
                    DiplomaticRelationStatus.FRIENDLY -> GrowthGreen.copy(alpha = 0.60f)
                    DiplomaticRelationStatus.COOPERATIVE -> TacticalCyan.copy(alpha = 0.50f)
                    DiplomaticRelationStatus.NEUTRAL -> SovereignGold.copy(alpha = 0.40f)
                    DiplomaticRelationStatus.TENSE -> Color(0xFFFF9100).copy(alpha = 0.45f)
                    DiplomaticRelationStatus.HOSTILE -> CrisisRed.copy(alpha = 0.55f)
                }
            }
        }
    }
}
