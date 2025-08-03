package com.appsbase.jetcode.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

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
    val success: Color,
    val warning: Color,
    val info: Color,
    val progressComplete: Color,
    val progressInProgress: Color,
    val progressLocked: Color,
    val difficultyBeginner: Color,
    val difficultyIntermediate: Color,
    val difficultyAdvanced: Color,
    val difficultyExpert: Color,
)

/**
 * Default extended colors instance
 */
private val DefaultJetCodeExtendedColors = JetCodeExtendedColors(
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

private val LocalJetCodeExtendedColors = staticCompositionLocalOf {
    DefaultJetCodeExtendedColors
}

@Composable
fun JetCodeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(
        LocalJetCodeExtendedColors provides DefaultJetCodeExtendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = JetCodeTypography,
            content = content,
        )
    }
}

/**
 * Access to extended colors
 */
object JetCodeTheme {
    val extendedColors: JetCodeExtendedColors
        @Composable get() = LocalJetCodeExtendedColors.current
}
