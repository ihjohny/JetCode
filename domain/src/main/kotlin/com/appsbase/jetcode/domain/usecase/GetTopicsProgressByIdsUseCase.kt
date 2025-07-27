package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.TopicProgress
import com.appsbase.jetcode.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting topic progress for multiple topics for a specific user
 */
class GetTopicsProgressByIdsUseCase(
    private val progressRepository: ProgressRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(
        topicIds: List<String>,
    ): Flow<Result<List<TopicProgress>>> {
        return progressRepository.getProgressByTopicsIdsAndUser(
            topicIds = topicIds,
            userId = DummyUserId,
        ).flowOn(dispatcherProvider.io)
    }
}
