package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.model.Practice
import com.appsbase.jetcode.core.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting practices for a specific lesson using practice IDs list
 */
class GetPracticesForLessonUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(lessonId: String): Flow<Result<List<Practice>>> {
        return learningRepository.getLessonById(lessonId)
            .flatMapLatest { lessonResult ->
                when (lessonResult) {
                    is Result.Success -> {
                        // Use the practiceIds list directly from the lesson
                        learningRepository.getPracticesByIds(lessonResult.data.practiceIds)
                    }
                    is Result.Error -> {
                        flowOf(Result.Error(lessonResult.error))
                    }
                }
            }
            .flowOn(dispatcherProvider.io)
    }
}
