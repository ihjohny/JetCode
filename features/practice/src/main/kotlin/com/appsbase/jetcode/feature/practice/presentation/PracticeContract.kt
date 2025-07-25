package com.appsbase.jetcode.feature.practice.presentation

import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiState
import com.appsbase.jetcode.domain.model.PracticeSet
import com.appsbase.jetcode.domain.model.Quiz
import com.appsbase.jetcode.domain.model.QuizType

/**
 * MVI contracts for Practice screen
 */

data class PracticeState(
    val isLoading: Boolean = false,
    val practiceSet: PracticeSet? = null,
    val quizzes: List<Quiz> = emptyList(),
    val currentQuizIndex: Int = 0,
    val userAnswer: String = "",
    val quizResults: List<QuizResult> = emptyList(),
    val isCompleted: Boolean = false,
    val showAllAnswers: Boolean = false,
    val error: String? = null,
    val startTime: Long = System.currentTimeMillis(),
    val currentQuizStartTime: Long = System.currentTimeMillis(),
) : UiState {

    data class QuizResult(
        val quiz: Quiz,
        val userAnswer: String,
        val timeTakenMillis: Long = 0L,
    ) {
        val isCorrect: Boolean
            get() {
                val userAnswer = userAnswer.trim()
                if (userAnswer.isEmpty()) return false

                return when (quiz.type) {
                    QuizType.MCQ -> userAnswer == quiz.correctAnswer
                    else -> userAnswer.equals(quiz.correctAnswer.trim(), ignoreCase = true)
                }
            }

        val formattedTime get() = "${(timeTakenMillis / 1000f).toInt()}s"
    }

    data class QuizStatistics(
        val totalQuizzes: Int,
        val correctAnswers: Int,
        val scorePercentage: Int,
        val averageTimeSeconds: Float = 0F,
    ) {
        val wrongAnswers: Int get() = totalQuizzes - correctAnswers

        val formattedAverageTime: String
            get() = if (averageTimeSeconds % 1 == 0f) {
                "${averageTimeSeconds.toInt()}s"
            } else {
                "%.1f".format(averageTimeSeconds) + "s"
            }
    }

    val currentQuiz: Quiz? get() = quizzes.getOrNull(currentQuizIndex)
    val progressLabel get() = "${(currentQuizIndex + 1).coerceAtMost(quizzes.size)} of ${quizzes.size}"
    val progressValue get() = if (quizzes.isNotEmpty()) (currentQuizIndex + 1f) / quizzes.size else 0f

    val totalTimeMillis: Long
        get() = if (quizResults.isNotEmpty()) {
            quizResults.sumOf { it.timeTakenMillis }
        } else 0

    val statistics: QuizStatistics
        get() {
            val correctCount = quizResults.count { it.isCorrect }
            val scorePercentage = if (quizResults.isNotEmpty()) {
                (correctCount * 100) / quizResults.size
            } else 0

            val averageTimeSeconds = if (quizResults.isNotEmpty()) {
                ((totalTimeMillis / quizResults.size) / 1000).toFloat()
            } else 0f

            return QuizStatistics(
                totalQuizzes = quizResults.size,
                correctAnswers = correctCount,
                scorePercentage = scorePercentage,
                averageTimeSeconds = averageTimeSeconds,
            )
        }
}

sealed class PracticeIntent : UiIntent {
    data class LoadPracticeSet(val practiceSetId: String) : PracticeIntent()
    data class AnswerChanged(val answer: String) : PracticeIntent()
    data object NextQuiz : PracticeIntent()
    data object PreviousQuiz : PracticeIntent()
    data object ViewAnswers : PracticeIntent()
    data object RestartPractice : PracticeIntent()
    data class RetryClicked(val practiceSetId: String) : PracticeIntent()
}

sealed class PracticeEffect : UiEffect {
    data object NavigateBack : PracticeEffect()
    data class ShowError(val message: String) : PracticeEffect()
    data object QuizCompleted : PracticeEffect()
}
