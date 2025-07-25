package com.appsbase.jetcode.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PracticeSet(
    val id: String,
    val name: String,
    val description: String,
    val quizIds: List<String>,
) : Content()
