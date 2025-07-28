package com.appsbase.jetcode.feature.learning.presentation.skill_detail

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.domain.model.UserSkill
import com.appsbase.jetcode.domain.model.UserTopic
import com.appsbase.jetcode.domain.usecase.GetSkillByIdUseCase
import com.appsbase.jetcode.domain.usecase.GetSkillProgressUseCase
import com.appsbase.jetcode.domain.usecase.GetTopicsByIdsUseCase
import com.appsbase.jetcode.domain.usecase.GetTopicsProgressByIdsUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Skill Detail screen following MVI pattern
 */
class SkillDetailViewModel(
    private val getSkillByIdUseCase: GetSkillByIdUseCase,
    private val getSkillProgressUseCase: GetSkillProgressUseCase,
    private val getTopicsByIdsUseCase: GetTopicsByIdsUseCase,
    private val getTopicsProgressByIdsUseCase: GetTopicsProgressByIdsUseCase,
) : BaseViewModel<SkillDetailState, SkillDetailIntent, SkillDetailEffect>(
    initialState = SkillDetailState()
) {

    override fun handleIntent(intent: SkillDetailIntent) {
        when (intent) {
            is SkillDetailIntent.LoadSkill -> loadSkill(intent.skillId)
            is SkillDetailIntent.TopicClicked -> handleTopicClick(intent.topicId)
            is SkillDetailIntent.RetryClicked -> retryLoading(intent.skillId)
        }
    }

    private fun loadSkill(skillId: String) {
        updateState(currentState().copy(isLoading = true, error = null))

        viewModelScope.launch {
            combine(
                getSkillByIdUseCase(skillId),
                getSkillProgressUseCase(skillId)
            ) { skillResult, progressResult ->
                when (skillResult) {
                    is Result.Success -> {
                        val progress = when (progressResult) {
                            is Result.Success -> progressResult.data
                            is Result.Error -> null
                        }

                        updateState(
                            currentState().copy(
                                userSkill = UserSkill(
                                    skill = skillResult.data,
                                    completedMaterial = progress?.completedMaterial ?: 0,
                                    totalMaterial = progress?.totalMaterial ?: skillResult.data.topicIds.size,
                                ),
                                isLoading = false,
                            )
                        )
                        loadTopicsWithProgress(topicIds = skillResult.data.topicIds)
                        Timber.d("Skill loaded successfully: ${skillResult.data.name}")
                    }

                    is Result.Error -> {
                        val errorMessage = when (val exception = skillResult.exception) {
                            is AppError -> exception.getUserMessage()
                            else -> exception.message ?: "Failed to load skill"
                        }
                        updateState(
                            currentState().copy(
                                isLoading = false,
                                error = errorMessage,
                            )
                        )
                        sendEffect(SkillDetailEffect.ShowError(errorMessage))
                        Timber.e(skillResult.exception, "Error loading skill")
                    }
                }
            }.collect { }
        }
    }

    private fun loadTopicsWithProgress(topicIds: List<String>) {
        viewModelScope.launch {
            combine(
                getTopicsByIdsUseCase(topicIds = topicIds),
                getTopicsProgressByIdsUseCase(topicIds = topicIds)
            ) { topicsResult, progressResult ->
                when (topicsResult) {
                    is Result.Success -> {
                        val progressResult =
                            progressResult as? Result.Success ?: Result.Success(emptyList())
                        val userTopics = topicsResult.data.map { topic ->
                            val progress = progressResult.data.find { it.topicId == topic.id }
                            UserTopic(
                                topic = topic,
                                currentMaterialIndex = progress?.currentMaterialIndex ?: NoProgress
                            )
                        }
                        Result.Success(userTopics)
                    }

                    is Result.Error -> topicsResult
                }
            }.collect { combinedResult ->
                when (combinedResult) {
                    is Result.Success -> {
                        updateState(currentState().copy(userTopics = combinedResult.data))
                        Timber.d("Topics loaded successfully: ${combinedResult.data.size} topics")
                    }

                    is Result.Error -> {
                        Timber.e(combinedResult.exception, "Error loading topics")
                    }
                }
            }
        }
    }

    private fun handleTopicClick(topicId: String) {
        sendEffect(SkillDetailEffect.NavigateToTopic(topicId))
        Timber.d("Topic clicked: $topicId")
    }

    private fun retryLoading(skillId: String) {
        loadSkill(skillId)
    }
}
