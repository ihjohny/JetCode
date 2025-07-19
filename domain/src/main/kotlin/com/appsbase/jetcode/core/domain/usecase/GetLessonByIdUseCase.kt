package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.model.Lesson
import com.appsbase.jetcode.core.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting a lesson by ID with materials and practices
 */
class GetLessonByIdUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(lessonId: String): Flow<Result<Lesson>> {
        return learningRepository.getLessonById(lessonId)
            .flowOn(dispatcherProvider.io)
    }
}
