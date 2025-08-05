package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.ThemeMode
import com.appsbase.jetcode.domain.repository.PreferencesRepository
import kotlinx.coroutines.withContext

/**
 * Use case for getting current theme mode
 */
class GetThemeModeUseCase(
    private val preferencesRepository: PreferencesRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    suspend operator fun invoke(): Result<ThemeMode> {
        return withContext(dispatcherProvider.io) {
            preferencesRepository.getThemeMode()
        }
    }
}
