package com.appsbase.jetcode.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PracticeSetResult(
    val id: String,
    val userId: String,
    val practiceSetId: String,
    val practiceSessionStatistics: PracticeSessionStatistics,
    val updatedAt: Long,
)