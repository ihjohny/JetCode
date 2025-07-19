package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.model.UserProgress
import com.appsbase.jetcode.core.domain.repository.UserProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting user progress
 */
class GetUserProgressUseCase(
    private val userProgressRepository: UserProgressRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(userId: String): Flow<Result<UserProgress>> {
        return userProgressRepository.getUserProgress(userId)
            .flowOn(dispatcherProvider.io)
    }
}
