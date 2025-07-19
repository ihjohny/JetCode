package com.appsbase.jetcode.feature.learning.presentation.dashboard

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.core.domain.usecase.GetSkillsUseCase
import com.appsbase.jetcode.core.domain.usecase.SyncContentUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class LearningDashboardViewModel(
    private val getSkillsUseCase: GetSkillsUseCase,
    private val syncContentUseCase: SyncContentUseCase,
) : BaseViewModel<LearningDashboardState, LearningDashboardIntent, LearningDashboardEffect>(
    initialState = LearningDashboardState()
) {

    init {
        handleIntent(LearningDashboardIntent.LoadSkills)
    }

    override fun handleIntent(intent: LearningDashboardIntent) {
        when (intent) {
            is LearningDashboardIntent.LoadSkills -> loadSkills()
            is LearningDashboardIntent.RefreshSkills -> refreshSkills()
            is LearningDashboardIntent.SearchSkills -> searchSkills(intent.query)
            is LearningDashboardIntent.SkillClicked -> handleSkillClick(intent.skillId)
            is LearningDashboardIntent.ProfileClicked -> handleProfileClick()
            is LearningDashboardIntent.RetryClicked -> loadSkills()
        }
    }

    private fun loadSkills() {
        updateState(currentState().copy(isLoading = true, error = null))

        viewModelScope.launch {
            getSkillsUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        updateState(currentState().copy(isLoading = true))
                    }

                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                isLoading = false,
                                skills = result.data,
                                filteredSkills = result.data,
                                error = null
                            )
                        )
                        Timber.d("Skills loaded successfully: ${result.data.size} skills")
                    }

                    is Result.Error -> {
                        val errorMessage = when (val exception = result.exception) {
                            is com.appsbase.jetcode.core.common.error.AppError -> exception.getUserMessage()
                            else -> exception.message ?: "An unexpected error occurred"
                        }
                        updateState(
                            currentState().copy(
                                isLoading = false, error = errorMessage
                            )
                        )
                        sendEffect(LearningDashboardEffect.ShowError(errorMessage))
                        Timber.e(result.exception, "Error loading skills")
                    }
                }
            }
        }
    }

    private fun refreshSkills() {
        updateState(currentState().copy(isRefreshing = true))

        viewModelScope.launch {
            // First sync content from remote
            when (val syncResult = syncContentUseCase()) {
                is Result.Success -> {
                    Timber.d("Content synced successfully")
                    // Then load updated skills
                    loadSkillsAfterSync()
                }

                is Result.Error -> {
                    updateState(currentState().copy(isRefreshing = false))
                    val errorMessage = when (val exception = syncResult.exception) {
                        is com.appsbase.jetcode.core.common.error.AppError -> exception.getUserMessage()
                        else -> exception.message ?: "An unexpected error occurred"
                    }
                    sendEffect(LearningDashboardEffect.ShowError("Sync failed: $errorMessage"))
                    Timber.e(syncResult.exception, "Error syncing content")
                }

                is Result.Loading -> {
                    // Keep the refreshing state - loading is handled by the isRefreshing flag
                    Timber.d("Syncing content...")
                }
            }
        }
    }

    private fun loadSkillsAfterSync() {
        viewModelScope.launch {
            getSkillsUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                isRefreshing = false,
                                skills = result.data,
                                filteredSkills = filterSkills(
                                    result.data, currentState().searchQuery
                                )
                            )
                        )
                    }

                    is Result.Error -> {
                        updateState(currentState().copy(isRefreshing = false))
                        val errorMessage = when (val exception = result.exception) {
                            is com.appsbase.jetcode.core.common.error.AppError -> exception.getUserMessage()
                            else -> exception.message ?: "An unexpected error occurred"
                        }
                        sendEffect(LearningDashboardEffect.ShowError(errorMessage))
                    }

                    is Result.Loading -> {
                        // Keep refreshing state
                    }
                }
            }
        }
    }

    private fun searchSkills(query: String) {
        val filteredSkills = filterSkills(currentState().skills, query)
        updateState(
            currentState().copy(
                searchQuery = query, filteredSkills = filteredSkills
            )
        )
        Timber.d("Search query: '$query', found ${filteredSkills.size} results")
    }

    private fun filterSkills(
        skills: List<com.appsbase.jetcode.core.domain.model.Skill>, query: String
    ): List<com.appsbase.jetcode.core.domain.model.Skill> {
        if (query.isBlank()) return skills

        return skills.filter { skill ->
            skill.name.contains(query, ignoreCase = true) || skill.description.contains(
                query,
                ignoreCase = true
            )
        }
    }

    private fun handleSkillClick(skillId: String) {
        sendEffect(LearningDashboardEffect.NavigateToSkillDetail(skillId))
        Timber.d("Skill clicked: $skillId")
    }

    private fun handleProfileClick() {
        sendEffect(LearningDashboardEffect.NavigateToProfile)
        Timber.d("Profile clicked")
    }
}
