package com.appsbase.jetcode.data.repository.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.data.preferences.data_store.PreferencesDataStore
import com.appsbase.jetcode.data.repository.mapper.toDomain
import com.appsbase.jetcode.data.repository.mapper.toEntity
import com.appsbase.jetcode.domain.model.ThemeMode
import com.appsbase.jetcode.domain.repository.PreferencesRepository
import timber.log.Timber

/**
 * Implementation of PreferencesRepository following Clean Architecture
 * Handles data from local preferences storage
 */
class PreferencesRepositoryImpl(
    private val preferencesDataStore: PreferencesDataStore,
) : PreferencesRepository {

    override suspend fun getShouldShowOnboarding(): Result<Boolean> {
        return try {
            val shouldShow = preferencesDataStore.getShouldShowOnboarding()
            Result.Success(shouldShow)
        } catch (e: Exception) {
            Timber.e(e, "Error getting onboarding preference")
            Result.Error(AppError.DataError.DatabaseError)
        }
    }

    override suspend fun setShouldShowOnboarding(shouldShow: Boolean): Result<Unit> {
        return try {
            preferencesDataStore.setShouldShowOnboarding(shouldShow)
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error setting onboarding preference")
            Result.Error(AppError.DataError.DatabaseError)
        }
    }

    override suspend fun getThemeMode(): Result<ThemeMode> {
        return try {
            val themeModeEntity = preferencesDataStore.getThemeMode()
            val themeMode = themeModeEntity.toDomain()
            Result.Success(themeMode)
        } catch (e: Exception) {
            Timber.e(e, "Error getting theme preference")
            Result.Error(AppError.DataError.DatabaseError)
        }
    }

    override suspend fun setThemeMode(themeMode: ThemeMode): Result<Unit> {
        return try {
            val themeModeEntity = themeMode.toEntity()
            preferencesDataStore.setThemeMode(themeModeEntity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error setting theme preference")
            Result.Error(AppError.DataError.DatabaseError)
        }
    }

    override suspend fun clearAll(): Result<Unit> {
        return try {
            preferencesDataStore.clearAll()
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error clearing all preferences")
            Result.Error(AppError.DataError.DatabaseError)
        }
    }
}
