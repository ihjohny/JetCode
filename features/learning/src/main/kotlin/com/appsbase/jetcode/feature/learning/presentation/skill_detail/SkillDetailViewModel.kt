package com.appsbase.jetcode.feature.learning.presentation.skill_detail

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.domain.model.NoProgress
import com.appsbase.jetcode.domain.model.UserSkill
import com.appsbase.jetcode.domain.usecase.GetSkillByIdUseCase
import com.appsbase.jetcode.domain.usecase.GetSkillProgressUseCase
import com.appsbase.jetcode.domain.usecase.GetUserTopicsByIdsUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Skill Detail screen following MVI pattern
 */
class SkillDetailViewModel(
    private val getSkillByIdUseCase: GetSkillByIdUseCase,
    private val getSkillProgressUseCase: GetSkillProgressUseCase,
    private val getUserTopicsByIdsUseCase: GetUserTopicsByIdsUseCase,
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
                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                userSkill = UserSkill(
                                    skill = skillResult.data,
                                    completedMaterial = NoProgress,
                                    totalMaterial = 0,
                                ),
                                isLoading = false,
                            )
                        )
                        loadSkillProgress(skillId = skillId)
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
            }
        }
    }

    private fun loadSkillProgress(skillId: String) {
        viewModelScope.launch {
            getSkillProgressUseCase(skillId = skillId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        val userSkill = currentState().userSkill ?: return@collect
                        updateState(
                            currentState().copy(
                                userSkill = userSkill.copy(
                                    completedMaterial = result.data?.completedMaterial ?: NoProgress,
                                    totalMaterial = result.data?.totalMaterial ?: 0,
                                )
                            )
                        )
                        Timber.d("Skill progress loaded successfully: ${result.data?.completedMaterial}/${result.data?.totalMaterial}")
                    }

                    is Result.Error -> {
                        Timber.e(result.exception, "Error loading skill progress")
                    }
                }
            }
        }
    }

    private fun loadTopicsWithProgress(topicIds: List<String>) {
        viewModelScope.launch {
            getUserTopicsByIdsUseCase(topicIds).collect { result ->
                when (result) {
                    is Result.Success -> {
                        updateState(currentState().copy(userTopics = result.data))
                        Timber.d("User Topics loaded successfully: ${result.data.size} topics")
                    }

                    is Result.Error -> {
                        Timber.e(result.exception, "Error loading user topics")
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
