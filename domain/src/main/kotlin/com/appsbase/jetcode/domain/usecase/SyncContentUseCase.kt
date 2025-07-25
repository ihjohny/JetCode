package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.domain.repository.LearningRepository
import com.appsbase.jetcode.domain.repository.PracticeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

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
        val minDelayAsync = async { delay(1000) }

        // Wait for both operations and minimum delay to complete
        val practiceResult = practiceSync.await()
        val learningResult = learningSync.await()
        minDelayAsync.await() // Ensure minimum delay is respected

        when {
            practiceResult is Result.Error -> practiceResult
            learningResult is Result.Error -> learningResult
            else -> Result.Success(Unit)
        }
    }
}
