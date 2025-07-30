package com.appsbase.jetcode.feature.dashboard.presentation

import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiState

/**
 * MVI contracts for Dashboard
 */
data class DashboardState(
    val isLoading: Boolean = false,
    val error: String? = null,
) : UiState

sealed class DashboardIntent : UiIntent {
    data object EnterLearning : DashboardIntent()
    data object EnterPractice : DashboardIntent()
    data object ProfileClicked : DashboardIntent()
}

sealed class DashboardEffect : UiEffect {
    data object NavigateToLearning : DashboardEffect()
    data object NavigateToPractice : DashboardEffect()
    data object NavigateToProfile : DashboardEffect()
    data class ShowError(val message: String) : DashboardEffect()
}

