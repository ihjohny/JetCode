package com.appsbase.jetcode.feature.practice.presentation

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.core.domain.usecase.GetPracticeSetByIdUseCase
import com.appsbase.jetcode.core.domain.usecase.GetQuizzesByIdsUseCase
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
            is PracticeIntent.ShowAllAnswers -> showAllAnswers()
            is PracticeIntent.HideAllAnswers -> hideAllAnswers()
            is PracticeIntent.RestartPractice -> restartPractice()
            is PracticeIntent.RetryClicked -> retryLoading(intent.practiceSetId)
        }
    }

    private fun loadPracticeSet(practiceSetId: String) {
        updateState(
            currentState().copy(
                isLoading = true,
                error = null,
                startTime = System.currentTimeMillis()
            )
        )

        viewModelScope.launch {
            // Load practice set details
            getPracticeSetByIdUseCase(practiceSetId).collect { practiceSetResult ->
                when (practiceSetResult) {
                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                practiceSet = practiceSetResult.data,
                                isLoading = false,
                            )
                        )
                        loadQuizzes(practiceSetResult.data.quizIds)
                        Timber.d("Practice set loaded successfully: ${practiceSetResult.data.name}")
                    }

                    is Result.Error -> {
                        val errorMessage = when (val exception = practiceSetResult.exception) {
                            is AppError -> exception.getUserMessage()
                            else -> exception.message ?: "An unexpected error occurred"
                        }
                        updateState(
                            currentState().copy(
                                isLoading = false,
                                error = errorMessage,
                            )
                        )
                        sendEffect(PracticeEffect.ShowError(errorMessage))
                        Timber.e("Error loading practice set: $errorMessage")
                    }

                    Result.Loading -> {
                        // Loading state already set
                    }
                }
            }
        }
    }

    private fun loadQuizzes(quizIds: List<String>) {
        viewModelScope.launch {
            getQuizzesByIdsUseCase(quizIds).collect { quizzesResult ->
                when (quizzesResult) {
                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                quizzes = quizzesResult.data
                            )
                        )
                        Timber.d("Quizzes loaded successfully: ${quizzesResult.data.size} items")
                    }

                    is Result.Error -> {
                        val errorMessage = when (val exception = quizzesResult.exception) {
                            is AppError -> exception.getUserMessage()
                            else -> exception.message ?: "Failed to load quizzes"
                        }
                        Timber.e("Error loading quizzes: $errorMessage")
                    }

                    is Result.Loading -> {
                        // Loading handled by practice set loading
                    }
                }
            }
        }
    }

    private fun updateAnswer(answer: String) {
        updateState(currentState().copy(userAnswer = answer))
    }

    private fun nextQuiz() {
        val state = currentState()
        val currentQuiz = state.currentQuiz

        // Submit answer for current quiz if it hasn't been answered yet
        if (currentQuiz != null && state.quizResults.size <= state.currentQuizIndex) {
            // If no answer is provided, treat it as incorrect
            val userAnswer = state.userAnswer.trim()
            val isCorrect = if (userAnswer.isEmpty()) {
                false // No answer selected = wrong
            } else {
                when (currentQuiz.type) {
                    com.appsbase.jetcode.core.domain.model.QuizType.MCQ -> {
                        userAnswer == currentQuiz.correctAnswer
                    }

                    else -> {
                        userAnswer.equals(currentQuiz.correctAnswer.trim(), ignoreCase = true)
                    }
                }
            }

            val timeTaken = System.currentTimeMillis() - state.startTime
            val quizResult = QuizResult(
                quiz = currentQuiz,
                userAnswer = if (userAnswer.isEmpty()) "No answer" else userAnswer,
                isCorrect = isCorrect,
                timeTaken = timeTaken
            )

            val updatedResults = state.quizResults + quizResult

            // Check if this was the last quiz
            val isLastQuiz = state.currentQuizIndex == state.quizzes.size - 1

            if (isLastQuiz) {
                // Complete the practice
                updateState(
                    state.copy(
                        quizResults = updatedResults,
                        isCompleted = true
                    )
                )
                sendEffect(PracticeEffect.QuizCompleted)
                Timber.d("All quizzes completed for practice set: ${state.practiceSet?.name}")
                return
            } else {
                // Move to next quiz
                updateState(
                    state.copy(
                        quizResults = updatedResults,
                        currentQuizIndex = state.currentQuizIndex + 1,
                        userAnswer = "",
                        startTime = System.currentTimeMillis()
                    )
                )
            }

            Timber.d("Answer submitted: ${if (isCorrect) "Correct" else "Incorrect"} - User answer: '${userAnswer}'")
        } else {
            // Just navigate to next quiz if already answered
            val nextIndex = state.currentQuizIndex + 1
            if (nextIndex < state.quizzes.size) {
                updateState(
                    state.copy(
                        currentQuizIndex = nextIndex,
                        userAnswer = "",
                        startTime = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    private fun previousQuiz() {
        val state = currentState()
        val prevIndex = state.currentQuizIndex - 1

        if (prevIndex >= 0) {
            // Find the previous result if it exists
            val previousResult = state.quizResults.getOrNull(prevIndex)

            updateState(
                state.copy(
                    currentQuizIndex = prevIndex,
                    userAnswer = previousResult?.userAnswer ?: "",
                    startTime = System.currentTimeMillis()
                )
            )
        }
    }

    private fun showAllAnswers() {
        updateState(currentState().copy(showAllAnswers = true))
    }

    private fun hideAllAnswers() {
        updateState(currentState().copy(showAllAnswers = false))
    }

    private fun restartPractice() {
        val state = currentState()
        updateState(
            state.copy(
                currentQuizIndex = 0,
                userAnswer = "",
                quizResults = emptyList(),
                isCompleted = false,
                showAllAnswers = false,
                startTime = System.currentTimeMillis()
            )
        )
    }

    private fun retryLoading(practiceSetId: String) {
        loadPracticeSet(practiceSetId)
    }
}
