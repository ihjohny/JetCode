package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.domain.repository.LearningRepository
import com.appsbase.jetcode.core.domain.repository.PracticeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Use case for syncing content from remote
 */
class SyncContentUseCase(
    private val learningRepository: LearningRepository,
    private val practiceRepository: PracticeRepository,
) {
    suspend operator fun invoke(): Result<Unit> = coroutineScope {
        val practiceSync = async { practiceRepository.syncPracticeContent() }
        val learningSync = async { learningRepository.syncLearningContent() }

        // Wait for both operations to complete
        val practiceResult = practiceSync.await()
        val learningResult = learningSync.await()

        // Return error if any operation failed, otherwise success
        when {
            practiceResult is Result.Error -> practiceResult
            learningResult is Result.Error -> learningResult
            else -> Result.Success(Unit)
        }
    }
}
