package com.appsbase.jetcode.feature.settings.presentation

import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiState
import com.appsbase.jetcode.domain.model.AppThemeSettings
import com.appsbase.jetcode.domain.model.ColorPalette
import com.appsbase.jetcode.domain.model.FontFamily
import com.appsbase.jetcode.domain.model.ThemeMode

/**
 * MVI contracts for Settings Screen
 */
data class SettingsState(
    val isLoading: Boolean = false,
    val themeSettings: AppThemeSettings = AppThemeSettings(),
    val error: String? = null,
    val isSaving: Boolean = false,
) : UiState

sealed class SettingsIntent : UiIntent {
    object LoadSettings : SettingsIntent()
    object RetryLoad : SettingsIntent()
    object ResetToDefaults : SettingsIntent()
    data class UpdateThemeMode(val themeMode: ThemeMode) : SettingsIntent()
    data class UpdateColorPalette(val colorPalette: ColorPalette) : SettingsIntent()
    data class UpdateFontFamily(val fontFamily: FontFamily) : SettingsIntent()
}

sealed class SettingsEffect : UiEffect {
    data class ShowError(val message: String) : SettingsEffect()
    object ShowAutoSaveSuccess : SettingsEffect()
    object ShowResetSuccess : SettingsEffect()
}
