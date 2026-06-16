package com.agl.ml.di

import com.agl.ml.database.DatabaseFactory
import com.appgolive.meescanner.qr.ui.AppResolver
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule() = module {
    single { AppResolver() }

    single{
        DatabaseFactory()
    }
}