package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PracticeSet(
    val id: String,
    val name: String,
    val description: String,
    val quizIds: List<String>,
    val attributes: List<String>? = null,
) : Content()
