package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.Topic
import com.appsbase.jetcode.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting topics for a specific skill using topic IDs list
 */
class GetTopicsByIdsUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(topicIds: List<String>): Flow<Result<List<Topic>>> {
        return learningRepository.getTopicsByIds(topicIds = topicIds)
            .flowOn(dispatcherProvider.io)
    }
}
