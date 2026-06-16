package com.appgolive.meescanner.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.appgolive.meescanner.dao.ScanHistoryDao
import com.appgolive.meescanner.entity.ScanHistoryEntity

@Database(
    entities = [
        ScanHistoryEntity::class
    ],
    version = 1
)
//@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scanHistoryDao(): ScanHistoryDao
}

//@Suppress("NO_ACTUAL_FOR_EXPECT")
//expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>