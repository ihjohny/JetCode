package com.appsbase.jetcode.feature.learning.presentation.skill_list

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.domain.model.Skill
import com.appsbase.jetcode.domain.usecase.GetSkillsUseCase
import com.appsbase.jetcode.domain.usecase.SyncContentUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class SkillListViewModel(
    private val getSkillsUseCase: GetSkillsUseCase,
    private val syncContentUseCase: SyncContentUseCase,
) : BaseViewModel<SkillListState, SkillListIntent, SkillListEffect>(
    initialState = SkillListState()
) {

    init {
        handleIntent(SkillListIntent.LoadSkills)
    }

    override fun handleIntent(intent: SkillListIntent) {
        when (intent) {
            is SkillListIntent.LoadSkills -> loadSkills()
            is SkillListIntent.SyncSkills -> syncSkills()
            is SkillListIntent.SearchSkills -> searchSkills(intent.query)
            is SkillListIntent.SkillClicked -> handleSkillClick(intent.skillId)
            is SkillListIntent.ProfileClicked -> handleProfileClick()
            is SkillListIntent.RetryClicked -> loadSkills()
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
                        Timber.Forest.d("Skills loaded successfully: ${result.data.size} skills")
                    }

                    is Result.Error -> {
                        val errorMessage = when (val exception = result.exception) {
                            is AppError -> exception.getUserMessage()
                            else -> exception.message ?: "An unexpected error occurred"
                        }
                        updateState(
                            currentState().copy(
                                isLoading = false, error = errorMessage
                            )
                        )
                        sendEffect(SkillListEffect.ShowError(errorMessage))
                        Timber.Forest.e(result.exception, "Error loading skills")
                    }
                }
            }
        }
    }

    private fun syncSkills() {
        updateState(currentState().copy(isSyncing = true))

        viewModelScope.launch {
            // First sync content from remote
            when (val syncResult = syncContentUseCase()) {
                is Result.Success -> {
                    updateState(currentState().copy(isSyncing = false))
                    Timber.Forest.d("Content synced successfully")
                }

                is Result.Error -> {
                    updateState(currentState().copy(isSyncing = false))
                    val errorMessage = when (val exception = syncResult.exception) {
                        is AppError -> exception.getUserMessage()
                        else -> exception.message ?: "An unexpected error occurred"
                    }
                    sendEffect(SkillListEffect.ShowError("Sync failed: $errorMessage"))
                    Timber.Forest.e(syncResult.exception, "Error syncing content")
                }

                is Result.Loading -> {
                    // Keep the refreshing state - loading is handled by the isRefreshing flag
                    Timber.Forest.d("Syncing content...")
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
        Timber.Forest.d("Search query: '$query', found ${filteredSkills.size} results")
    }

    private fun filterSkills(
        skills: List<Skill>, query: String
    ): List<Skill> {
        if (query.isBlank()) return skills

        return skills.filter { skill ->
            skill.name.contains(query, ignoreCase = true) || skill.description.contains(
                query, ignoreCase = true
            )
        }
    }

    private fun handleSkillClick(skillId: String) {
        sendEffect(SkillListEffect.NavigateToSkillDetail(skillId))
        Timber.Forest.d("Skill clicked: $skillId")
    }

    private fun handleProfileClick() {
        sendEffect(SkillListEffect.NavigateToProfile)
        Timber.Forest.d("Profile clicked")
    }
}