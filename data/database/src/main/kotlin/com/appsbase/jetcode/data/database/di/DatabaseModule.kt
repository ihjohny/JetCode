package com.appsbase.jetcode.data.database.di

import com.appsbase.jetcode.data.database.JetCodeDatabase
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

    single { get<JetCodeDatabase>().progressDao() }
}
