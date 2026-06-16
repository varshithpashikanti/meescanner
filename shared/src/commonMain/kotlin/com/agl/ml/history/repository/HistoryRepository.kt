package com.agl.ml.history.repository

import com.appgolive.meescanner.entity.ScanHistoryEntity
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun insertHistory(history: ScanHistoryEntity)
    fun getHistory(): Flow<List<ScanHistoryEntity>>
}