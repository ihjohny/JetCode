package com.appsbase.jetcode.data.preferences.di

import com.appsbase.jetcode.data.preferences.data_store.PreferencesDataStore
import com.appsbase.jetcode.data.preferences.data_store.PreferencesDataStoreImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module for preferences dependencies
 */
val preferencesModule = module {

    single<PreferencesDataStore> {
        PreferencesDataStoreImpl(
            context = androidContext(),
        )
    }

}
