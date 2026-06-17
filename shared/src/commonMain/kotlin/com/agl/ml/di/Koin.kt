package com.agl.ml.di

import com.appgolive.meescanner.di.databaseModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration = {}) {
    startKoin {
        config()
        modules(
            appModule,
            databaseModule,
            platformModule()
        )
    }
}


fun startKoin() {
    initKoin { }
}
