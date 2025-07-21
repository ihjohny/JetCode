package com.appsbase.jetcode.di

import com.appsbase.jetcode.core.common.util.DefaultDispatcherProvider
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.data.di.dataModule
import com.appsbase.jetcode.core.data.repository.LearningRepositoryImpl
import com.appsbase.jetcode.core.database.di.databaseModule
import com.appsbase.jetcode.core.domain.repository.LearningRepository
import com.appsbase.jetcode.core.domain.usecase.GetMaterialsByIdsUseCase
import com.appsbase.jetcode.core.domain.usecase.GetSkillByIdUseCase
import com.appsbase.jetcode.core.domain.usecase.GetSkillsUseCase
import com.appsbase.jetcode.core.domain.usecase.GetTopicByIdUseCase
import com.appsbase.jetcode.core.domain.usecase.GetTopicsByIdsUseCase
import com.appsbase.jetcode.core.domain.usecase.SearchContentUseCase
import com.appsbase.jetcode.core.domain.usecase.SyncContentUseCase
import com.appsbase.jetcode.core.network.di.networkModule
import com.appsbase.jetcode.feature.learning.di.learningModule
import org.koin.dsl.module

/**
 * Core Koin modules for dependency injection
 */

val coreModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
}

val repositoryModule = module {
    single<LearningRepository> {
        LearningRepositoryImpl(
            learningDao = get(),
            apiService = get(),
        )
    }

    // UserProgressRepository will be implemented similarly
}

val useCaseModule = module {
    factory {
        GetSkillsUseCase(
            learningRepository = get(),
            dispatcherProvider = get(),
        )
    }

    factory {
        GetSkillByIdUseCase(
            learningRepository = get(),
            dispatcherProvider = get(),
        )
    }

    factory {
        GetTopicByIdUseCase(
            learningRepository = get(),
            dispatcherProvider = get(),
        )
    }

    factory {
        GetTopicsByIdsUseCase(
            learningRepository = get(),
            dispatcherProvider = get(),
        )
    }

    factory {
        GetMaterialsByIdsUseCase(
            learningRepository = get(),
            dispatcherProvider = get(),
        )
    }

    factory {
        SearchContentUseCase(
            learningRepository = get(), dispatcherProvider = get()
        )
    }

    factory {
        SyncContentUseCase(
            learningRepository = get(),
        )
    }
}

/**
 * All app modules for easy registration
 */
val appModules = listOf(
    coreModule,
    networkModule,
    databaseModule,
    dataModule,
    repositoryModule,
    useCaseModule,
    learningModule
)
