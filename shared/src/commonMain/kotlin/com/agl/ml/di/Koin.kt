package com.appgolive.meescanner.di

import com.agl.ml.di.platformModule
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
