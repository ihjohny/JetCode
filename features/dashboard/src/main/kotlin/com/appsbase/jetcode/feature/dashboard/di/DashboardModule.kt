package com.appsbase.jetcode.feature.dashboard.di

import com.appsbase.jetcode.feature.dashboard.presentation.DashboardViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for dashboard feature
 */
val dashboardModule = module {
    viewModel {
        DashboardViewModel()
    }
}
