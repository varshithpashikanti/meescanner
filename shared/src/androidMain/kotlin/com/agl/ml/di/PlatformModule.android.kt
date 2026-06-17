package com.agl.ml.di

import com.agl.ml.database.DatabaseFactory
import com.agl.ml.qr.ui.AppResolver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


actual fun platformModule() = module {
    single {
        AppResolver(androidContext())
    }

    single {
        DatabaseFactory(get())
    }
}