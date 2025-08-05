package com.appsbase.jetcode.data.preferences.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.appsbase.jetcode.data.preferences.entity.ThemeModeEntity
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

    companion object {
        private const val PREFERENCES_NAME = "jetcode_preferences"
        private val SHOULD_SHOW_ONBOARDING = booleanPreferencesKey("should_show_onboarding")
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private const val DEFAULT_SHOULD_SHOW_ONBOARDING = true
        private val DEFAULT_THEME_MODE_ENTITY = ThemeModeEntity.SYSTEM
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

    override suspend fun getThemeMode(): ThemeModeEntity {
        return try {
            context.dataStore.data.catch { exception ->
                if (exception is IOException) {
                    Timber.e(exception, "Error reading theme preference")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                val themeString = preferences[THEME_MODE] ?: DEFAULT_THEME_MODE_ENTITY.name
                try {
                    ThemeModeEntity.valueOf(themeString)
                } catch (e: IllegalArgumentException) {
                    Timber.w("Invalid theme mode: $themeString, using default")
                    DEFAULT_THEME_MODE_ENTITY
                }
            }.first() // Collect the first value to return as single shot ThemeModeEntity
        } catch (e: Exception) {
            Timber.e(e, "Failed to read theme preference")
            DEFAULT_THEME_MODE_ENTITY
        }
    }

    override suspend fun setThemeMode(themeMode: ThemeModeEntity) {
        try {
            context.dataStore.edit { preferences ->
                preferences[THEME_MODE] = themeMode.name
            }
            Timber.d("Updated theme mode to: $themeMode")
        } catch (exception: IOException) {
            Timber.e(exception, "Error writing theme preference")
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
