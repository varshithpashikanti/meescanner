package com.agl.ml.database

import android.content.Context
import androidx.room.Room
import com.appgolive.meescanner.database.AppDatabase

actual class DatabaseFactory(
        private val context:Context
) {
    actual fun create(): AppDatabase {
        return Room.databaseBuilder<AppDatabase>(
            context = context,
            name = "scanner.db"
        ).build()
    }
}
