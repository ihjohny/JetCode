package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SkillProgress(
    val skillId: String,
    val topicProgresses: List<TopicProgress> = emptyList(),
    val completedLessons: Int = 0,
    val totalLessons: Int = 0,
    val points: Int = 0,
    val isCompleted: Boolean = false
)
