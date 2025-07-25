package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.repository.PreferencesRepository
import kotlinx.coroutines.withContext

/**
 * Use case for getting onboarding completion status
 */
class GetOnboardingStatusUseCase(
    private val preferencesRepository: PreferencesRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(): Result<Boolean> {
        return withContext(dispatcherProvider.io) {
            preferencesRepository.getShouldShowOnboarding()
        }
    }
}
