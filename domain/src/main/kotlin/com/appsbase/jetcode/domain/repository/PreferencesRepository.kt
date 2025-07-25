package com.appsbase.jetcode.domain.repository

import com.appsbase.jetcode.core.common.Result

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
     * Clear all stored preferences
     */
    suspend fun clearAll(): Result<Unit>
}