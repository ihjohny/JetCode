package com.appsbase.jetcode.feature.learning.di

import com.appsbase.jetcode.feature.learning.presentation.dashboard.LearningDashboardViewModel
import com.appsbase.jetcode.feature.learning.presentation.lesson.LessonViewModel
import com.appsbase.jetcode.feature.learning.presentation.skilldetail.SkillDetailViewModel
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

    viewModel { (skillId: String) ->
        SkillDetailViewModel(
            getSkillByIdUseCase = get(),
            getTopicsForSkillUseCase = get()
        )
    }

    viewModel { (lessonId: String) ->
        LessonViewModel(
            getLessonByIdUseCase = get()
        )
    }
}
