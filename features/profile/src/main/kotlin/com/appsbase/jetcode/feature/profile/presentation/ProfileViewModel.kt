package com.appsbase.jetcode.feature.profile.presentation

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.domain.usecase.GetUserStatisticsUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Profile screen following MVI pattern
 */
class ProfileViewModel(
    private val getUserStatisticsUseCase: GetUserStatisticsUseCase,
) : BaseViewModel<ProfileState, ProfileIntent, ProfileEffect>(
    initialState = ProfileState()
) {

    init {
        handleIntent(ProfileIntent.LoadProfile)
    }

    override fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.LoadProfile -> loadProfile()
            is ProfileIntent.RetryLoad -> loadProfile()
        }
    }

    private fun loadProfile() {
        updateState(currentState().copy(isLoading = true, error = null))

        viewModelScope.launch {
            try {
                // Load user statistics
                getUserStatisticsUseCase().collect { statisticsResult ->
                    when (statisticsResult) {
                        is Result.Success -> {
                            updateState(
                                currentState().copy(
                                    isLoading = false,
                                    userStatistics = statisticsResult.data,
                                    error = null
                                )
                            )
                            Timber.d("Profile loaded successfully")
                        }

                        is Result.Error -> handleError(statisticsResult.exception)
                    }
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(exception: Throwable) {
        val errorMessage = when (exception) {
            is AppError -> exception.getUserMessage()
            else -> exception.message ?: "An unexpected error occurred"
        }
        updateState(currentState().copy(isLoading = false, error = errorMessage))
        sendEffect(ProfileEffect.ShowError(errorMessage))
        Timber.e(exception, "Profile error: $errorMessage")
    }
}
