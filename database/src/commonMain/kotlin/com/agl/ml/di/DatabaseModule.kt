package com.appgolive.meescanner.di

import com.agl.ml.database.DatabaseFactory
import com.appgolive.meescanner.database.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        get<DatabaseFactory>().create()
    }

    single {
        get<AppDatabase>()
            .scanHistoryDao()
    }

}