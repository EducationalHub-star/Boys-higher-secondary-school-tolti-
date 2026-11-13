package com.example.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppDesignConfig

val LocalAppDesignConfig = compositionLocalOf { AppDesignConfig() }

fun designButtonShape(config: AppDesignConfig): Shape = when (config.buttonStyle) {
    "PILL" -> RoundedCornerShape(50)
    "SHARP" -> RoundedCornerShape(4.dp)
    else -> RoundedCornerShape(12.dp)
}

fun designCardShape(config: AppDesignConfig): Shape = when (config.buttonStyle) {
    "PILL" -> RoundedCornerShape(24.dp)
    "SHARP" -> RoundedCornerShape(6.dp)
    else -> RoundedCornerShape(18.dp)
}

@Composable
fun designCardColors(
    config: AppDesignConfig = LocalAppDesignConfig.current,
    defaultContainer: Color = MaterialTheme.colorScheme.surface
): CardColors {
    val container = when (config.cardStyle) {
        "GLASS" -> defaultContainer.copy(alpha = 0.86f)
        "OUTLINED" -> MaterialTheme.colorScheme.surface
        else -> defaultContainer
    }
    return CardDefaults.cardColors(
        containerColor = container,
        contentColor = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun designCardBorder(
    config: AppDesignConfig = LocalAppDesignConfig.current,
    accent: Color = MaterialTheme.colorScheme.outlineVariant
): BorderStroke? = when (config.cardStyle) {
    "OUTLINED" -> BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
    "GLASS" -> BorderStroke(1.dp, Color.White.copy(alpha = 0.55f))
    else -> BorderStroke(1.dp, accent.copy(alpha = 0.35f))
}

private fun scaledTypography(scale: Float): Typography {
    val s = scale.coerceIn(0.85f, 1.25f)
    val base = Typography()
    return Typography(
        displayLarge = base.displayLarge.copy(fontSize = (57f * s).sp, lineHeight = (64f * s).sp),
        displayMedium = base.displayMedium.copy(fontSize = (45f * s).sp, lineHeight = (52f * s).sp),
        displaySmall = base.displaySmall.copy(fontSize = (36f * s).sp, lineHeight = (44f * s).sp),
        headlineLarge = base.headlineLarge.copy(fontSize = (32f * s).sp, lineHeight = (40f * s).sp),
        headlineMedium = base.headlineMedium.copy(fontSize = (28f * s).sp, lineHeight = (36f * s).sp),
        headlineSmall = base.headlineSmall.copy(fontSize = (24f * s).sp, lineHeight = (32f * s).sp),
        titleLarge = base.titleLarge.copy(fontSize = (22f * s).sp, lineHeight = (28f * s).sp),
        titleMedium = base.titleMedium.copy(fontSize = (16f * s).sp, lineHeight = (24f * s).sp),
        titleSmall = base.titleSmall.copy(fontSize = (14f * s).sp, lineHeight = (20f * s).sp),
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = (16f * s).sp,
            lineHeight = (24f * s).sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = base.bodyMedium.copy(fontSize = (14f * s).sp, lineHeight = (20f * s).sp),
        bodySmall = base.bodySmall.copy(fontSize = (12f * s).sp, lineHeight = (16f * s).sp),
        labelLarge = base.labelLarge.copy(fontSize = (14f * s).sp, lineHeight = (20f * s).sp),
        labelMedium = base.labelMedium.copy(fontSize = (12f * s).sp, lineHeight = (16f * s).sp),
        labelSmall = base.labelSmall.copy(fontSize = (11f * s).sp, lineHeight = (16f * s).sp)
    )
}

@Composable
fun SchoolAcademicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    designConfig: AppDesignConfig = AppDesignConfig(),
    content: @Composable () -> Unit
) {
    val isDark = when (designConfig.themeMode) {
        "DARK" -> true
        "LIGHT" -> false
        "SCHOOL_CUSTOM" -> false
        else -> darkTheme
    }

    val primaryColor = AppDesignConfig.parseColorSafe(designConfig.primaryColorHex, DeepNavy)
    val secondaryColor = AppDesignConfig.parseColorSafe(designConfig.secondaryColorHex, RoyalBlue)
    val accentColor = AppDesignConfig.parseColorSafe(designConfig.accentColorHex, ElegantGold)
    val customBg = AppDesignConfig.parseColorSafe(designConfig.backgroundColorHex, Color(0xFFF8FAFC))

    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = if (primaryColor.luminance() < 0.25f) Color(0xFF60A5FA) else primaryColor,
            onPrimary = Color(0xFF0B192C),
            primaryContainer = primaryColor.copy(alpha = 0.85f),
            onPrimaryContainer = Color(0xFFDBEAFE),
            secondary = if (secondaryColor.luminance() < 0.25f) Color(0xFF93C5FD) else secondaryColor,
            onSecondary = Color(0xFF0F172A),
            tertiary = accentColor,
            background = Color(0xFF0F172A),
            surface = Color(0xFF1E293B),
            surfaceVariant = Color(0xFF334155),
            onBackground = Color(0xFFF8FAFC),
            onSurface = Color(0xFFF8FAFC),
            onSurfaceVariant = Color(0xFFCBD5E1),
            outline = Color(0xFF475569),
            outlineVariant = Color(0xFF334155)
        )
    } else {
        val primaryContainerColor = secondaryColor.copy(alpha = 0.14f)
        lightColorScheme(
            primary = primaryColor,
            onPrimary = if (primaryColor.luminance() > 0.6f) Color(0xFF0F172A) else Color.White,
            primaryContainer = primaryContainerColor,
            onPrimaryContainer = primaryColor,
            secondary = secondaryColor,
            onSecondary = if (secondaryColor.luminance() > 0.6f) Color(0xFF0F172A) else Color.White,
            secondaryContainer = SchoolNavyLight,
            onSecondaryContainer = primaryColor,
            tertiary = accentColor,
            onTertiary = Color.White,
            background = customBg,
            surface = Color.White,
            surfaceVariant = Color(0xFFF1F5F9),
            onBackground = Color(0xFF0F172A),
            onSurface = Color(0xFF0F172A),
            onSurfaceVariant = Color(0xFF475569),
            outline = Color(0xFFCBD5E1),
            outlineVariant = Color(0xFFE2E8F0)
        )
    }

    val buttonCorner = when (designConfig.buttonStyle) {
        "PILL" -> 50
        "SHARP" -> 4
        else -> 12
    }
    val cardCorner = when (designConfig.buttonStyle) {
        "PILL" -> 22.dp
        "SHARP" -> 6.dp
        else -> 16.dp
    }

    val customShapes = Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = if (designConfig.buttonStyle == "PILL") RoundedCornerShape(50) else RoundedCornerShape(buttonCorner.dp),
        medium = RoundedCornerShape(cardCorner),
        large = RoundedCornerShape(cardCorner),
        extraLarge = RoundedCornerShape(28.dp)
    )

    CompositionLocalProvider(LocalAppDesignConfig provides designConfig) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = scaledTypography(designConfig.fontSizeScale),
            shapes = customShapes,
            content = content
        )
    }
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
