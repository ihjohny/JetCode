package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class MaterialType {
    TEXT, MARKDOWN, CODE, IMAGE, VIDEO,
}
