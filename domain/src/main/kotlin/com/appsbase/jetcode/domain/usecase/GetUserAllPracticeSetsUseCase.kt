package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.UserPracticeSet
import com.appsbase.jetcode.domain.repository.PracticeRepository
import com.appsbase.jetcode.domain.repository.PracticeResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting user all practice sets with results
 * Combines practice sets data with practice results data to create UserPracticeSet objects
 */
class GetUserAllPracticeSetsUseCase(
    private val practiceRepository: PracticeRepository,
    private val practiceResultRepository: PracticeResultRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(): Flow<Result<List<UserPracticeSet>>> {
        return combine(
            practiceRepository.getPracticeSets(),
            practiceResultRepository.getAllPracticeResults(userId = DummyUserId),
        ) { practiceSetsResult, practiceResultsResult ->
            when (practiceSetsResult) {
                is Result.Success -> {
                    val practiceResultsData = when (practiceResultsResult) {
                        is Result.Success -> practiceResultsResult.data
                        is Result.Error -> emptyList()
                    }

                    val userPracticeSets = practiceSetsResult.data.map { practiceSet ->
                        val practiceResult = practiceResultsData.find { it.practiceSetId == practiceSet.id }
                        UserPracticeSet(
                            practiceSet = practiceSet,
                            practiceSetResult = practiceResult,
                        )
                    }
                    Result.Success(userPracticeSets)
                }

                is Result.Error -> practiceSetsResult
            }
        }.flowOn(dispatcherProvider.io)
    }
}
