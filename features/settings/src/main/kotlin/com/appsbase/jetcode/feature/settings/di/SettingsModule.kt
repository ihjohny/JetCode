package com.appsbase.jetcode.feature.settings.di

import com.appsbase.jetcode.feature.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel {
        SettingsViewModel()
    }
}
