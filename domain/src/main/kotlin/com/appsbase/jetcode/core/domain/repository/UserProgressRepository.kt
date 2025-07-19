package com.appsbase.jetcode.core.domain.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.domain.model.UserProgress
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user progress
 */
interface UserProgressRepository {

    /**
     * Get user progress
     */
    fun getUserProgress(userId: String): Flow<Result<UserProgress>>

    /**
     * Update lesson progress
     */
    suspend fun updateLessonProgress(
        userId: String,
        lessonId: String,
        score: Int,
        timeSpent: Int
    ): Result<Unit>

    /**
     * Mark lesson as completed
     */
    suspend fun completeLessonProgress(
        userId: String,
        lessonId: String,
        score: Int
    ): Result<Unit>

    /**
     * Get progress for specific skill
     */
    fun getSkillProgress(
        userId: String,
        skillId: String
    ): Flow<Result<UserProgress>>

    /**
     * Sync progress with remote
     */
    suspend fun syncProgress(userId: String): Result<Unit>
}
