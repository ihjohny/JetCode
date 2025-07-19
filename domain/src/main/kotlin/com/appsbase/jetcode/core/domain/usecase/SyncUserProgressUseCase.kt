package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.repository.UserProgressRepository
import kotlinx.coroutines.withContext

/**
 * Use case for syncing user progress
 */
class SyncUserProgressUseCase(
    private val userProgressRepository: UserProgressRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            userProgressRepository.syncProgress(userId)
        }
    }
}
