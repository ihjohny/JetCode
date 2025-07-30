package com.appsbase.jetcode.feature.dashboard.presentation

import com.appsbase.jetcode.core.common.mvi.BaseViewModel

class DashboardViewModel : BaseViewModel<DashboardState, DashboardIntent, DashboardEffect>(
    initialState = DashboardState()
) {
    override fun handleIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.EnterLearning -> sendEffect(DashboardEffect.NavigateToLearning)
            is DashboardIntent.EnterPractice -> sendEffect(DashboardEffect.NavigateToPractice)
            is DashboardIntent.ProfileClicked -> sendEffect(DashboardEffect.NavigateToProfile)
        }
    }
}
