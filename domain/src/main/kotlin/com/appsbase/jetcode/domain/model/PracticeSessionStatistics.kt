package com.appsbase.jetcode.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PracticeSessionStatistics(
    val totalQuizzes: Int,
    val correctAnswers: Int,
    val averageTimeSeconds: Float = 0F,
) {
    val wrongAnswers: Int get() = totalQuizzes - correctAnswers

    val scorePercentage: Int
        get() = if (totalQuizzes > 0) {
            (correctAnswers * 100) / totalQuizzes
        } else 0

    val formattedAverageTime: String
        get() = if (averageTimeSeconds % 1 == 0f) {
            "${averageTimeSeconds.toInt()}s"
        } else {
            "%.1f".format(averageTimeSeconds) + "s"
        }
}
