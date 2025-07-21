package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting materials for a specific lesson using material IDs list
 */
class GetMaterialsForLessonUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(lessonId: String): Flow<Result<List<Material>>> {
        return learningRepository.getLessonById(lessonId)
            .flatMapLatest { lessonResult ->
                when (lessonResult) {
                    is Result.Success -> {
                        // Use the materialIds list directly from the lesson
                        learningRepository.getMaterialsByIds(lessonResult.data.materialIds)
                    }
                    is Result.Error -> {
                        flowOf(Result.Error(lessonResult.error))
                    }
                }
            }
            .flowOn(dispatcherProvider.io)
    }
}
