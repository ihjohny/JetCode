package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.model.UserProgress
import com.appsbase.jetcode.core.domain.repository.UserProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Use case for getting user progress
 */
class GetUserProgressUseCase(
    private val userProgressRepository: UserProgressRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(userId: String): Flow<Result<UserProgress>> {
        return userProgressRepository.getUserProgress(userId)
            .flowOn(dispatcherProvider.io)
    }
}

/**
 * Use case for completing a lesson
 */
class CompleteLessonUseCase(
    private val userProgressRepository: UserProgressRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(
        userId: String,
        lessonId: String,
        score: Int
    ): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            userProgressRepository.completeLessonProgress(userId, lessonId, score)
        }
    }
}

/**
 * Use case for updating lesson progress
 */
class UpdateLessonProgressUseCase(
    private val userProgressRepository: UserProgressRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(
        userId: String,
        lessonId: String,
        score: Int,
        timeSpent: Int
    ): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            userProgressRepository.updateLessonProgress(userId, lessonId, score, timeSpent)
        }
    }
}

/**
 * Use case for syncing user progress
 */
class SyncUserProgressUseCase(
    private val userProgressRepository: UserProgressRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            userProgressRepository.syncProgress(userId)
        }
    }
}
