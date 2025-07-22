package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.model.PracticeSet
import com.appsbase.jetcode.core.domain.repository.PracticeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting practice set by ID
 */
class GetPracticeSetByIdUseCase(
    private val practiceRepository: PracticeRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(practiceSetId: String): Flow<Result<PracticeSet>> {
        return practiceRepository.getPracticeSetById(practiceSetId)
            .flowOn(dispatcherProvider.io)
    }
}
