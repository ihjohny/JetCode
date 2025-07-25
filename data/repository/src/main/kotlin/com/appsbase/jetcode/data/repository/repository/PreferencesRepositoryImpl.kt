package com.appsbase.jetcode.data.repository.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.data.preferences.data_store.PreferencesDataStore
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
