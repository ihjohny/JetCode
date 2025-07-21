package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Topic(
    val id: String,
    val name: String,
    val description: String,
    val materialIds: List<String> = emptyList(),
    val practiceSetId: String,
    val duration: Int,
) : Content()
