package com.appsbase.jetcode.data.preferences.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

/**
 * DataStore implementation for managing user preferences
 */
class PreferencesDataStoreImpl(
    private val context: Context,
) : PreferencesDataStore {

    companion object Companion {
        private const val PREFERENCES_NAME = "jetcode_preferences"
        private val SHOULD_SHOW_ONBOARDING = booleanPreferencesKey("should_show_onboarding")
        private const val DEFAULT_SHOULD_SHOW_ONBOARDING = true
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = PREFERENCES_NAME
    )

    override suspend fun getShouldShowOnboarding(): Boolean {
        return try {
            context.dataStore.data.catch { exception ->
                if (exception is IOException) {
                    Timber.e(exception, "Error reading preferences")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[SHOULD_SHOW_ONBOARDING] ?: DEFAULT_SHOULD_SHOW_ONBOARDING
            }.first() // Collect the first value to return as single shot Boolean
        } catch (e: Exception) {
            Timber.e(e, "Failed to read onboarding preference")
            DEFAULT_SHOULD_SHOW_ONBOARDING
        }
    }

    override suspend fun setShouldShowOnboarding(shouldShow: Boolean) {
        try {
            context.dataStore.edit { preferences ->
                preferences[SHOULD_SHOW_ONBOARDING] = shouldShow
            }
            Timber.d("Updated shouldShowOnboarding to: $shouldShow")
        } catch (exception: IOException) {
            Timber.e(exception, "Error writing shouldShowOnboarding preference")
            throw exception
        }
    }

    override suspend fun clearAll() {
        try {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
            Timber.d("All preferences cleared")
        } catch (exception: IOException) {
            Timber.e(exception, "Error clearing preferences")
            throw exception
        }
    }

}
