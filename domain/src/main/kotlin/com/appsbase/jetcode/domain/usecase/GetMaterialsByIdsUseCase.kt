package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.Material
import com.appsbase.jetcode.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case to get materials for a specific topic
 */
class GetMaterialsByIdsUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(materialIds: List<String>): Flow<Result<List<Material>>> {
        return learningRepository.getMaterialsByIds(materialIds = materialIds)
            .flowOn(dispatcherProvider.io)
    }
}
