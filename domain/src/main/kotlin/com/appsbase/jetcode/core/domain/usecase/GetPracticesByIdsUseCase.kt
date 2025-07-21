package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.model.Practice
import com.appsbase.jetcode.core.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case to get practices for a specific topic
 */
class GetPracticesByIdsUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(practiceIds: List<String>): Flow<Result<List<Practice>>> {
        return learningRepository.getPracticesByIds(practiceIds = practiceIds)
            .flowOn(dispatcherProvider.io)
    }
}
