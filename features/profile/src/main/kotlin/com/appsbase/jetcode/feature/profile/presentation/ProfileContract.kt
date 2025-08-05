package com.appsbase.jetcode.feature.profile.presentation

import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiState
import com.appsbase.jetcode.domain.model.UserStatistics

/**
 * MVI contracts for Profile Screen
 */
data class ProfileState(
    val isLoading: Boolean = false,
    val userStatistics: UserStatistics? = null,
    val error: String? = null,
) : UiState

sealed class ProfileIntent : UiIntent {
    object LoadProfile : ProfileIntent()
    object RetryLoad : ProfileIntent()
}

sealed class ProfileEffect : UiEffect {
    data class ShowError(val message: String) : ProfileEffect()
}
