package com.appsbase.jetcode.feature.learning.di

import com.appsbase.jetcode.feature.learning.presentation.skill_detail.SkillDetailViewModel
import com.appsbase.jetcode.feature.learning.presentation.skill_list.SkillListViewModel
import com.appsbase.jetcode.feature.learning.presentation.topic_detail.TopicDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for Learning feature
 */
val learningModule = module {
    viewModel {
        SkillListViewModel(
            getSkillsUseCase = get(),
            syncContentUseCase = get(),
        )
    }

    viewModel {
        SkillDetailViewModel(
            getSkillByIdUseCase = get(),
            getTopicsByIdsUseCase = get(),
        )
    }

    viewModel {
        TopicDetailViewModel(
            getTopicByIdUseCase = get(),
            getMaterialsForTopicUseCase = get(),
        )
    }
}
