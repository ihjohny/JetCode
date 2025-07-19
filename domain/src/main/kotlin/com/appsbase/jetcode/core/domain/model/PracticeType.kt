package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class PracticeType {
    MCQ, CODE_CHALLENGE, OUTPUT_PREDICTION, FILL_BLANK
}
