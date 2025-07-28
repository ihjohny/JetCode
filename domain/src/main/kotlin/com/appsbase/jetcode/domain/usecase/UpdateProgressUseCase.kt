package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.SkillProgress
import com.appsbase.jetcode.domain.model.TopicProgress
import com.appsbase.jetcode.domain.repository.LearningRepository
import com.appsbase.jetcode.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * Use case for updating progress
 */
class UpdateProgressUseCase(
    private val learningRepository: LearningRepository,
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
                        updatedAt = System.currentTimeMillis(),
                    )

                    progressRepository.upsertProgress(progress)
                    incrementSkillProgressUsingTopic(topicId = topicId)
                }

                is Result.Error -> currentProgressResult
            }
        }
    }

    private suspend fun incrementSkillProgressUsingTopic(
        topicId: String,
    ): Result<Unit> {
        val skillsResult = learningRepository.getSkillsByTopicId(topicId).first()

        when (skillsResult) {
            is Result.Success -> {
                val skills = skillsResult.data
                if (skills.isEmpty()) {
                    return Result.Error(AppError.DataError.NotFound)
                }

                // Since topicIds can contain the same topicId in multiple skills,
                // we'll update progress for the first matching skill
                val skill = skills.first()

                // Get current skill progress
                val currentSkillProgressResult = progressRepository.getSkillProgressById(
                    skillId = skill.id,
                    userId = DummyUserId,
                ).first()

                when (currentSkillProgressResult) {
                    is Result.Success -> {
                        val currentSkillProgress = currentSkillProgressResult.data
                        val updatedSkillProgress = if (currentSkillProgress == null) {
                            // Get total material count for the skill
                            val totalMaterialResult =
                                learningRepository.getTotalMaterialCountForSkill(skillId = skill.id)
                            val totalMaterial = when (totalMaterialResult) {
                                is Result.Success -> totalMaterialResult.data
                                is Result.Error -> 0
                            }

                            SkillProgress(
                                id = "${DummyUserId}_${skill.id}",
                                userId = DummyUserId,
                                skillId = skill.id,
                                completedMaterial = 1,
                                totalMaterial = totalMaterial,
                                updatedAt = System.currentTimeMillis()
                            )
                        } else {
                            currentSkillProgress.copy(
                                completedMaterial = currentSkillProgress.completedMaterial + 1,
                                updatedAt = System.currentTimeMillis()
                            )
                        }

                        return progressRepository.upsertSkillProgress(updatedSkillProgress)
                    }

                    is Result.Error -> return currentSkillProgressResult
                }
            }

            is Result.Error -> return skillsResult
        }
    }
}
