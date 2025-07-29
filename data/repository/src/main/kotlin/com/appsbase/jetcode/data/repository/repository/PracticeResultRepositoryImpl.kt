package com.appsbase.jetcode.data.repository.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.data.database.dao.PracticeResultDao
import com.appsbase.jetcode.data.repository.mapper.toDomain
import com.appsbase.jetcode.data.repository.mapper.toEntity
import com.appsbase.jetcode.domain.model.PracticeSetResult
import com.appsbase.jetcode.domain.repository.PracticeResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * Implementation of PracticeResultRepository following Clean Architecture
 */
class PracticeResultRepositoryImpl(
    private val practiceResultDao: PracticeResultDao,
) : PracticeResultRepository {

    override suspend fun upsertPracticeResult(result: PracticeSetResult): Result<Unit> {
        return try {
            practiceResultDao.upsertPracticeResult(result.toEntity())
            Timber.d("Practice result upserted successfully for user ${result.userId}, practice set ${result.practiceSetId}")
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error upserting practice result")
            Result.Error(AppError.DataError.DatabaseError)
        }
    }

    override fun getPracticeResultById(
        practiceSetId: String,
        userId: String,
    ): Flow<Result<PracticeSetResult?>> {
        return practiceResultDao.getPracticeResultById(practiceSetId, userId).map { entity ->
            try {
                if (entity != null) {
                    Result.Success(entity.toDomain())
                } else {
                    Result.Success(null)
                }
            } catch (e: Exception) {
                Timber.e(e, "Error mapping practice result to domain")
                Result.Error(AppError.DataError.ParseError(e))
            }
        }.catch { e ->
            Timber.e(e, "Error getting practice result by id from database")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

    override fun getAllPracticeResults(userId: String): Flow<Result<List<PracticeSetResult>>> {
        return practiceResultDao.getAllPracticeResults(userId).map { entities ->
            try {
                val results = entities.map { it.toDomain() }
                Result.Success(results)
            } catch (e: Exception) {
                Timber.e(e, "Error mapping practice results to domain")
                Result.Error(AppError.DataError.ParseError(e))
            }
        }.catch { e ->
            Timber.e(e, "Error getting all practice results from database")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

    override fun getPracticeResultsByIds(
        practiceSetIds: List<String>,
        userId: String,
    ): Flow<Result<List<PracticeSetResult>>> {
        return if (practiceSetIds.isEmpty()) {
            kotlinx.coroutines.flow.flowOf(Result.Success(emptyList()))
        } else {
            practiceResultDao.getPracticeResultsByIds(practiceSetIds, userId).map { entities ->
                try {
                    val results = entities.map { it.toDomain() }
                    Result.Success(results)
                } catch (e: Exception) {
                    Timber.e(e, "Error mapping practice results to domain")
                    Result.Error(AppError.DataError.ParseError(e))
                }
            }.catch { e ->
                Timber.e(e, "Error getting practice results by ids from database")
                emit(Result.Error(AppError.DataError.DatabaseError))
            }
        }
    }

    override suspend fun deleteAllUserPracticeResults(userId: String): Result<Unit> {
        return try {
            practiceResultDao.deleteAllUserPracticeResults(userId)
            Timber.d("All practice results deleted successfully for user $userId")
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error deleting all user practice results")
            Result.Error(AppError.DataError.DatabaseError)
        }
    }
}
