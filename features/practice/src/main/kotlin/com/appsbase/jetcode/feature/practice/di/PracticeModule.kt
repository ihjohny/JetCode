package com.appsbase.jetcode.feature.practice.di

import com.appsbase.jetcode.feature.practice.presentation.PracticeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for Practice feature
 */
val practiceModule = module {
    viewModel {
        PracticeViewModel(
            getPracticeSetByIdUseCase = get(),
            getQuizzesByIdsUseCase = get(),
            savePracticeResultUseCase = get(),
        )
    }
}
