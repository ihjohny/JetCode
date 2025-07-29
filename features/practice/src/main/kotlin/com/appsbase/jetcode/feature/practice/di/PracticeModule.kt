package com.appsbase.jetcode.feature.practice.di

import com.appsbase.jetcode.feature.practice.presentation.practice_list.PracticeListViewModel
import com.appsbase.jetcode.feature.practice.presentation.practice_quiz.PracticeQuizViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for Practice feature
 */
val practiceModule = module {
    viewModel {
        PracticeQuizViewModel(
            getPracticeSetByIdUseCase = get(),
            getQuizzesByIdsUseCase = get(),
            savePracticeResultUseCase = get(),
        )
    }

    viewModel {
        PracticeListViewModel(
            getUserAllPracticeSetsUseCase = get(),
        )
    }
}
