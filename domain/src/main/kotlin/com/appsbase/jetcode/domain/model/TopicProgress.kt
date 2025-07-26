package com.appsbase.jetcode.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TopicProgress(
    val id: String,
    val userId: String,
    val topicId: String,
    val currentMaterialIndex: Int,
    val updatedAt: Long,
)