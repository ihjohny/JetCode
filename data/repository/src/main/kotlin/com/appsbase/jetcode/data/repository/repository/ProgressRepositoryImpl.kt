package com.appsbase.jetcode.data.repository.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.data.database.dao.ProgressDao
import com.appsbase.jetcode.data.repository.mapper.toDomain
import com.appsbase.jetcode.data.repository.mapper.toEntity
import com.appsbase.jetcode.domain.model.SkillProgress
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

    override suspend fun upsertTopicProgress(progress: TopicProgress): Result<Unit> {
        return try {
            progressDao.upsertTopicProgress(progress.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(
                e,
                "Error upserting progress for topic: ${progress.topicId}, user: ${progress.userId}"
            )
            Result.Error(AppError.DataError.DatabaseError)
        }
    }

    override fun getTopicProgressById(
        topicId: String,
        userId: String,
    ): Flow<Result<TopicProgress?>> {
        return progressDao.getTopicProgressById(
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

    override fun getTopicsProgressByIds(
        topicIds: List<String>,
        userId: String,
    ): Flow<Result<List<TopicProgress>>> {
        return progressDao.getTopicsProgressByIds(
            topicIds = topicIds,
            userId = userId,
        ).map { entities ->
            try {
                val progressList = entities.map { it.toDomain() }
                Result.Success(progressList)
            } catch (e: Exception) {
                Timber.e(
                    e, "Error mapping progress list to domain for topics: $topicIds, user: $userId"
                )
                Result.Error(AppError.DataError.ParseError(e))
            }
        }.catch { e ->
            Timber.e(
                e, "Error getting progress list from database for topics: $topicIds, user: $userId"
            )
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

    override suspend fun upsertSkillProgress(progress: SkillProgress): Result<Unit> {
        return try {
            progressDao.upsertSkillProgress(progress.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(
                e,
                "Error upserting skill progress for skill: ${progress.skillId}, user: ${progress.userId}"
            )
            Result.Error(AppError.DataError.DatabaseError)
        }
    }

    override fun getSkillProgressById(
        skillId: String,
        userId: String,
    ): Flow<Result<SkillProgress?>> {
        return progressDao.getSkillProgressById(
            skillId = skillId,
            userId = userId,
        ).map { entity ->
            try {
                val progress = entity?.toDomain()
                Result.Success(progress)
            } catch (e: Exception) {
                Timber.e(
                    e, "Error mapping skill progress to domain for skill: $skillId, user: $userId"
                )
                Result.Error(AppError.DataError.ParseError(e))
            }
        }.catch { e ->
            Timber.e(
                e, "Error getting skill progress from database for skill: $skillId, user: $userId"
            )
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

    override fun getAllSkillsProgress(userId: String): Flow<Result<List<SkillProgress>>> {
        return progressDao.getAllSkillsProgress(userId = userId).map { entities ->
            try {
                val progressList = entities.map { it.toDomain() }
                Result.Success(progressList)
            } catch (e: Exception) {
                Timber.e(
                    e, "Error mapping all skills progress list to domain for user: $userId"
                )
                Result.Error(AppError.DataError.ParseError(e))
            }
        }.catch { e ->
            Timber.e(
                e, "Error getting all skills progress list from database for user: $userId"
            )
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }
}
