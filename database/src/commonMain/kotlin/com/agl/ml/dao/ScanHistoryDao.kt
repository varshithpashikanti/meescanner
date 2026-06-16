package com.appgolive.meescanner.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.appgolive.meescanner.entity.ScanHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanHistoryDao {

    @Insert
    suspend fun insert(item: ScanHistoryEntity)

    @Query("""
        SELECT * FROM scan_history
        ORDER BY createdAt DESC
    """)
    fun getAll(): Flow<List<ScanHistoryEntity>>

    @Delete
    suspend fun delete(item: ScanHistoryEntity)

    @Query("DELETE FROM scan_history")
    suspend fun clearAll()
}