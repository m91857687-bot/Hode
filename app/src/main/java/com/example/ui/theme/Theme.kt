package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TacticalDarkColorScheme = darkColorScheme(
    primary = TacticalCyan,
    onPrimary = Color.Black,
    primaryContainer = TacticalCyanDark,
    onPrimaryContainer = Color.White,
    secondary = SovereignGold,
    onSecondary = Color.Black,
    secondaryContainer = SovereignGoldMuted,
    onSecondaryContainer = Color.White,
    tertiary = GrowthGreen,
    onTertiary = Color.Black,
    background = CommandDarkBg,
    onBackground = TextPrimary,
    surface = CommandSurface,
    onSurface = TextPrimary,
    surfaceVariant = CommandSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = CommandBorder,
    error = CrisisRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TacticalDarkColorScheme,
        typography = Typography,
        content = content
    )
}
