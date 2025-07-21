package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Topic(
    val id: String,
    val name: String,
    val description: String,
    val materialIds: List<String> = emptyList(),
    val practiceIds: List<String> = emptyList(),
    val duration: Int, // in minutes - total estimated duration for all materials and practices
    val isCompleted: Boolean = false,
    val progress: Float = 0f
) : Content()
