
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
    private val preferencesDataSource: PreferencesDataStore,
) : PreferencesRepository {

    override suspend fun getShouldShowOnboarding(): Result<Boolean> {
        return try {
            val shouldShow = preferencesDataSource.getShouldShowOnboarding()
            Result.Success(shouldShow)
        } catch (e: Exception) {
            Timber.e(e, "Error getting onboarding preference")
            Result.Error(AppError.DataError.DatabaseError)
        }
    }

    override suspend fun setShouldShowOnboarding(shouldShow: Boolean): Result<Unit> {
        return try {
            preferencesDataSource.setShouldShowOnboarding(shouldShow)
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error setting onboarding preference")
            Result.Error(AppError.DataError.DatabaseError)
        }
    }

    override suspend fun clearAll(): Result<Unit> {
        return try {
            preferencesDataSource.clearAll()
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error clearing all preferences")
            Result.Error(AppError.DataError.DatabaseError)
        }
    }
}
