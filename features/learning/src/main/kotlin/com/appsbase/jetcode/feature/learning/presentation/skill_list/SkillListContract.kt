package com.appsbase.jetcode.feature.learning.presentation.skill_list

import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiState
import com.appsbase.jetcode.core.domain.model.Skill

/**
 * MVI contracts for Learning Dashboard
 */

data class SkillListState(
    val isLoading: Boolean = false,
    val skills: List<Skill> = emptyList(),
    val searchQuery: String = "",
    val filteredSkills: List<Skill> = emptyList(),
    val error: String? = null,
    val isSyncing: Boolean = false,
) : UiState

sealed class SkillListIntent : UiIntent {
    data object LoadSkills : SkillListIntent()
    data object SyncSkills : SkillListIntent()
    data class SearchSkills(val query: String) : SkillListIntent()
    data class SkillClicked(val skillId: String) : SkillListIntent()
    data object ProfileClicked : SkillListIntent()
    data object RetryClicked : SkillListIntent()
}

sealed class SkillListEffect : UiEffect {
    data class NavigateToSkillDetail(val skillId: String) : SkillListEffect()
    data object NavigateToProfile : SkillListEffect()
    data class ShowError(val message: String) : SkillListEffect()
}
