package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = SchoolGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = SchoolGreenLight,
    onPrimaryContainer = SchoolGreenPrimary,
    secondary = SchoolNavy,
    onSecondary = Color.White,
    secondaryContainer = SchoolNavyLight,
    onSecondaryContainer = SchoolNavy,
    tertiary = SchoolGold,
    background = Color(0xFFF8FAFC),
    surface = Color.White,
    surfaceVariant = Color(0xFFF1F5F9),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF34D399),
    onPrimary = Color(0xFF064E3B),
    primaryContainer = Color(0xFF065F46),
    onPrimaryContainer = Color(0xFFA7F3D0),
    secondary = Color(0xFF93C5FD),
    onSecondary = Color(0xFF1E3A8A),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFF334155),
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC),
    onSurfaceVariant = Color(0xFFCBD5E1)
)

@Composable
fun SchoolAcademicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Aliases for compatibility
@Composable
fun KasuruMalTokTheme(content: @Composable () -> Unit) {
    SchoolAcademicTheme(content = content)
}

@Composable
fun CampusPulseTheme(content: @Composable () -> Unit) {
    SchoolAcademicTheme(content = content)
}

@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    SchoolAcademicTheme(content = content)
}
