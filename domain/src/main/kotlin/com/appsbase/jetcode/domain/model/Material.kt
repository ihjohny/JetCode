package com.appsbase.jetcode.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class MaterialType {
    TEXT, MARKDOWN, CODE, IMAGE, VIDEO,
}

@Serializable
data class Material(
    val id: String,
    val type: MaterialType,
    val title: String,
    val content: String,
    val metadata: Map<String, String> = emptyMap(),
) : Content()
