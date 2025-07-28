package com.appsbase.jetcode.domain.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.domain.model.PracticeSetResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for practice results
 */
interface PracticeResultRepository {

    /**
     * Save or update practice result
     */
    suspend fun upsertPracticeResult(result: PracticeSetResult): Result<Unit>

    /**
     * Get practice result by practice set ID and user ID
     */
    fun getPracticeResultById(
        practiceSetId: String,
        userId: String,
    ): Flow<Result<PracticeSetResult?>>

    /**
     * Get all practice results for a user
     */
    fun getAllPracticeResults(userId: String): Flow<Result<List<PracticeSetResult>>>

    /**
     * Get practice results by list of practice set IDs for a specific user
     */
    fun getPracticeResultsByIds(
        practiceSetIds: List<String>,
        userId: String,
    ): Flow<Result<List<PracticeSetResult>>>

    /**
     * Delete practice result for a specific practice set and user
     */
    suspend fun deletePracticeResult(
        practiceSetId: String,
        userId: String,
    ): Result<Unit>

    /**
     * Delete all practice results for a user
     */
    suspend fun deleteAllUserPracticeResults(userId: String): Result<Unit>
}
