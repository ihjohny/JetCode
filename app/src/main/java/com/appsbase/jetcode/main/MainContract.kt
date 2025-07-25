package com.appsbase.jetcode.main

import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiState

data class MainState(
    val shouldShowOnboarding: Boolean? = null,
) : UiState

sealed class MainIntent : UiIntent {
    data object CheckOnboardingStatus : MainIntent()
    data object CompleteOnboarding : MainIntent()
}

sealed class MainEffect : UiEffect
