package com.appsbase.jetcode.main

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.domain.usecase.CompleteOnboardingUseCase
import com.appsbase.jetcode.domain.usecase.GetOnboardingStatusUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
) : BaseViewModel<MainState, MainIntent, MainEffect>(
    initialState = MainState()
) {

    init {
        handleIntent(MainIntent.CheckOnboardingStatus)
    }

    override fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.CheckOnboardingStatus -> checkOnboardingStatus()
            is MainIntent.CompleteOnboarding -> completeOnboarding()
        }
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            when (val result = getOnboardingStatusUseCase()) {
                is Result.Success -> {
                    updateState(
                        currentState().copy(
                            shouldShowOnboarding = result.data,
                        )
                    )
                    Timber.d("Onboarding status loaded: shouldShow=${result.data}")
                }

                is Result.Error -> {
                    updateState(
                        currentState().copy(
                            shouldShowOnboarding = false,
                        )
                    )
                    Timber.e(result.exception, "Error checking onboarding status")
                }

                is Result.Loading -> {}
            }
        }
    }

    private fun completeOnboarding() {
        viewModelScope.launch {
            when (val result = completeOnboardingUseCase()) {
                is Result.Success -> {
                    Timber.d("Onboarding completed successfully")
                }

                is Result.Error -> {
                    Timber.e(result.exception, "Error completing onboarding")
                }

                is Result.Loading -> {}
            }
        }
    }
}
