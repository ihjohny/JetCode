package com.appsbase.jetcode.data.repository.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.data.database.dao.ProgressDao
import com.appsbase.jetcode.data.repository.mapper.toDomain
import com.appsbase.jetcode.data.repository.mapper.toEntity
import com.appsbase.jetcode.domain.model.TopicProgress
import com.appsbase.jetcode.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * Implementation of ProgressRepository following Clean Architecture
 * Handles progress data from local database
 */
class ProgressRepositoryImpl(
    private val progressDao: ProgressDao,
) : ProgressRepository {

    override suspend fun upsertProgress(progress: TopicProgress): Result<Unit> {
        return try {
            progressDao.upsertProgress(progress.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(
                e,
                "Error upserting progress for topic: ${progress.topicId}, user: ${progress.userId}"
            )
            Result.Error(AppError.DataError.DatabaseError)
        }
    }

    override fun getProgressByTopicAndUser(
        topicId: String,
        userId: String,
    ): Flow<Result<TopicProgress?>> {
        return progressDao.getProgressByTopicAndUser(
            topicId = topicId,
            userId = userId,
        ).map { entity ->
            try {
                val progress = entity?.toDomain()
                Result.Success(progress)
            } catch (e: Exception) {
                Timber.e(e, "Error mapping progress to domain for topic: $topicId, user: $userId")
                Result.Error(AppError.DataError.ParseError(e))
            }
        }.catch { e ->
            Timber.e(e, "Error getting progress from database for topic: $topicId, user: $userId")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }
}
