package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.TopicProgress
import com.appsbase.jetcode.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * Use case for updating topic progress
 */
class UpdateTopicProgressUseCase(
    private val progressRepository: ProgressRepository,
    private val getTopicProgressUseCase: GetTopicProgressUseCase,
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(
        topicId: String,
        updatedMaterialIndex: Int,
    ): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            val currentProgressResult = getTopicProgressUseCase(topicId).first()

            when (currentProgressResult) {
                is Result.Success -> {
                    val currentProgress = currentProgressResult.data
                    if (currentProgress != null && updatedMaterialIndex <= currentProgress.currentMaterialIndex) {
                        return@withContext Result.Error(
                            AppError.BusinessError.Custom("Cannot update progress to a previous material index.")
                        )
                    }

                    val userId = DummyUserId
                    val progressId = "${userId}_${topicId}"
                    val progress = TopicProgress(
                        id = progressId,
                        userId = userId,
                        topicId = topicId,
                        currentMaterialIndex = updatedMaterialIndex,
                        updatedAt = System.currentTimeMillis()
                    )

                    progressRepository.upsertProgress(progress)
                }

                is Result.Error -> currentProgressResult
            }
        }
    }
}
