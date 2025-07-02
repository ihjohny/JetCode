package com.appsbase.jetcode.feature.learning.di

import com.appsbase.jetcode.feature.learning.presentation.dashboard.LearningDashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for Learning feature
 */
val learningModule = module {
    viewModel {
        LearningDashboardViewModel(
            getSkillsUseCase = get(),
            syncContentUseCase = get()
        )
    }
}
