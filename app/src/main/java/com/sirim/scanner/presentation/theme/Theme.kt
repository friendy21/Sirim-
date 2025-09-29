package com.sirim.scanner.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    secondary = SecondaryBlue,
    background = BackgroundGray,
    surface = SurfaceWhite,
)

private val DarkColors = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    secondary = SecondaryBlue,
    background = Color(0xFF101010),
    surface = Color(0xFF1F1F1F),
)

@Composable
fun SirimScannerTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
