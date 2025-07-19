package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TopicProgress(
    val topicId: String,
    val lessonProgresses: List<LessonProgress> = emptyList(),
    val isCompleted: Boolean = false
)
