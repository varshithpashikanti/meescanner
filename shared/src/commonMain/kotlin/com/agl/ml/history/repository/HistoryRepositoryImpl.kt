package com.agl.ml.history.repository

import com.appgolive.meescanner.dao.ScanHistoryDao
import com.appgolive.meescanner.entity.ScanHistoryEntity
import kotlinx.coroutines.flow.Flow

class HistoryRepositoryImpl(
    private val dao: ScanHistoryDao
) : HistoryRepository {

    override suspend fun insertHistory(
        history: ScanHistoryEntity
    ) {
        println("Result inserted")
        dao.insert(history)
        println("Success")
    }

    override fun getHistory(): Flow<List<ScanHistoryEntity>> {
        println("Getting history")
        return dao.getAll()
    }
}