package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PersianTealLight,
    onPrimary = Color.Black,
    primaryContainer = PersianTealDark,
    onPrimaryContainer = Color.White,
    secondary = GoldAmber,
    onSecondary = Color.Black,
    secondaryContainer = GoldDark,
    onSecondaryContainer = Color.White,
    tertiary = CrimsonTertiary,
    background = MidnightBg,
    surface = MidnightCard,
    surfaceVariant = MidnightCardElevated,
    onBackground = TextLight,
    onSurface = TextLight
)

private val LightColorScheme = lightColorScheme(
    primary = PersianTealPrimary,
    onPrimary = Color.White,
    primaryContainer = PersianTealLight,
    onPrimaryContainer = Color.Black,
    secondary = GoldSecondary,
    onSecondary = Color.White,
    secondaryContainer = GoldLight,
    onSecondaryContainer = Color.Black,
    tertiary = CrimsonTertiary,
    background = CreamBg,
    surface = CreamCard,
    surfaceVariant = Color(0xFFF1F5F9),
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to gorgeous dark Iranian arcade style!
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
