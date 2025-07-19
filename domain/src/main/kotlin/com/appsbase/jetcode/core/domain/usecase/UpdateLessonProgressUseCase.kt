package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.repository.UserProgressRepository
import kotlinx.coroutines.withContext

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
