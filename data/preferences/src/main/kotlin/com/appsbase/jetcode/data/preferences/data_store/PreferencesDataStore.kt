package com.appsbase.jetcode.data.preferences.data_store

import com.appsbase.jetcode.data.preferences.entity.ThemeModeEntity

/**
 * Data source interface for local preferences storage
 */
interface PreferencesDataStore {

    /**
     * Get the onboarding completion status
     */
    suspend fun getShouldShowOnboarding(): Boolean

    /**
     * Set the onboarding completion status
     */
    suspend fun setShouldShowOnboarding(shouldShow: Boolean)

    /**
     * Get the current theme mode
     */
    suspend fun getThemeMode(): ThemeModeEntity

    /**
     * Set the theme mode
     */
    suspend fun setThemeMode(themeMode: ThemeModeEntity)

    /**
     * Clear all stored preferences
     */
    suspend fun clearAll()
}
