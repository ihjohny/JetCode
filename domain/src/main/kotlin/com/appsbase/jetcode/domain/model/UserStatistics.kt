package com.appsbase.jetcode.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserStatistics(
    val totalSkillsEnrolled: Int = 0,
    val totalSkillsCompleted: Int = 0,
    val totalTopicsCompleted: Int = 0,
    val totalPracticeSetsCompleted: Int = 0,
    val totalCorrectAnswers: Int = 0,
    val totalQuestions: Int = 0,
    val averageScore: Float = 0f,
    val recentActivities: List<RecentActivity> = emptyList(),
) {
    val overallAccuracy: Float
        get() = if (totalQuestions > 0) {
            (totalCorrectAnswers.toFloat() / totalQuestions) * 100
        } else 0f
}

@Serializable
data class RecentActivity(
    val id: String,
    val type: ActivityType,
    val title: String,
    val description: String,
    val timestamp: Long,
    val metadata: Map<String, String> = emptyMap(),
)

@Serializable
enum class ActivityType {
    SKILL_STARTED, SKILL_COMPLETED, TOPIC_COMPLETED, PRACTICE_COMPLETED, MATERIAL_READ
}
