package com.agl.ml.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    config: KoinAppDeclaration = {}
) {
    startKoin {
        config()
        modules(
            appModule,
            platformModule()
        )
    }
}