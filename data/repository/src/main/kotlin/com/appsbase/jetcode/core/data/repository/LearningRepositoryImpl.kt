package com.appsbase.jetcode.core.data.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.data.mapper.toDomain
import com.appsbase.jetcode.core.data.mapper.toEntity
import com.appsbase.jetcode.core.data.remote.LearningApiService
import com.appsbase.jetcode.core.database.dao.LearningDao
import com.appsbase.jetcode.core.domain.model.Lesson
import com.appsbase.jetcode.core.domain.model.Skill
import com.appsbase.jetcode.core.domain.model.Topic
import com.appsbase.jetcode.core.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber

/**
 * Implementation of LearningRepository following Clean Architecture
 * Handles data from both local database and remote API
 */
class LearningRepositoryImpl(
    private val learningDao: LearningDao,
    private val apiService: LearningApiService,
) : LearningRepository {

    override fun getSkills(): Flow<Result<List<Skill>>> {
        return learningDao.getAllSkills().map { entities ->
            try {
                val skills = entities.map { it.toDomain() }
                Result.Success(skills)
            } catch (e: Exception) {
                Timber.e(e, "Error mapping skills to domain")
                Result.Error(AppError.DataError.ParseError(e))
            }
        }.catch { e ->
            Timber.e(e, "Error getting skills from database")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

    override fun getSkillById(skillId: String): Flow<Result<Skill>> {
        return learningDao.getSkillById(skillId).map { entity ->
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
        }.catch { e ->
            Timber.e(e, "Error getting skill by id from database")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

    override fun getTopicsForSkill(skillId: String): Flow<Result<List<Topic>>> {
        return learningDao.getSkillById(skillId).flatMapLatest { skillEntity ->
            try {
                if (skillEntity != null) {
                    // Get topics using the topicIds from the skill
                    if (skillEntity.topicIds.isNotEmpty()) {
                        learningDao.getTopicsByIds(skillEntity.topicIds).map { topicEntities ->
                            Result.Success(topicEntities.map { it.toDomain() })
                        }
                    } else {
                        flowOf(Result.Success(emptyList()))
                    }
                } else {
                    flowOf(Result.Error(AppError.DataError.NotFound))
                }
            } catch (e: Exception) {
                Timber.e(e, "Error getting topics for skill")
                flowOf(Result.Error(AppError.DataError.ParseError(e)))
            }
        }.catch { e ->
            Timber.e(e, "Error getting topics for skill from database")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

    override fun getLessonsForTopic(topicId: String): Flow<Result<List<Lesson>>> {
        return learningDao.getTopicById(topicId).flatMapLatest { topicEntity ->
            try {
                if (topicEntity != null) {
                    // Get lessons using the lessonIds from the topic
                    if (topicEntity.lessonIds.isNotEmpty()) {
                        learningDao.getLessonsByIds(topicEntity.lessonIds).map { lessonEntities ->
                            Result.Success(lessonEntities.map { it.toDomain() })
                        }
                    } else {
                        flowOf(Result.Success(emptyList()))
                    }
                } else {
                    flowOf(Result.Error(AppError.DataError.NotFound))
                }
            } catch (e: Exception) {
                Timber.e(e, "Error getting lessons for topic")
                flowOf(Result.Error(AppError.DataError.ParseError(e)))
            }
        }.catch { e ->
            Timber.e(e, "Error getting lessons for topic from database")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

    override fun getLessonById(lessonId: String): Flow<Result<Lesson>> {
        return learningDao.getLessonById(lessonId).map { entity ->
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
        }.catch { e ->
            Timber.e(e, "Error getting lesson by id from database")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

    override suspend fun syncContent(): Result<Unit> {
        return try {
            // Fetch all content types independently
            val skills = fetchContent("skills") { apiService.getSkills() }
            val topics = fetchContent("topics") { apiService.getTopics() }
            val lessons = fetchContent("lessons") { apiService.getLessons() }
            val materials = fetchContent("materials") { apiService.getMaterials() }
            val practices = fetchContent("practices") { apiService.getPractices() }

            // Check if at least one fetch succeeded
            val hasAnySuccess =
                listOf(skills, topics, lessons, materials, practices).any { it != null }
            if (!hasAnySuccess) {
                Timber.e("All remote data fetches failed")
                return Result.Error(AppError.NetworkError.Unknown(Exception("All remote data sources failed")))
            }

            // Update database with successfully fetched data (reverse order for FK constraints)
            try {
                practices?.let {
                    learningDao.clearPractices()
                    learningDao.insertPractices(it.map { practice -> practice.toEntity() })
                    Timber.d("Practices synced successfully")
                }

                materials?.let {
                    learningDao.clearMaterials()
                    learningDao.insertMaterials(it.map { material -> material.toEntity() })
                    Timber.d("Materials synced successfully")
                }

                lessons?.let {
                    learningDao.clearLessons()
                    learningDao.insertLessons(it.map { lesson -> lesson.toEntity() })
                    Timber.d("Lessons synced successfully")
                }

                topics?.let {
                    learningDao.clearTopics()
                    learningDao.insertTopics(it.map { topic -> topic.toEntity() })
                    Timber.d("Topics synced successfully")
                }

                skills?.let {
                    learningDao.clearSkills()
                    learningDao.insertSkills(it.map { skill -> skill.toEntity() })
                    Timber.d("Skills synced successfully")
                }

                // Log summary
                val successCount =
                    listOf(skills, topics, lessons, materials, practices).count { it != null }
                val totalCount = 5

                if (successCount < totalCount) {
                    Timber.w("Partial sync completed: $successCount/$totalCount content types synced")
                } else {
                    Timber.d("Complete sync successful: all $totalCount content types updated")
                }

                Result.Success(Unit)

            } catch (e: Exception) {
                Timber.e(e, "Database update failed during sync")
                Result.Error(AppError.DataError.DatabaseError)
            }

        } catch (e: Exception) {
            Timber.e(e, "Unexpected error during sync")
            Result.Error(AppError.NetworkError.Unknown(e))
        }
    }

    private suspend fun <T> fetchContent(type: String, fetcher: suspend () -> List<T>): List<T>? {
        return try {
            val result = fetcher()
            Timber.d("Successfully fetched $type")
            result
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch $type")
            null
        }
    }

    override fun searchContent(query: String): Flow<Result<List<Any>>> {
        return learningDao.searchSkills(query).map { skillEntities ->
            try {
                val results = mutableListOf<Any>()
                results.addAll(skillEntities.map { it.toDomain() })
                Result.Success(results as List<Any>)
            } catch (e: Exception) {
                Timber.e(e, "Error searching content")
                Result.Error(AppError.DataError.ParseError(e))
            }
        }.catch { e ->
            Timber.e(e, "Error in search query")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }
}
