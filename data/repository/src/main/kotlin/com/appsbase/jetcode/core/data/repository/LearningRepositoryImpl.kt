package com.appsbase.jetcode.core.data.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.database.dao.LearningDao
import com.appsbase.jetcode.core.data.mapper.toDomain
import com.appsbase.jetcode.core.data.mapper.toEntity
import com.appsbase.jetcode.core.data.remote.LearningApiService
import com.appsbase.jetcode.core.domain.model.Skill
import com.appsbase.jetcode.core.domain.model.Topic
import com.appsbase.jetcode.core.domain.model.Lesson
import com.appsbase.jetcode.core.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * Implementation of LearningRepository following Clean Architecture
 * Handles data from both local database and remote API
 */
class LearningRepositoryImpl(
    private val learningDao: LearningDao,
    private val apiService: LearningApiService
) : LearningRepository {

    override fun getSkills(): Flow<Result<List<Skill>>> {
        return learningDao.getAllSkills()
            .map { entities ->
                try {
                    val skills = entities.map { it.toDomain() }
                    Result.Success(skills)
                } catch (e: Exception) {
                    Timber.e(e, "Error mapping skills to domain")
                    Result.Error(AppError.DataError.ParseError(e))
                }
            }
            .catch { e ->
                Timber.e(e, "Error getting skills from database")
                emit(Result.Error(AppError.DataError.DatabaseError))
            }
    }

    override fun getSkillById(skillId: String): Flow<Result<Skill>> {
        return learningDao.getSkillById(skillId)
            .map { entity ->
                try {
                    if (entity != null) {
                        Result.Success(entity.toDomain())
                    } else {
                        Result.Error(AppError.DataError.NotFound)
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error mapping skill to domain")
                    Result.Error(AppError.DataError.ParseError(e))
                }
            }
            .catch { e ->
                Timber.e(e, "Error getting skill by id from database")
                emit(Result.Error(AppError.DataError.DatabaseError))
            }
    }

    override fun getTopicsForSkill(skillId: String): Flow<Result<List<Topic>>> {
        return learningDao.getTopicsForSkill(skillId)
            .map { entities ->
                try {
                    val topics = entities.map { it.toDomain() }
                    Result.Success(topics)
                } catch (e: Exception) {
                    Timber.e(e, "Error mapping topics to domain")
                    Result.Error(AppError.DataError.ParseError(e))
                }
            }
            .catch { e ->
                Timber.e(e, "Error getting topics from database")
                emit(Result.Error(AppError.DataError.DatabaseError))
            }
    }

    override fun getLessonsForTopic(topicId: String): Flow<Result<List<Lesson>>> {
        return learningDao.getLessonsForTopic(topicId)
            .map { entities ->
                try {
                    val lessons = entities.map { it.toDomain() }
                    Result.Success(lessons)
                } catch (e: Exception) {
                    Timber.e(e, "Error mapping lessons to domain")
                    Result.Error(AppError.DataError.ParseError(e))
                }
            }
            .catch { e ->
                Timber.e(e, "Error getting lessons from database")
                emit(Result.Error(AppError.DataError.DatabaseError))
            }
    }

    override fun getLessonById(lessonId: String): Flow<Result<Lesson>> {
        return learningDao.getLessonById(lessonId)
            .map { entity ->
                try {
                    if (entity != null) {
                        Result.Success(entity.toDomain())
                    } else {
                        Result.Error(AppError.DataError.NotFound)
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error mapping lesson to domain")
                    Result.Error(AppError.DataError.ParseError(e))
                }
            }
            .catch { e ->
                Timber.e(e, "Error getting lesson by id from database")
                emit(Result.Error(AppError.DataError.DatabaseError))
            }
    }

    override suspend fun syncContent(): Result<Unit> {
        return try {
            // Fetch content from remote API (GitHub in this case)
            val remoteSkills = apiService.getSkills()

            // Clear existing data and insert new data
            learningDao.clearSkills()
            learningDao.clearTopics()
            learningDao.clearLessons()
            learningDao.clearMaterials()
            learningDao.clearPractices()

            // Insert new data
            learningDao.insertSkills(remoteSkills.map { it.toEntity() })

            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error syncing content")
            Result.Error(AppError.NetworkError.Unknown(e))
        }
    }

    override fun searchContent(query: String): Flow<Result<List<Any>>> {
        return learningDao.searchSkills(query)
            .map { skillEntities ->
                try {
                    val results = mutableListOf<Any>()
                    results.addAll(skillEntities.map { it.toDomain() })
                    Result.Success(results as List<Any>)
                } catch (e: Exception) {
                    Timber.e(e, "Error searching content")
                    Result.Error(AppError.DataError.ParseError(e))
                }
            }
            .catch { e ->
                Timber.e(e, "Error in search query")
                emit(Result.Error(AppError.DataError.DatabaseError))
            }
    }
}
