package com.appsbase.jetcode.core.database.di

import com.appsbase.jetcode.core.database.JetCodeDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Database module for dependency injection
 */
val databaseModule = module {
    single {
        JetCodeDatabase.create(androidContext())
    }

    single { get<JetCodeDatabase>().learningDao() }

    single { get<JetCodeDatabase>().practiceDao() }
}
