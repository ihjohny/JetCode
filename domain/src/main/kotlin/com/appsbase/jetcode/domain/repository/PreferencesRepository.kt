package com.appsbase.jetcode.domain.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.domain.model.ThemeMode

/**
 * Repository interface for user preferences
 */
interface PreferencesRepository {

    /**
     * Get the onboarding completion status
     */
    suspend fun getShouldShowOnboarding(): Result<Boolean>

    /**
     * Set the onboarding completion status
     */
    suspend fun setShouldShowOnboarding(shouldShow: Boolean): Result<Unit>

    /**
     * Get the current theme mode
     */
    suspend fun getThemeMode(): Result<ThemeMode>

    /**
     * Set the theme mode
     */
    suspend fun setThemeMode(themeMode: ThemeMode): Result<Unit>

    /**
     * Clear all stored preferences
     */
    suspend fun clearAll(): Result<Unit>
}