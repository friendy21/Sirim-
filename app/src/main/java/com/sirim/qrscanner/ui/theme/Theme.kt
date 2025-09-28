package com.sirim.qrscanner.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    secondary = SecondaryGold,
    background = NeutralBackground,
    surface = NeutralSurface,
    onSurface = NeutralOnSurface
)

private val DarkColors = darkColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryGold
)

@Composable
fun SirimTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors: ColorScheme = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = SirimTypography,
        content = content
    )
}
