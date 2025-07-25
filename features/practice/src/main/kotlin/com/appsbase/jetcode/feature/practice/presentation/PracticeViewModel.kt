package com.appsbase.jetcode.feature.practice.presentation

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.domain.model.Quiz
import com.appsbase.jetcode.domain.usecase.GetPracticeSetByIdUseCase
import com.appsbase.jetcode.domain.usecase.GetQuizzesByIdsUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Practice screen following MVI pattern
 */
class PracticeViewModel(
    private val getPracticeSetByIdUseCase: GetPracticeSetByIdUseCase,
    private val getQuizzesByIdsUseCase: GetQuizzesByIdsUseCase,
) : BaseViewModel<PracticeState, PracticeIntent, PracticeEffect>(
    initialState = PracticeState()
) {

    override fun handleIntent(intent: PracticeIntent) {
        when (intent) {
            is PracticeIntent.LoadPracticeSet -> loadPracticeSet(intent.practiceSetId)
            is PracticeIntent.AnswerChanged -> updateAnswer(intent.answer)
            is PracticeIntent.NextQuiz -> nextQuiz()
            is PracticeIntent.PreviousQuiz -> previousQuiz()
            is PracticeIntent.ViewAnswers -> viewAnswers()
            is PracticeIntent.RestartPractice -> restartPractice()
            is PracticeIntent.RetryClicked -> loadPracticeSet(intent.practiceSetId)
        }
    }

    private fun loadPracticeSet(practiceSetId: String) {
        updateState(currentState().copy(isLoading = true, error = null))

        viewModelScope.launch {
            getPracticeSetByIdUseCase(practiceSetId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                practiceSet = result.data, isLoading = false
                            )
                        )
                        loadQuizzes(result.data.quizIds)
                    }

                    is Result.Error -> handleError(result.exception)
                    is Result.Loading -> {}
                }
            }
        }
    }

    private fun loadQuizzes(quizIds: List<String>) {
        viewModelScope.launch {
            getQuizzesByIdsUseCase(quizIds).collect { result ->
                when (result) {
                    is Result.Success -> {
                        updateState(currentState().copy(quizzes = result.data))
                        Timber.d("Loaded ${result.data.size} quizzes")
                    }

                    is Result.Error -> handleError(result.exception)
                    is Result.Loading -> {}
                }
            }
        }
    }

    private fun handleError(exception: Throwable) {
        val errorMessage = when (exception) {
            is AppError -> exception.getUserMessage()
            else -> exception.message ?: "An unexpected error occurred"
        }
        updateState(currentState().copy(isLoading = false, error = errorMessage))
        sendEffect(PracticeEffect.ShowError(errorMessage))
        Timber.e("Error: $errorMessage")
    }

    private fun updateAnswer(answer: String) {
        updateState(currentState().copy(userAnswer = answer))
    }

    private fun nextQuiz() {
        val state = currentState()
        val currentQuiz = state.currentQuiz ?: return

        // Submit answer if not already answered
        if (state.quizResults.size <= state.currentQuizIndex) {
            submitAnswer(currentQuiz, state)
        } else {
            navigateToNextQuiz(state)
        }
    }

    private fun submitAnswer(currentQuiz: Quiz, state: PracticeState) {
        val userAnswer = state.userAnswer.trim()
        val timeTaken = System.currentTimeMillis() - state.currentQuizStartTime

        val quizResult = PracticeState.QuizResult(
            quiz = currentQuiz,
            userAnswer = userAnswer.ifEmpty { "No answer" },
            timeTakenMillis = timeTaken,
        )

        val updatedResults = state.quizResults + quizResult
        val isLastQuiz = state.currentQuizIndex == state.quizzes.size - 1

        if (isLastQuiz) {
            updateState(state.copy(quizResults = updatedResults, isCompleted = true))
            sendEffect(PracticeEffect.QuizCompleted)
        } else {
            updateState(
                state.copy(
                    quizResults = updatedResults,
                    currentQuizIndex = state.currentQuizIndex + 1,
                    userAnswer = "",
                    currentQuizStartTime = System.currentTimeMillis()
                )
            )
        }
    }

    private fun navigateToNextQuiz(state: PracticeState) {
        val nextIndex = state.currentQuizIndex + 1
        if (nextIndex < state.quizzes.size) {
            updateState(
                state.copy(
                    currentQuizIndex = nextIndex,
                    userAnswer = "",
                    currentQuizStartTime = System.currentTimeMillis()
                )
            )
        }
    }

    private fun previousQuiz() {
        val state = currentState()
        val prevIndex = state.currentQuizIndex - 1

        if (prevIndex >= 0) {
            val previousResult = state.quizResults.getOrNull(prevIndex)
            updateState(
                state.copy(
                    currentQuizIndex = prevIndex,
                    userAnswer = previousResult?.userAnswer ?: "",
                    currentQuizStartTime = System.currentTimeMillis()
                )
            )
        }
    }

    private fun viewAnswers() {
        updateState(currentState().copy(showAllAnswers = !currentState().showAllAnswers))
    }

    private fun restartPractice() {
        updateState(
            currentState().copy(
                currentQuizIndex = 0,
                userAnswer = "",
                quizResults = emptyList(),
                isCompleted = false,
                showAllAnswers = false,
                startTime = System.currentTimeMillis(),
                currentQuizStartTime = System.currentTimeMillis(),
            )
        )
    }
}
