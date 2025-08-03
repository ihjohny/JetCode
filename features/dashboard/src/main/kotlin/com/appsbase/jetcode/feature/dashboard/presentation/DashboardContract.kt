package com.appsbase.jetcode.feature.dashboard.presentation

import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiState
import com.appsbase.jetcode.domain.model.UserPracticeSet
import com.appsbase.jetcode.domain.model.UserSkill

/**
 * MVI contracts for Dashboard screen
 */

data class DashboardState(
    val isLoading: Boolean = false,
    val userSkills: List<UserSkill> = emptyList(),
    val userPracticeSets: List<UserPracticeSet> = emptyList(),
    val userName: String = "Learner",
    val error: String? = null,
) : UiState {

    val featuredSkills: List<UserSkill>
        get() = userSkills.take(3)

    val recentPracticeSets: List<UserPracticeSet>
        get() = userPracticeSets.filter { !it.isCompleted }.take(2)

    val completedPracticeSets: List<UserPracticeSet>
        get() = userPracticeSets.filter { it.isCompleted }.take(3)

    val inProgressSkills: List<UserSkill>
        get() = userSkills.filter { it.progressValue > 0 && it.progressValue < 1f }

    val nextSkillToStart: UserSkill?
        get() = userSkills.firstOrNull { it.progressValue == 0f }
}

sealed class DashboardIntent : UiIntent {
    data object LoadDashboard : DashboardIntent()
    data object RefreshDashboard : DashboardIntent()
    data class SkillClicked(val skillId: String) : DashboardIntent()
    data class PracticeClicked(val practiceSetId: String) : DashboardIntent()
    data object ViewAllSkillsClicked : DashboardIntent()
    data object ViewAllPracticeClicked : DashboardIntent()
    data object ProfileClicked : DashboardIntent()
    data object RetryClicked : DashboardIntent()
}

sealed class DashboardEffect : UiEffect {
    data class NavigateToSkill(val skillId: String) : DashboardEffect()
    data class NavigateToPractice(val practiceSetId: String) : DashboardEffect()
    data object NavigateToSkillsList : DashboardEffect()
    data object NavigateToPracticeList : DashboardEffect()
    data object NavigateToProfile : DashboardEffect()
    data class ShowError(val message: String) : DashboardEffect()
}
