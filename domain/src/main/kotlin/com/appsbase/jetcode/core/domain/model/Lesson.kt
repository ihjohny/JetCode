package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val id: String,
    val topicId: String,
    val title: String,
    val description: String,
    val materialIds: List<String> = emptyList(),
    val practiceIds: List<String> = emptyList(),
    val order: Int,
    val duration: Int, // in minutes
    val isCompleted: Boolean = false,
    val score: Int? = null,
)
