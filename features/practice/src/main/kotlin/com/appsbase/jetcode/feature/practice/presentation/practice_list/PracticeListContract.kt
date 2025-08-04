package com.appsbase.jetcode.feature.practice.presentation.practice_list

import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiState
import com.appsbase.jetcode.domain.model.UserPracticeSet

/**
 * MVI contracts for Practice List screen
 */

data class PracticeListState(
    val isLoading: Boolean = false,
    val userPracticeSets: List<UserPracticeSet> = emptyList(),
    val selectedTab: PracticeTab = PracticeTab.INCOMPLETE,
    val error: String? = null,
) : UiState {

    val incompletePracticeSets: List<UserPracticeSet> = userPracticeSets.filter { !it.isCompleted }

    val completedPracticeSets: List<UserPracticeSet> = userPracticeSets.filter { it.isCompleted }
}

enum class PracticeTab {
    INCOMPLETE,
    COMPLETED
}

sealed class PracticeListIntent : UiIntent {
    data object LoadPracticeSets : PracticeListIntent()
    data class TabChanged(val tab: PracticeTab) : PracticeListIntent()
    data class PracticeSetClicked(val practiceSetId: String) : PracticeListIntent()
    data object Retry : PracticeListIntent()
}

sealed class PracticeListEffect : UiEffect {
    data class NavigateToPractice(val practiceSetId: String) : PracticeListEffect()
    data class ShowError(val message: String) : PracticeListEffect()
}
