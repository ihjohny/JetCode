package com.appsbase.jetcode.feature.profile.di

import com.appsbase.jetcode.feature.profile.presentation.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    viewModel {
        ProfileViewModel(
            getUserStatisticsUseCase = get(),
        )
    }
}
