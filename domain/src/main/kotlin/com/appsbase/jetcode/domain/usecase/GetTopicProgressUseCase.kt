package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.TopicProgress
import com.appsbase.jetcode.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting topic progress for a specific user and topic
 */
const val DummyUserId = "dummy_user_id"

class GetTopicProgressUseCase(
    private val progressRepository: ProgressRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(
        topicId: String,
    ): Flow<Result<TopicProgress?>> {
        return progressRepository.getTopicProgressById(
            topicId = topicId,
            userId = DummyUserId,
        ).flowOn(dispatcherProvider.io)
    }
}
