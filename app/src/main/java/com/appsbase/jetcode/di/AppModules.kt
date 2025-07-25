package com.appsbase.jetcode.di

import com.appsbase.jetcode.core.common.util.DefaultDispatcherProvider
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.network.di.networkModule
import com.appsbase.jetcode.data.database.di.databaseModule
import com.appsbase.jetcode.data.remote.di.apiServiceModule
import com.appsbase.jetcode.data.repository.repository.LearningRepositoryImpl
import com.appsbase.jetcode.data.repository.repository.PracticeRepositoryImpl
import com.appsbase.jetcode.domain.repository.LearningRepository
import com.appsbase.jetcode.domain.repository.PracticeRepository
import com.appsbase.jetcode.domain.usecase.GetMaterialsByIdsUseCase
import com.appsbase.jetcode.domain.usecase.GetPracticeSetByIdUseCase
import com.appsbase.jetcode.domain.usecase.GetQuizzesByIdsUseCase
import com.appsbase.jetcode.domain.usecase.GetSkillByIdUseCase
import com.appsbase.jetcode.domain.usecase.GetSkillsUseCase
import com.appsbase.jetcode.domain.usecase.GetTopicByIdUseCase
import com.appsbase.jetcode.domain.usecase.GetTopicsByIdsUseCase
import com.appsbase.jetcode.domain.usecase.SearchContentUseCase
import com.appsbase.jetcode.domain.usecase.SyncContentUseCase
import com.appsbase.jetcode.feature.learning.di.learningModule
import com.appsbase.jetcode.feature.practice.di.practiceModule
import com.appsbase.jetcode.sync.SyncManager
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

    single<PracticeRepository> {
        PracticeRepositoryImpl(
            practiceDao = get(),
            apiService = get(),
        )
    }
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
        GetPracticeSetByIdUseCase(
            practiceRepository = get(),
            dispatcherProvider = get(),
        )
    }

    factory {
        GetQuizzesByIdsUseCase(
            practiceRepository = get(),
            dispatcherProvider = get(),
        )
    }

    factory {
        SearchContentUseCase(
            learningRepository = get(),
            dispatcherProvider = get(),
        )
    }

    factory {
        SyncContentUseCase(
            learningRepository = get(),
            practiceRepository = get(),
        )
    }
}

val syncModule = module {
    single {
        SyncManager(
            syncContentUseCase = get()
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
    apiServiceModule,
    repositoryModule,
    useCaseModule,
    syncModule,
    learningModule,
    practiceModule,
)
