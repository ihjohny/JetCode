package com.appsbase.jetcode.feature.dashboard.presentation

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.domain.usecase.GetUserAllPracticeSetsUseCase
import com.appsbase.jetcode.domain.usecase.GetUserAllSkillsUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Dashboard screen following MVI pattern
 */
class DashboardViewModel(
    private val getUserAllSkillsUseCase: GetUserAllSkillsUseCase,
    private val getUserAllPracticeSetsUseCase: GetUserAllPracticeSetsUseCase,
) : BaseViewModel<DashboardState, DashboardIntent, DashboardEffect>(
    initialState = DashboardState()
) {

    init {
        handleIntent(DashboardIntent.LoadDashboard)
    }

    override fun handleIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.LoadDashboard -> loadDashboard()
            is DashboardIntent.RefreshDashboard -> refreshDashboard()
            is DashboardIntent.SkillClicked -> navigateToSkill(intent.skillId)
            is DashboardIntent.PracticeClicked -> navigateToPractice(intent.practiceSetId)
            is DashboardIntent.ViewAllSkillsClicked -> navigateToSkillsList()
            is DashboardIntent.ViewAllPracticeClicked -> navigateToPracticeList()
            is DashboardIntent.ViewPracticeHistoryClicked -> navigateToPracticeHistory()
            is DashboardIntent.ProfileClicked -> navigateToProfile()
            is DashboardIntent.RetryClicked -> loadDashboard()
        }
    }

    private fun loadDashboard() {
        updateState(currentState().copy(isLoading = true, error = null))

        viewModelScope.launch {
            try {
                combine(
                    getUserAllSkillsUseCase(), getUserAllPracticeSetsUseCase()
                ) { skillsResult, practiceResult ->
                    Triple(skillsResult, practiceResult, System.currentTimeMillis())
                }.collect { (skillsResult, practiceResult, _) ->
                    when {
                        skillsResult is Result.Success && practiceResult is Result.Success -> {
                            updateState(
                                currentState().copy(
                                    isLoading = false,
                                    userSkills = skillsResult.data,
                                    userPracticeSets = practiceResult.data,
                                    error = null
                                )
                            )
                            Timber.d("Dashboard loaded: ${skillsResult.data.size} skills, ${practiceResult.data.size} practice sets")
                        }

                        skillsResult is Result.Error -> handleError(skillsResult.exception)
                        practiceResult is Result.Error -> handleError(practiceResult.exception)
                    }
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun refreshDashboard() {
        loadDashboard()
    }

    private fun handleError(exception: Throwable) {
        val errorMessage = when (exception) {
            is AppError -> exception.getUserMessage()
            else -> exception.message ?: "An unexpected error occurred"
        }
        updateState(currentState().copy(isLoading = false, error = errorMessage))
        sendEffect(DashboardEffect.ShowError(errorMessage))
        Timber.e(exception, "Dashboard error: $errorMessage")
    }

    private fun navigateToSkill(skillId: String) {
        sendEffect(DashboardEffect.NavigateToSkill(skillId))
    }

    private fun navigateToPractice(practiceSetId: String) {
        sendEffect(DashboardEffect.NavigateToPractice(practiceSetId))
    }

    private fun navigateToSkillsList() {
        sendEffect(DashboardEffect.NavigateToSkillsList)
    }

    private fun navigateToPracticeList() {
        sendEffect(DashboardEffect.NavigateToPracticeList)
    }

    private fun navigateToPracticeHistory() {
        sendEffect(DashboardEffect.NavigateToPracticeHistory)
    }

    private fun navigateToProfile() {
        sendEffect(DashboardEffect.NavigateToProfile)
    }
}
