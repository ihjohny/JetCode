package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.repository.LearningRepository

/**
 * Use case for syncing content from remote
 */
class SyncContentUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(): Result<Unit> {
        return learningRepository.syncContent()
    }
}
