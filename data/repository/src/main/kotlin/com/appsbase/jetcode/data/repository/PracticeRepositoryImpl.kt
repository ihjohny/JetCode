package com.appsbase.jetcode.data.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.util.fetchContent
import com.appsbase.jetcode.data.mapper.toDomain
import com.appsbase.jetcode.data.mapper.toEntity
import com.appsbase.jetcode.data.remote.PracticeApiService
import com.appsbase.jetcode.data.database.dao.PracticeDao
import com.appsbase.jetcode.core.domain.model.Content
import com.appsbase.jetcode.core.domain.model.PracticeSet
import com.appsbase.jetcode.core.domain.model.Quiz
import com.appsbase.jetcode.core.domain.repository.PracticeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * Implementation of PracticeRepository following Clean Architecture
 * Handles data from both local database and remote API
 */
class PracticeRepositoryImpl(
    private val practiceDao: PracticeDao,
    private val apiService: PracticeApiService,
) : PracticeRepository {

    override fun getPracticeSets(): Flow<Result<List<PracticeSet>>> {
        return practiceDao.getAllPracticeSets().map { entities ->
            try {
                val practiceSets = entities.map { it.toDomain() }
                Result.Success(practiceSets)
            } catch (e: Exception) {
                Timber.e(e, "Error mapping practice sets to domain")
                Result.Error(AppError.DataError.ParseError(e))
            }
        }.catch { e ->
            Timber.e(e, "Error getting practice sets from database")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

    override fun getPracticeSetById(practiceSetId: String): Flow<Result<PracticeSet>> {
        return practiceDao.getPracticeSetById(practiceSetId).map { entity ->
            try {
                if (entity != null) {
                    Result.Success(entity.toDomain())
                } else {
                    Result.Error(AppError.DataError.NotFound)
                }
            } catch (e: Exception) {
                Timber.e(e, "Error mapping practice set to domain")
                Result.Error(AppError.DataError.ParseError(e))
            }
        }.catch { e ->
            Timber.e(e, "Error getting practice set by id from database")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

    override fun getQuizzesByIds(quizIds: List<String>): Flow<Result<List<Quiz>>> {
        return if (quizIds.isEmpty()) {
            flowOf(Result.Success(emptyList()))
        } else {
            practiceDao.getQuizzesByIds(quizIds).map { entities ->
                try {
                    val quizzes = entities.map { it.toDomain() }
                    Result.Success(quizzes)
                } catch (e: Exception) {
                    Timber.e(e, "Error mapping quizzes to domain")
                    Result.Error(AppError.DataError.ParseError(e))
                }
            }.catch { e ->
                Timber.e(e, "Error getting quizzes from database")
                emit(Result.Error(AppError.DataError.DatabaseError))
            }
        }
    }

    override suspend fun syncPracticeContent(): Result<Unit> {
        return try {
            // Fetch practice sets
            val practiceSets = fetchContent("practice sets") { apiService.getPracticeSets() }

            // Fetch all quiz IDs from practice sets to get quizzes
            val allQuizIds = practiceSets?.flatMap { it.quizIds }?.distinct() ?: emptyList()
            val quizzes = if (allQuizIds.isNotEmpty()) {
                fetchContent("quizzes") { apiService.getQuizzesByIds(allQuizIds) }
            } else null

            // Check if at least one fetch succeeded
            val hasAnySuccess = listOf(practiceSets, quizzes).any { it != null }
            if (!hasAnySuccess) {
                Timber.e("All remote practice data fetches failed")
                return Result.Error(AppError.NetworkError.Unknown(Exception("All remote practice data sources failed")))
            }

            // Update database with successfully fetched data (reverse order for FK constraints)
            try {
                quizzes?.let {
                    practiceDao.clearQuizzes()
                    practiceDao.insertQuizzes(it.map { quiz -> quiz.toEntity() })
                    Timber.d("Quizzes synced successfully")
                }

                practiceSets?.let {
                    practiceDao.clearPracticeSets()
                    practiceDao.insertPracticeSets(it.map { practiceSet -> practiceSet.toEntity() })
                    Timber.d("Practice sets synced successfully")
                }

                Timber.i("Practice content sync completed successfully")
                Result.Success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "Error saving synced practice content to database")
                Result.Error(AppError.DataError.DatabaseError)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error during practice content sync")
            Result.Error(AppError.NetworkError.Unknown(e))
        }
    }

    override fun searchPracticeContent(query: String): Flow<Result<List<Content>>> {
        return practiceDao.searchPracticeSets(query).map { practiceSetEntities ->
            try {
                val results = mutableListOf<Content>()
                results.addAll(practiceSetEntities.map { it.toDomain() })
                Result.Success(results)
            } catch (e: Exception) {
                Timber.e(e, "Error mapping practice search results to domain")
                Result.Error(AppError.DataError.ParseError(e))
            }
        }.catch { e ->
            Timber.e(e, "Error searching practice content")
            emit(Result.Error(AppError.DataError.DatabaseError))
        }
    }

}
