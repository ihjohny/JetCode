package com.appsbase.jetcode.feature.learning.presentation.topic_detail

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.core.domain.usecase.GetMaterialsByIdsUseCase
import com.appsbase.jetcode.core.domain.usecase.GetPracticesByIdsUseCase
import com.appsbase.jetcode.core.domain.usecase.GetTopicByIdUseCase
import com.appsbase.jetcode.feature.learning.presentation.topic_detail.TopicDetailEffect.ShowCorrectAnswer
import com.appsbase.jetcode.feature.learning.presentation.topic_detail.TopicDetailEffect.ShowError
import com.appsbase.jetcode.feature.learning.presentation.topic_detail.TopicDetailEffect.ShowIncorrectAnswer
import com.appsbase.jetcode.feature.learning.presentation.topic_detail.TopicDetailEffect.ShowTopicCompleted
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Topic Detail screen following MVI pattern
 */
class TopicDetailViewModel(
    private val getTopicByIdUseCase: GetTopicByIdUseCase,
    private val getMaterialsForTopicUseCase: GetMaterialsByIdsUseCase,
    private val getPracticesForTopicUseCase: GetPracticesByIdsUseCase
) : BaseViewModel<TopicDetailState, TopicDetailIntent, TopicDetailEffect>(
    initialState = TopicDetailState()
) {

    override fun handleIntent(intent: TopicDetailIntent) {
        when (intent) {
            is TopicDetailIntent.LoadTopic -> loadTopic(intent.topicId)
            is TopicDetailIntent.NextMaterial -> nextMaterial()
            is TopicDetailIntent.PreviousMaterial -> previousMaterial()
            is TopicDetailIntent.StartPractice -> startPractice()
            is TopicDetailIntent.NextPractice -> nextPractice()
            is TopicDetailIntent.PreviousPractice -> previousPractice()
            is TopicDetailIntent.SubmitAnswer -> submitAnswer(intent.answer)
            is TopicDetailIntent.CompleteTopic -> completeTopic()
            is TopicDetailIntent.RetryClicked -> retryLoading(intent.topicId)
        }
    }

    private fun loadTopic(topicId: String) {
        updateState(
            currentState().copy(
                isLoading = true,
                error = null,
            )
        )

        viewModelScope.launch {
            // Load topic details
            getTopicByIdUseCase(topicId).collect { topicResult ->
                when (topicResult) {
                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                topic = topicResult.data,
                                isLoading = false,
                            )
                        )
                        loadMaterials(topicResult.data.materialIds)
                        loadPractices(topicResult.data.practiceIds)
                        Timber.d("Topic loaded successfully: ${topicResult.data.name}")
                    }

                    is Result.Error -> {
                        val errorMessage = when (val exception = topicResult.exception) {
                            is AppError -> exception.getUserMessage()
                            else -> exception.message ?: "An unexpected error occurred"
                        }
                        updateState(
                            currentState().copy(
                                isLoading = false,
                                error = errorMessage,
                            )
                        )
                        sendEffect(ShowError(errorMessage))
                        Timber.e("Error loading topic: $errorMessage")
                    }

                    Result.Loading -> {

                    }
                }
            }
        }
    }

    private fun loadMaterials(materialIds: List<String>) {
        viewModelScope.launch {
            getMaterialsForTopicUseCase(materialIds).collect { materialsResult ->
                when (materialsResult) {
                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                materials = materialsResult.data
                            )
                        )
                        Timber.d("Materials loaded successfully: ${materialsResult.data.size} items")
                    }

                    is Result.Error -> {
                        val errorMessage = when (val exception = materialsResult.exception) {
                            is AppError -> exception.getUserMessage()
                            else -> exception.message ?: "Failed to load materials"
                        }
                        Timber.e("Error loading materials: $errorMessage")
                    }

                    is Result.Loading -> {

                    }
                }
            }
        }
    }

    private fun loadPractices(practiceIds: List<String>) {
        viewModelScope.launch {
            getPracticesForTopicUseCase(practiceIds).collect { practicesResult ->
                when (practicesResult) {
                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                practices = practicesResult.data
                            )
                        )
                        Timber.d("Practices loaded successfully: ${practicesResult.data.size} items")
                    }

                    is Result.Error -> {
                        val errorMessage = when (val exception = practicesResult.exception) {
                            is AppError -> exception.getUserMessage()
                            else -> exception.message ?: "Failed to load practices"
                        }
                        Timber.e("Error loading practices: $errorMessage")
                    }

                    is Result.Loading -> {

                    }
                }
            }
        }
    }

    private fun nextMaterial() {
        val state = currentState()
        val nextIndex = state.currentMaterialIndex + 1
        if (nextIndex < state.materials.size) {
            updateState(state.copy(currentMaterialIndex = nextIndex))
        } else {
            // All materials completed, start practices
            startPractice()
        }
    }

    private fun previousMaterial() {
        val state = currentState()
        val prevIndex = state.currentMaterialIndex - 1
        if (prevIndex >= 0) {
            updateState(state.copy(currentMaterialIndex = prevIndex))
        }
    }

    private fun startPractice() {
        val state = currentState()
        if (state.practices.isNotEmpty()) {
            updateState(
                state.copy(
                    isShowingPractice = true, currentPracticeIndex = 0
                )
            )
        } else {
            // No practices, complete the topic
            completeTopic()
        }
    }

    private fun nextPractice() {
        val state = currentState()
        val nextIndex = state.currentPracticeIndex + 1
        if (nextIndex < state.practices.size) {
            updateState(state.copy(currentPracticeIndex = nextIndex))
        } else {
            // All practices completed
            completeTopic()
        }
    }

    private fun previousPractice() {
        val state = currentState()
        val prevIndex = state.currentPracticeIndex - 1
        if (prevIndex >= 0) {
            updateState(state.copy(currentPracticeIndex = prevIndex))
        } else {
            // Go back to materials
            updateState(state.copy(isShowingPractice = false))
        }
    }

    private fun submitAnswer(answer: String) {
        val state = currentState()
        if (state.practices.isNotEmpty() && state.currentPracticeIndex < state.practices.size) {
            val currentPractice = state.practices[state.currentPracticeIndex]
            if (answer == currentPractice.correctAnswer) {
                sendEffect(ShowCorrectAnswer)
            } else {
                sendEffect(ShowIncorrectAnswer(currentPractice.correctAnswer))
            }
        }
    }

    private fun completeTopic() {
        val state = currentState()
        val score = calculateScore(state)
        sendEffect(ShowTopicCompleted(score))
        // Here you would typically save progress to the repository
    }

    private fun calculateScore(state: TopicDetailState): Int {
        // Simple scoring logic - in reality this would be more sophisticated
        return state.practices.sumOf { it.points }
    }

    private fun retryLoading(topicId: String) {
        loadTopic(topicId)
    }
}
