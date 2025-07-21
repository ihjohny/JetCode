package com.appsbase.jetcode.feature.learning.presentation.skill_detail

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.core.domain.usecase.GetSkillByIdUseCase
import com.appsbase.jetcode.core.domain.usecase.GetTopicsByIdsUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Skill Detail screen following MVI pattern
 */
class SkillDetailViewModel(
    private val getSkillByIdUseCase: GetSkillByIdUseCase,
    private val getTopicsByIdsUseCase: GetTopicsByIdsUseCase
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
            getSkillByIdUseCase(skillId).collect { skillResult ->
                when (skillResult) {
                    is Result.Loading -> {
                        updateState(currentState().copy(isLoading = true))
                    }

                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                skill = skillResult.data,
                                isLoading = false,
                            )
                        )
                        loadTopics(topicIds = skillResult.data.topicIds)
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
            }
        }
    }

    private fun loadTopics(topicIds: List<String>) {
        viewModelScope.launch {
            getTopicsByIdsUseCase(topicIds = topicIds).collect { topicsResult ->
                when (topicsResult) {
                    is Result.Success -> {
                        updateState(currentState().copy(topics = topicsResult.data))
                        Timber.d("Topics loaded successfully: ${topicsResult.data.size} topics")
                    }

                    is Result.Error -> {
                        when (val exception = topicsResult.exception) {
                            is AppError -> exception.getUserMessage()
                            else -> exception.message ?: "Failed to load topics"
                        }
                        Timber.e(topicsResult.exception, "Error loading topics")
                        // Don't show error for topics if skill loaded successfully
                    }

                    is Result.Loading -> {
                        // Keep current state
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
