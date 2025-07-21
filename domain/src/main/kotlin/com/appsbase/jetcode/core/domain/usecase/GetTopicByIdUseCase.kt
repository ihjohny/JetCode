package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.model.Topic
import com.appsbase.jetcode.core.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting a specific topic by ID
 */
class GetTopicByIdUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(topicId: String): Flow<Result<Topic>> {
        return learningRepository.getTopicById(topicId)
            .flowOn(dispatcherProvider.io)
    }
}
