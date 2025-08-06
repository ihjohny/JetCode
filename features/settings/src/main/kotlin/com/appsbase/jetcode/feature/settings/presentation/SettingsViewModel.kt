package com.appsbase.jetcode.feature.settings.presentation

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.domain.model.AppThemeSettings
import com.appsbase.jetcode.domain.model.ColorPalette
import com.appsbase.jetcode.domain.model.FontFamily
import com.appsbase.jetcode.domain.model.ThemeMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Settings screen following MVI pattern
 */
class SettingsViewModel(
) : BaseViewModel<SettingsState, SettingsIntent, SettingsEffect>(
    initialState = SettingsState()
) {

    init {
        handleIntent(SettingsIntent.LoadSettings)
    }

    override fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.LoadSettings -> loadSettings()
            is SettingsIntent.RetryLoad -> loadSettings()
            is SettingsIntent.UpdateThemeMode -> updateThemeMode(intent.themeMode)
            is SettingsIntent.UpdateColorPalette -> updateColorPalette(intent.colorPalette)
            is SettingsIntent.UpdateFontFamily -> updateFontFamily(intent.fontFamily)
            is SettingsIntent.ResetToDefaults -> resetToDefaults()
        }
    }

    private fun loadSettings() {
        updateState(currentState().copy(isLoading = true, error = null))

        viewModelScope.launch {
            try {
                // TODO: Replace with actual use case implementation
                // Dummy implementation for now
                delay(500) // Simulate network/database delay

                val dummySettings = AppThemeSettings(
                    themeMode = ThemeMode.SYSTEM,
                    colorPalette = ColorPalette.BLUE_THEME,
                    fontFamily = FontFamily.DEFAULT
                )

                updateState(
                    currentState().copy(
                        isLoading = false,
                        themeSettings = dummySettings,
                        error = null
                    )
                )
                Timber.d("Settings loaded successfully (dummy data)")

                /* Uncomment when use case is implemented:
                when (val result = getAppThemeSettingsUseCase()) {
                    is Result.Success -> {
                        originalSettings = result.data
                        updateState(
                            currentState().copy(
                                isLoading = false,
                                themeSettings = result.data,
                                error = null,
                                hasChanges = false
                            )
                        )
                        Timber.d("Settings loaded successfully")
                    }
                    is Result.Error -> handleError(result.exception)
                }
                */
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun updateThemeMode(themeMode: ThemeMode) {
        val updatedSettings = currentState().themeSettings.copy(themeMode = themeMode)
        updateAndSaveSettings(updatedSettings)
    }

    private fun updateColorPalette(colorPalette: ColorPalette) {
        val updatedSettings = currentState().themeSettings.copy(colorPalette = colorPalette)
        updateAndSaveSettings(updatedSettings)
    }

    private fun updateFontFamily(fontFamily: FontFamily) {
        val updatedSettings = currentState().themeSettings.copy(fontFamily = fontFamily)
        updateAndSaveSettings(updatedSettings)
    }

    private fun updateAndSaveSettings(settings: AppThemeSettings) {
        // Update UI immediately
        updateState(
            currentState().copy(
                themeSettings = settings,
                isSaving = true
            )
        )

        // Auto-save in background
        viewModelScope.launch {
            try {
                // TODO: Replace with actual use case implementation
                // Dummy implementation for now
                delay(300) // Simulate save operation

                updateState(
                    currentState().copy(
                        isSaving = false,
                        error = null
                    )
                )
                sendEffect(SettingsEffect.ShowAutoSaveSuccess)
                Timber.d("Settings auto-saved successfully (dummy implementation)")

                /* Uncomment when use case is implemented:
                when (val result = updateAppThemeSettingsUseCase(settings)) {
                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                isSaving = false,
                                error = null
                            )
                        )
                        sendEffect(SettingsEffect.ShowAutoSaveSuccess)
                        Timber.d("Settings auto-saved successfully")
                    }
                    is Result.Error -> handleError(result.exception)
                }
                */
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun resetToDefaults() {
        val defaultSettings = AppThemeSettings()
        updateAndSaveSettings(defaultSettings)
        sendEffect(SettingsEffect.ShowResetSuccess)
    }

    private fun handleError(exception: Throwable) {
        val errorMessage = when (exception) {
            is AppError -> exception.getUserMessage()
            else -> exception.message ?: "An unexpected error occurred"
        }
        updateState(currentState().copy(isLoading = false, error = errorMessage))
        sendEffect(SettingsEffect.ShowError(errorMessage))
        Timber.e(exception, "Settings error: $errorMessage")
    }
}
