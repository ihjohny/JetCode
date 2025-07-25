package com.appsbase.jetcode.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Skill(
    val id: String,
    val name: String,
    val description: String,
    val iconUrl: String?,
    val topicIds: List<String> = emptyList(),
    val difficulty: Difficulty,
    val estimatedDuration: Int,
) : Content()
