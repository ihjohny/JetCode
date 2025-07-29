package com.appsbase.jetcode.feature.practice.presentation.practice_list

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.domain.usecase.GetUserAllPracticeSetsUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Practice List screen following MVI pattern
 */
class PracticeListViewModel(
    private val getUserAllPracticeSetsUseCase: GetUserAllPracticeSetsUseCase,
) : BaseViewModel<PracticeListState, PracticeListIntent, PracticeListEffect>(
    initialState = PracticeListState()
) {

    override fun handleIntent(intent: PracticeListIntent) {
        when (intent) {
            is PracticeListIntent.LoadPracticeSets -> loadPracticeSets()
            is PracticeListIntent.TabChanged -> changeTab(intent.tab)
            is PracticeListIntent.PracticeSetClicked -> navigateToPractice(intent.practiceSetId)
            is PracticeListIntent.Retry -> loadPracticeSets()
        }
    }

    private fun loadPracticeSets() {
        updateState(currentState().copy(isLoading = true, error = null))

        viewModelScope.launch {
            getUserAllPracticeSetsUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                isLoading = false,
                                userPracticeSets = result.data,
                                error = null
                            )
                        )
                        Timber.d("Loaded ${result.data.size} practice sets")
                    }

                    is Result.Error -> {
                        val errorMessage = when (val exception = result.exception) {
                            is AppError -> exception.getUserMessage()
                            else -> exception.message ?: "Failed to load practice sets"
                        }
                        updateState(
                            currentState().copy(
                                isLoading = false,
                                error = errorMessage
                            )
                        )
                        sendEffect(PracticeListEffect.ShowError(errorMessage))
                        Timber.e("Error loading practice sets: $errorMessage")
                    }
                }
            }
        }
    }

    private fun changeTab(tab: PracticeTab) {
        updateState(currentState().copy(selectedTab = tab))
    }

    private fun navigateToPractice(practiceSetId: String) {
        sendEffect(PracticeListEffect.NavigateToPractice(practiceSetId))
    }
}
