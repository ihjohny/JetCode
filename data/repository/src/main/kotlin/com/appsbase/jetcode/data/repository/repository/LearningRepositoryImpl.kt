package com.appsbase.jetcode.data.repository.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.util.fetchContent
import com.appsbase.jetcode.data.repository.mapper.toDomain
import com.appsbase.jetcode.data.repository.mapper.toEntity
import com.appsbase.jetcode.data.repository.remote.LearningApiService
import com.appsbase.jetcode.data.database.dao.LearningDao
import com.appsbase.jetcode.domain.model.Content
import com.appsbase.jetcode.domain.model.Material
import com.appsbase.jetcode.domain.model.Skill
import com.appsbase.jetcode.domain.model.Topic
import com.appsbase.jetcode.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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

    override fun getTopicById(topicId: String): Flow<Result<Topic>> {
        return learningDao.getTopicById(topicId).map { entity ->
            try {
                if (entity != null) {
                    Result.Success(entity.toDomain())
                } else {
                    Result.Error(AppError.DataError.NotFound)
                }
            } catch (e: Exception) {
                Timber.e(e, "Error mapping topic to domain")
                Result.Error(AppError.DataError.ParseError(e))
            }
        }.catch { e ->
            Timber.e(e, "Error getting topic by id from database")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

    override fun getTopicsByIds(topicIds: List<String>): Flow<Result<List<Topic>>> {
        return if (topicIds.isEmpty()) {
            flowOf(Result.Success(emptyList()))
        } else {
            learningDao.getTopicsByIds(topicIds).map { entities ->
                try {
                    val topics = entities.map { it.toDomain() }
                    Result.Success(topics)
                } catch (e: Exception) {
                    Timber.e(e, "Error mapping topics to domain")
                    Result.Error(AppError.DataError.ParseError(e))
                }
            }.catch { e ->
                Timber.e(e, "Error getting topics from database")
                emit(Result.Error(AppError.DataError.DatabaseError))
            }
        }
    }

    override fun getMaterialsByIds(materialIds: List<String>): Flow<Result<List<Material>>> {
        return if (materialIds.isEmpty()) {
            flowOf(Result.Success(emptyList()))
        } else {
            learningDao.getMaterialsByIds(materialIds).map { entities ->
                try {
                    val materials = entities.map { it.toDomain() }
                    Result.Success(materials)
                } catch (e: Exception) {
                    Timber.e(e, "Error mapping materials to domain")
                    Result.Error(AppError.DataError.ParseError(e))
                }
            }.catch { e ->
                Timber.e(e, "Error getting materials from database")
                emit(Result.Error(AppError.DataError.DatabaseError))
            }
        }
    }

    override suspend fun syncLearningContent(): Result<Unit> {
        return try {
            // Fetch all content types independently
            val skills = fetchContent("skills") { apiService.getSkills() }

            // Fetch all topic IDs from skills to get topics
            val allTopicIds = skills?.flatMap { it.topicIds }?.distinct() ?: emptyList()
            val topics = if (allTopicIds.isNotEmpty()) {
                fetchContent("topics") { apiService.getTopicsByIds(allTopicIds) }
            } else null

            // Fetch all material and practice IDs from topics
            val allMaterialIds = topics?.flatMap { it.materialIds }?.distinct() ?: emptyList()

            val materials = if (allMaterialIds.isNotEmpty()) {
                fetchContent("materials") { apiService.getMaterialsByIds(allMaterialIds) }
            } else null

            // Check if at least one fetch succeeded
            val hasAnySuccess = listOf(skills, topics, materials).any { it != null }
            if (!hasAnySuccess) {
                Timber.e("All remote data fetches failed")
                return Result.Error(AppError.NetworkError.Unknown(Exception("All remote data sources failed")))
            }

            // Update database with successfully fetched data (reverse order for FK constraints)
            try {
                skills?.let {
                    learningDao.clearSkills()
                    learningDao.insertSkills(it.map { skill -> skill.toEntity() })
                    Timber.d("Skills synced successfully")
                }

                topics?.let {
                    learningDao.clearTopics()
                    learningDao.insertTopics(it.map { topic -> topic.toEntity() })
                    Timber.d("Topics synced successfully")
                }

                materials?.let {
                    learningDao.clearMaterials()
                    learningDao.insertMaterials(it.map { material -> material.toEntity() })
                    Timber.d("Materials synced successfully")
                }
                Timber.i("Learning Content sync completed successfully")
                Result.Success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "Error saving synced learning content to database")
                Result.Error(AppError.DataError.DatabaseError)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error during learning content sync")
            Result.Error(AppError.NetworkError.Unknown(e))
        }
    }

    override fun searchLearningContent(query: String): Flow<Result<List<Content>>> {
        return combine(
            learningDao.searchSkills(query),
            learningDao.searchTopics(query),
            learningDao.searchMaterials(query),
        ) { skillEntities, topicEntities, materialEntities ->
            try {
                val results = mutableListOf<Content>()
                results.addAll(skillEntities.map { it.toDomain() })
                results.addAll(topicEntities.map { it.toDomain() })
                results.addAll(materialEntities.map { it.toDomain() })
                Result.Success(results)
            } catch (e: Exception) {
                Timber.e(e, "Error mapping search results to domain")
                Result.Error(AppError.DataError.ParseError(e))
            }
        }.catch { e ->
            Timber.e(e, "Error searching content")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

}
