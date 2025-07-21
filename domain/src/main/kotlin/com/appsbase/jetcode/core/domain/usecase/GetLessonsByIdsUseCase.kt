package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.model.Lesson
import com.appsbase.jetcode.core.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting lessons for a specific topic using lesson IDs list
 */
class GetLessonsByIdsUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(lessonIds: List<String>): Flow<Result<List<Lesson>>> {
        return learningRepository.getLessonsByIds(lessonIds = lessonIds)
            .flowOn(dispatcherProvider.io)
    }
}
