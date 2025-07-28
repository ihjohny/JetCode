package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.NoProgress
import com.appsbase.jetcode.domain.model.UserTopic
import com.appsbase.jetcode.domain.repository.LearningRepository
import com.appsbase.jetcode.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting user topics with progress for a list of topic IDs
 * Combines topics data with progress data to create UserTopic objects
 */
class GetUserTopicsByIdsUseCase(
    private val learningRepository: LearningRepository,
    private val progressRepository: ProgressRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(topicIds: List<String>): Flow<Result<List<UserTopic>>> {
        return combine(
            learningRepository.getTopicsByIds(topicIds = topicIds),
            progressRepository.getTopicsProgressByIds(
                topicIds = topicIds,
                userId = DummyUserId,
            ),
        ) { topicsResult, progressResult ->
            when (topicsResult) {
                is Result.Success -> {
                    val progressData = when (progressResult) {
                        is Result.Success -> progressResult.data
                        is Result.Error -> emptyList()
                    }

                    val userTopics = topicsResult.data.map { topic ->
                        val progress = progressData.find { it.topicId == topic.id }
                        UserTopic(
                            topic = topic,
                            currentMaterialIndex = progress?.currentMaterialIndex ?: NoProgress,
                        )
                    }
                    Result.Success(userTopics)
                }

                is Result.Error -> topicsResult
            }
        }.flowOn(dispatcherProvider.io)
    }
}
