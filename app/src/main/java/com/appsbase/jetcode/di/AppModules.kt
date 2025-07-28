package com.appsbase.jetcode.di

import com.appsbase.jetcode.core.common.util.DefaultDispatcherProvider
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.data.database.di.databaseModule
import com.appsbase.jetcode.data.preferences.di.preferencesModule
import com.appsbase.jetcode.data.remote.di.remoteModule
import com.appsbase.jetcode.data.repository.repository.LearningRepositoryImpl
import com.appsbase.jetcode.data.repository.repository.PracticeRepositoryImpl
import com.appsbase.jetcode.data.repository.repository.PreferencesRepositoryImpl
import com.appsbase.jetcode.data.repository.repository.ProgressRepositoryImpl
import com.appsbase.jetcode.domain.repository.LearningRepository
import com.appsbase.jetcode.domain.repository.PracticeRepository
import com.appsbase.jetcode.domain.repository.PreferencesRepository
import com.appsbase.jetcode.domain.repository.ProgressRepository
import com.appsbase.jetcode.domain.usecase.CompleteOnboardingUseCase
import com.appsbase.jetcode.domain.usecase.GetMaterialsByIdsUseCase
import com.appsbase.jetcode.domain.usecase.GetOnboardingStatusUseCase
import com.appsbase.jetcode.domain.usecase.GetPracticeSetByIdUseCase
import com.appsbase.jetcode.domain.usecase.GetQuizzesByIdsUseCase
import com.appsbase.jetcode.domain.usecase.GetSkillByIdUseCase
import com.appsbase.jetcode.domain.usecase.GetSkillProgressUseCase
import com.appsbase.jetcode.domain.usecase.GetTopicByIdUseCase
import com.appsbase.jetcode.domain.usecase.GetTopicProgressUseCase
import com.appsbase.jetcode.domain.usecase.GetUserAllSkillsUseCase
import com.appsbase.jetcode.domain.usecase.GetUserTopicsByIdsUseCase
import com.appsbase.jetcode.domain.usecase.SearchContentUseCase
import com.appsbase.jetcode.domain.usecase.SyncContentUseCase
import com.appsbase.jetcode.domain.usecase.UpdateProgressUseCase
import com.appsbase.jetcode.feature.learning.di.learningModule
import com.appsbase.jetcode.feature.practice.di.practiceModule
import com.appsbase.jetcode.main.MainViewModel
import com.appsbase.jetcode.sync.SyncManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Core Koin modules for dependency injection
 */

val coreModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }

    viewModel {
        MainViewModel(
            getOnboardingStatusUseCase = get(),
            completeOnboardingUseCase = get(),
        )
    }
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

    single<PreferencesRepository> {
        PreferencesRepositoryImpl(
            preferencesDataStore = get(),
        )
    }

    single<ProgressRepository> {
        ProgressRepositoryImpl(
            progressDao = get(),
        )
    }
}

val useCaseModule = module {
    factory {
        GetOnboardingStatusUseCase(
            preferencesRepository = get(),
            dispatcherProvider = get(),
        )
    }

    factory {
        CompleteOnboardingUseCase(
            preferencesRepository = get(),
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

    factory {
        GetTopicProgressUseCase(
            progressRepository = get(),
            dispatcherProvider = get(),
        )
    }

    factory {
        GetUserTopicsByIdsUseCase(
            learningRepository = get(),
            progressRepository = get(),
            dispatcherProvider = get(),
        )
    }

    factory {
        GetSkillProgressUseCase(
            progressRepository = get(),
            dispatcherProvider = get(),
        )
    }

    factory {
        GetUserAllSkillsUseCase(
            learningRepository = get(),
            progressRepository = get(),
            dispatcherProvider = get(),
        )
    }

    factory {
        UpdateProgressUseCase(
            learningRepository = get(),
            progressRepository = get(),
            getTopicProgressUseCase = get(),
            dispatcherProvider = get(),
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
    databaseModule,
    preferencesModule,
    remoteModule,
    repositoryModule,
    useCaseModule,
    syncModule,
    learningModule,
    practiceModule,
)
