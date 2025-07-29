package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.PracticeSessionStatistics
import com.appsbase.jetcode.domain.model.PracticeSetResult
import com.appsbase.jetcode.domain.repository.PracticeResultRepository
import kotlinx.coroutines.withContext

/**
 * Use case for saving practice result to database
 */
class SavePracticeResultUseCase(
    private val practiceResultRepository: PracticeResultRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    suspend operator fun invoke(
        practiceSetId: String,
        practiceSessionStatistics: PracticeSessionStatistics,
    ): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            val mId = "${DummyUserId}_${practiceSetId}"

            val practiceSetResult = PracticeSetResult(
                id = mId,
                userId = DummyUserId,
                practiceSetId = practiceSetId,
                practiceSessionStatistics = practiceSessionStatistics,
                updatedAt = System.currentTimeMillis(),
            )

            practiceResultRepository.upsertPracticeResult(practiceSetResult)
        }
    }
}
