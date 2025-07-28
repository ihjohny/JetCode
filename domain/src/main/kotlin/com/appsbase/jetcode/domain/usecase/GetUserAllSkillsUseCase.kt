package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.NoProgress
import com.appsbase.jetcode.domain.model.UserSkill
import com.appsbase.jetcode.domain.repository.LearningRepository
import com.appsbase.jetcode.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting user all skills with progress
 * Combines skills data with progress data to create UserSkill objects
 */
class GetUserAllSkillsUseCase(
    private val learningRepository: LearningRepository,
    private val progressRepository: ProgressRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(): Flow<Result<List<UserSkill>>> {
        return combine(
            learningRepository.getAllSkills(),
            progressRepository.getAllSkillsProgress(userId = DummyUserId),
        ) { skillsResult, progressResult ->
            when (skillsResult) {
                is Result.Success -> {
                    val progressData = when (progressResult) {
                        is Result.Success -> progressResult.data
                        is Result.Error -> emptyList()
                    }

                    val userTopics = skillsResult.data.map { skill ->
                        val progress = progressData.find { it.skillId == skill.id }
                        UserSkill(
                            skill = skill,
                            completedMaterial = progress?.completedMaterial ?: NoProgress,
                            totalMaterial = progress?.totalMaterial ?: 0,
                        )
                    }
                    Result.Success(userTopics)
                }

                is Result.Error -> skillsResult
            }
        }.flowOn(dispatcherProvider.io)
    }
}
