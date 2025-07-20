package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Material(
    val id: String,
    val type: MaterialType,
    val title: String,
    val content: String,
    val order: Int,
    val metadata: Map<String, String> = emptyMap(),
)
