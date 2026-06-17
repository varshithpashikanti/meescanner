package com.agl.ml.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.appgolive.meescanner.database.AppDatabase
import com.appgolive.meescanner.database.AppDatabaseConstructor
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DatabaseFactory {

    @OptIn(ExperimentalForeignApi::class)
    actual fun create(): AppDatabase {
        val documentsUrl = NSFileManager.defaultManager
            .URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null
            )!!

        println("created database")
        val dbPath = documentsUrl.path!! + "/scanner.db"

        return Room.databaseBuilder<AppDatabase>(
            name = dbPath,
            factory = { AppDatabaseConstructor.initialize() }  // ← critical for iOS
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}