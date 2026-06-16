package com.agl.ml.database

import com.appgolive.meescanner.database.AppDatabase

expect class DatabaseFactory {
    fun create(): AppDatabase
}