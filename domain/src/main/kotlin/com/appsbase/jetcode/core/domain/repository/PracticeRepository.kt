package com.appsbase.jetcode.core.domain.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.domain.model.Content
import com.appsbase.jetcode.core.domain.model.PracticeSet
import com.appsbase.jetcode.core.domain.model.Quiz
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for practice content
 */
interface PracticeRepository {

    /**
     * Get all available practice sets
     */
    fun getPracticeSets(): Flow<Result<List<PracticeSet>>>

    /**
     * Get practice set by ID
     */
    fun getPracticeSetById(practiceSetId: String): Flow<Result<PracticeSet>>

    /**
     * Get quizzes by list of IDs
     */
    fun getQuizzesByIds(quizIds: List<String>): Flow<Result<List<Quiz>>>

    /**
     * Sync practice content from remote source
     */
    suspend fun syncPracticeContent(): Result<Unit>

    /**
     * Search practice content
     */
    fun searchPracticeContent(query: String): Flow<Result<List<Content>>>
}
