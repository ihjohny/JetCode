package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.Quiz
import com.appsbase.jetcode.domain.repository.PracticeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting quizzes by list of IDs
 */
class GetQuizzesByIdsUseCase(
    private val practiceRepository: PracticeRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(quizIds: List<String>): Flow<Result<List<Quiz>>> {
        return practiceRepository.getQuizzesByIds(quizIds)
            .flowOn(dispatcherProvider.io)
    }
}
