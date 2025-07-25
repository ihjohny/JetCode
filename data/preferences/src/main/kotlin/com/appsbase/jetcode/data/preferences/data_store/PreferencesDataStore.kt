package com.appsbase.jetcode.data.preferences.data_store

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
     * Clear all stored preferences
     */
    suspend fun clearAll()
}
