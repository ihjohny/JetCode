package com.appsbase.jetcode.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * JetCode theme implementation with light and dark mode support
 */

private val LightColorScheme = lightColorScheme(
    primary = JetCodeColors.Primary,
    onPrimary = JetCodeColors.OnPrimary,
    primaryContainer = JetCodeColors.PrimaryVariant,
    secondary = JetCodeColors.Secondary,
    onSecondary = JetCodeColors.OnSecondary,
    secondaryContainer = JetCodeColors.SecondaryVariant,
    background = JetCodeColors.BackgroundLight,
    onBackground = JetCodeColors.OnBackgroundLight,
    surface = JetCodeColors.SurfaceLight,
    onSurface = JetCodeColors.OnSurfaceLight,
    error = JetCodeColors.Error,
    onError = JetCodeColors.OnPrimary,
)

private val DarkColorScheme = darkColorScheme(
    primary = JetCodeColors.Primary,
    onPrimary = JetCodeColors.OnPrimary,
    primaryContainer = JetCodeColors.PrimaryVariant,
    secondary = JetCodeColors.Secondary,
    onSecondary = JetCodeColors.OnSecondary,
    secondaryContainer = JetCodeColors.SecondaryVariant,
    background = JetCodeColors.BackgroundDark,
    onBackground = JetCodeColors.OnBackgroundDark,
    surface = JetCodeColors.SurfaceDark,
    onSurface = JetCodeColors.OnSurfaceDark,
    error = JetCodeColors.Error,
    onError = JetCodeColors.OnPrimary,
)

/**
 * Extended colors for JetCode specific use cases
 */
data class JetCodeExtendedColors(
    val success: androidx.compose.ui.graphics.Color,
    val warning: androidx.compose.ui.graphics.Color,
    val info: androidx.compose.ui.graphics.Color,
    val progressComplete: androidx.compose.ui.graphics.Color,
    val progressInProgress: androidx.compose.ui.graphics.Color,
    val progressLocked: androidx.compose.ui.graphics.Color,
    val difficultyBeginner: androidx.compose.ui.graphics.Color,
    val difficultyIntermediate: androidx.compose.ui.graphics.Color,
    val difficultyAdvanced: androidx.compose.ui.graphics.Color,
    val difficultyExpert: androidx.compose.ui.graphics.Color,
)

private val LocalJetCodeExtendedColors = staticCompositionLocalOf {
    JetCodeExtendedColors(
        success = JetCodeColors.Success,
        warning = JetCodeColors.Warning,
        info = JetCodeColors.Info,
        progressComplete = JetCodeColors.ProgressComplete,
        progressInProgress = JetCodeColors.ProgressInProgress,
        progressLocked = JetCodeColors.ProgressLocked,
        difficultyBeginner = JetCodeColors.DifficultyBeginner,
        difficultyIntermediate = JetCodeColors.DifficultyIntermediate,
        difficultyAdvanced = JetCodeColors.DifficultyAdvanced,
        difficultyExpert = JetCodeColors.DifficultyExpert,
    )
}

@Composable
fun JetCodeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = JetCodeExtendedColors(
        success = JetCodeColors.Success,
        warning = JetCodeColors.Warning,
        info = JetCodeColors.Info,
        progressComplete = JetCodeColors.ProgressComplete,
        progressInProgress = JetCodeColors.ProgressInProgress,
        progressLocked = JetCodeColors.ProgressLocked,
        difficultyBeginner = JetCodeColors.DifficultyBeginner,
        difficultyIntermediate = JetCodeColors.DifficultyIntermediate,
        difficultyAdvanced = JetCodeColors.DifficultyAdvanced,
        difficultyExpert = JetCodeColors.DifficultyExpert,
    )

    CompositionLocalProvider(
        LocalJetCodeExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = JetCodeTypography,
            content = content
        )
    }
}

/**
 * Access to extended colors
 */
object JetCodeTheme {
    val extendedColors: JetCodeExtendedColors
        @Composable
        get() = LocalJetCodeExtendedColors.current
}
