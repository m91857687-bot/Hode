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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Country
import com.example.model.GameState
import com.example.model.MapFilterMode
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
import kotlin.math.sqrt

@Composable
fun TacticalWorldMap(
    state: GameState,
    onCountryClick: (Country) -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1.0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(0.7f, 3.5f)
        offset += offsetChange
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
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
        // Interactive Canvas Map
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = transformState)
                .pointerInput(state.countries, scale, offset) {
                    detectTapGestures { tapOffset ->
                        // Reverse transform to world space
                        val worldX = (tapOffset.x - offset.x) / (size.width * scale)
                        val worldY = (tapOffset.y - offset.y) / (size.height * scale)

                        // Find closest country within tap radius
                        var closestCountry: Country? = null
                        var minDistance = Float.MAX_VALUE

                        for (country in state.countries.values) {
                            val dx = worldX - country.mapX
                            val dy = worldY - country.mapY
                            val dist = sqrt(dx * dx + dy * dy)
                            if (dist < 0.08f && dist < minDistance) {
                                minDistance = dist
                                closestCountry = country
                            }
                        }

                        if (closestCountry != null) {
                            onCountryClick(closestCountry)
                        }
                    }
                }
        ) {
            val mapWidth = size.width * scale
            val mapHeight = size.height * scale
            val startX = offset.x
            val startY = offset.y

            // 1. Draw Tactical Map Grid
            val gridStepX = mapWidth / 12f
            val gridStepY = mapHeight / 8f
            for (i in 0..12) {
                val x = startX + i * gridStepX
                drawLine(
                    color = CommandBorder.copy(alpha = 0.25f),
                    start = Offset(x, startY),
                    end = Offset(x, startY + mapHeight),
                    strokeWidth = 1f
                )
            }
            for (j in 0..8) {
                val y = startY + j * gridStepY
                drawLine(
                    color = CommandBorder.copy(alpha = 0.25f),
                    start = Offset(startX, y),
                    end = Offset(startX + mapWidth, y),
                    strokeWidth = 1f
                )
            }

            // 2. Draw Continents Silhouettes & Landmasses (Stylized World Geography)
            drawWorldContinents(startX, startY, mapWidth, mapHeight)

            // 3. Draw Connecting Global Trade Routes between key nations
            val player = state.playerCountry
            if (player != null) {
                val pX = startX + player.mapX * mapWidth
                val pY = startY + player.mapY * mapHeight
                for (other in state.countries.values) {
                    if (other.id != player.id) {
                        val oX = startX + other.mapX * mapWidth
                        val oY = startY + other.mapY * mapHeight
                        val routeColor = when {
                            other.relationsWithPlayer >= 75 -> GrowthGreen.copy(alpha = 0.22f)
                            other.relationsWithPlayer <= 40 -> CrisisRed.copy(alpha = 0.15f)
                            else -> TacticalCyan.copy(alpha = 0.15f)
                        }
                        drawLine(
                            color = routeColor,
                            start = Offset(pX, pY),
                            end = Offset(oX, oY),
                            strokeWidth = 1.2f * scale.coerceIn(0.8f, 1.8f),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            // 4. Draw Country Strategic Nodes
            for (country in state.countries.values) {
                val cx = startX + country.mapX * mapWidth
                val cy = startY + country.mapY * mapHeight
                val isSelected = (country.id == state.selectedCountryIdForDossier)
                val isPlayer = (country.id == state.playerCountryId)

                val nodeColor = getCountryNodeColor(country, state)

                // Outer halo / influence glow
                val outerRadius = (if (isSelected) 28f else 18f) * scale.coerceIn(0.7f, 1.5f)
                drawCircle(
                    color = nodeColor.copy(alpha = if (isSelected) pulseAlpha * 0.45f else 0.20f),
                    radius = outerRadius,
                    center = Offset(cx, cy)
                )

                // Selection reticle ring
                if (isSelected) {
                    drawCircle(
                        color = TacticalCyan,
                        radius = outerRadius + 6f,
                        center = Offset(cx, cy),
                        style = Stroke(width = 2.5f)
                    )
                }

                // Inner core node
                val coreRadius = (if (isPlayer) 11f else 8f) * scale.coerceIn(0.7f, 1.5f)
                drawCircle(
                    color = nodeColor,
                    radius = coreRadius,
                    center = Offset(cx, cy)
                )
                drawCircle(
                    color = Color.White,
                    radius = coreRadius * 0.4f,
                    center = Offset(cx, cy)
                )

                // Text labels (Flag + Short Name)
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = (12f * scale.coerceIn(0.8f, 1.4f)).coerceIn(10f, 22f)
                        textAlign = android.graphics.Paint.Align.CENTER
                        isFakeBoldText = true
                        setShadowLayer(4f, 0f, 2f, android.graphics.Color.BLACK)
                    }
                    val label = "${country.flag} ${country.capital}"
                    drawText(label, cx, cy + outerRadius + 14f, paint)
                }
            }
        }

        // Floating Map Controls (Zoom In, Zoom Out, Center)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { scale = (scale * 1.3f).coerceAtMost(3.5f) },
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
                onClick = { scale = (scale / 1.3f).coerceAtLeast(0.7f) },
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

private fun getCountryNodeColor(country: Country, state: GameState): Color {
    val player = state.playerCountry
    return when (state.mapFilterMode) {
        MapFilterMode.POLITICAL -> {
            if (country.id == state.playerCountryId) TacticalCyan
            else when (country.id) {
                "SA" -> Color(0xFF00B074)
                "EG" -> Color(0xFFE5A93C)
                "AE" -> Color(0xFF29B6F6)
                "US" -> Color(0xFF3F51B5)
                "CN" -> Color(0xFFE53935)
                "DE" -> Color(0xFFFFA000)
                "JP" -> Color(0xFFE91E63)
                "GB" -> Color(0xFF5C6BC0)
                "IN" -> Color(0xFFFF9800)
                "BR" -> Color(0xFF4CAF50)
                "TR" -> Color(0xFFD32F2F)
                else -> Color.Gray
            }
        }
        MapFilterMode.GDP -> {
            when {
                country.gdpBillions >= 10000.0 -> TacticalCyan
                country.gdpBillions >= 3000.0 -> SovereignGold
                country.gdpBillions >= 1000.0 -> GrowthGreen
                else -> Color(0xFF90A4AE)
            }
        }
        MapFilterMode.PLAYER_INFLUENCE -> {
            if (country.id == state.playerCountryId) TacticalCyan
            else {
                val influence = state.influenceNetwork.find {
                    it.sourceCountryId == state.playerCountryId && it.targetCountryId == country.id
                }?.totalScore ?: 0
                when {
                    influence >= 80 -> TacticalCyan
                    influence >= 50 -> DiplomaticPurple
                    influence >= 25 -> SovereignGold
                    else -> Color(0xFF546E7A)
                }
            }
        }
        MapFilterMode.RELATIONS -> {
            if (country.id == state.playerCountryId) TacticalCyan
            else when {
                country.relationsWithPlayer >= 75 -> GrowthGreen
                country.relationsWithPlayer >= 50 -> SovereignGold
                else -> CrisisRed
            }
        }
    }
}

/**
 * Draws stylized vector polygons representing continents on canvas.
 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawWorldContinents(
    startX: Float,
    startY: Float,
    width: Float,
    height: Float
) {
    val landBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF131D2F), Color(0xFF0E1726)),
        start = Offset(startX, startY),
        end = Offset(startX + width, startY + height)
    )
    val coastStroke = Stroke(width = 1.4f)
    val coastColor = CommandBorder.copy(alpha = 0.65f)

    // North America
    val naPath = Path().apply {
        moveTo(startX + width * 0.12f, startY + height * 0.18f)
        lineTo(startX + width * 0.28f, startY + height * 0.18f)
        lineTo(startX + width * 0.32f, startY + height * 0.28f)
        lineTo(startX + width * 0.26f, startY + height * 0.44f)
        lineTo(startX + width * 0.20f, startY + height * 0.48f)
        lineTo(startX + width * 0.14f, startY + height * 0.36f)
        close()
    }
    drawPath(naPath, landBrush)
    drawPath(naPath, coastColor, style = coastStroke)

    // South America
    val saPath = Path().apply {
        moveTo(startX + width * 0.26f, startY + height * 0.52f)
        lineTo(startX + width * 0.38f, startY + height * 0.58f)
        lineTo(startX + width * 0.36f, startY + height * 0.78f)
        lineTo(startX + width * 0.30f, startY + height * 0.88f)
        lineTo(startX + width * 0.24f, startY + height * 0.70f)
        close()
    }
    drawPath(saPath, landBrush)
    drawPath(saPath, coastColor, style = coastStroke)

    // Europe
    val euPath = Path().apply {
        moveTo(startX + width * 0.44f, startY + height * 0.20f)
        lineTo(startX + width * 0.56f, startY + height * 0.18f)
        lineTo(startX + width * 0.56f, startY + height * 0.34f)
        lineTo(startX + width * 0.46f, startY + height * 0.36f)
        close()
    }
    drawPath(euPath, landBrush)
    drawPath(euPath, coastColor, style = coastStroke)

    // Africa
    val afPath = Path().apply {
        moveTo(startX + width * 0.46f, startY + height * 0.38f)
        lineTo(startX + width * 0.60f, startY + height * 0.38f)
        lineTo(startX + width * 0.62f, startY + height * 0.60f)
        lineTo(startX + width * 0.54f, startY + height * 0.80f)
        lineTo(startX + width * 0.48f, startY + height * 0.62f)
        close()
    }
    drawPath(afPath, landBrush)
    drawPath(afPath, coastColor, style = coastStroke)

    // Middle East & West Asia
    val mePath = Path().apply {
        moveTo(startX + width * 0.54f, startY + height * 0.38f)
        lineTo(startX + width * 0.64f, startY + height * 0.38f)
        lineTo(startX + width * 0.64f, startY + height * 0.52f)
        lineTo(startX + width * 0.56f, startY + height * 0.50f)
        close()
    }
    drawPath(mePath, landBrush)
    drawPath(mePath, coastColor, style = coastStroke)

    // Asia (East, South, Central)
    val asiaPath = Path().apply {
        moveTo(startX + width * 0.60f, startY + height * 0.16f)
        lineTo(startX + width * 0.88f, startY + height * 0.16f)
        lineTo(startX + width * 0.88f, startY + height * 0.45f)
        lineTo(startX + width * 0.76f, startY + height * 0.55f)
        lineTo(startX + width * 0.66f, startY + height * 0.52f)
        lineTo(startX + width * 0.60f, startY + height * 0.36f)
        close()
    }
    drawPath(asiaPath, landBrush)
    drawPath(asiaPath, coastColor, style = coastStroke)

    // Japan archipelago
    val jpPath = Path().apply {
        moveTo(startX + width * 0.88f, startY + height * 0.32f)
        lineTo(startX + width * 0.91f, startY + height * 0.35f)
        lineTo(startX + width * 0.89f, startY + height * 0.42f)
        close()
    }
    drawPath(jpPath, landBrush)
    drawPath(jpPath, coastColor, style = coastStroke)

    // Australia & Oceania
    val ausPath = Path().apply {
        moveTo(startX + width * 0.78f, startY + height * 0.66f)
        lineTo(startX + width * 0.90f, startY + height * 0.66f)
        lineTo(startX + width * 0.88f, startY + height * 0.84f)
        lineTo(startX + width * 0.78f, startY + height * 0.82f)
        close()
    }
    drawPath(ausPath, landBrush)
    drawPath(ausPath, coastColor, style = coastStroke)
}
