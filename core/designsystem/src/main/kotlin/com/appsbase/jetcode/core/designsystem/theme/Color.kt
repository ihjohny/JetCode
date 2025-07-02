package com.appsbase.jetcode.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * JetCode color palette supporting light and dark themes
 */
object JetCodeColors {

    // Primary Colors
    val Primary = Color(0xFF6366F1) // Indigo
    val PrimaryVariant = Color(0xFF4F46E5)
    val OnPrimary = Color(0xFFFFFFFF)

    // Secondary Colors
    val Secondary = Color(0xFF8B5CF6) // Purple
    val SecondaryVariant = Color(0xFF7C3AED)
    val OnSecondary = Color(0xFFFFFFFF)

    // Background Colors - Light
    val BackgroundLight = Color(0xFFFAFAFA)
    val OnBackgroundLight = Color(0xFF1A1A1A)
    val SurfaceLight = Color(0xFFFFFFFF)
    val OnSurfaceLight = Color(0xFF1A1A1A)

    // Background Colors - Dark
    val BackgroundDark = Color(0xFF121212)
    val OnBackgroundDark = Color(0xFFE0E0E0)
    val SurfaceDark = Color(0xFF1E1E1E)
    val OnSurfaceDark = Color(0xFFE0E0E0)

    // Accent Colors
    val Success = Color(0xFF10B981) // Green
    val Warning = Color(0xFFF59E0B) // Amber
    val Error = Color(0xFFEF4444) // Red
    val Info = Color(0xFF3B82F6) // Blue

    // Learning Progress Colors
    val ProgressComplete = Color(0xFF10B981)
    val ProgressInProgress = Color(0xFF3B82F6)
    val ProgressLocked = Color(0xFF9CA3AF)

    // Difficulty Colors
    val DifficultyBeginner = Color(0xFF10B981) // Green
    val DifficultyIntermediate = Color(0xFFF59E0B) // Amber
    val DifficultyAdvanced = Color(0xFFEF4444) // Red
    val DifficultyExpert = Color(0xFF8B5CF6) // Purple

    // Material Type Colors
    val MaterialText = Color(0xFF64748B)
    val MaterialCode = Color(0xFF1E293B)
    val MaterialVideo = Color(0xFFDC2626)
    val MaterialImage = Color(0xFF059669)

    // Practice Type Colors
    val PracticeMCQ = Color(0xFF3B82F6)
    val PracticeCode = Color(0xFF8B5CF6)
    val PracticeOutput = Color(0xFFF59E0B)
    val PracticeFill = Color(0xFF10B981)
}
