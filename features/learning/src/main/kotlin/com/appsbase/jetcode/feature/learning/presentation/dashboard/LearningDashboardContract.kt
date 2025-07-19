package com.appsbase.jetcode.feature.learning.presentation.dashboard

import com.appsbase.jetcode.core.common.mvi.UiState
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.domain.model.Skill

/**
 * MVI contracts for Learning Dashboard
 */

data class LearningDashboardState(
    val isLoading: Boolean = false,
    val skills: List<Skill> = emptyList(),
    val searchQuery: String = "",
    val filteredSkills: List<Skill> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
) : UiState

sealed class LearningDashboardIntent : UiIntent {
    data object LoadSkills : LearningDashboardIntent()
    data object RefreshSkills : LearningDashboardIntent()
    data class SearchSkills(val query: String) : LearningDashboardIntent()
    data class SkillClicked(val skillId: String) : LearningDashboardIntent()
    data object ProfileClicked : LearningDashboardIntent()
    data object RetryClicked : LearningDashboardIntent()
}

sealed class LearningDashboardEffect : UiEffect {
    data class NavigateToSkillDetail(val skillId: String) : LearningDashboardEffect()
    data object NavigateToProfile : LearningDashboardEffect()
    data class ShowError(val message: String) : LearningDashboardEffect()
}
