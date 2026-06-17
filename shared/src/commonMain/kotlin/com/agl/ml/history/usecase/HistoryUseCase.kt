package com.agl.ml.history.usecase

import com.agl.ml.history.repository.HistoryRepository
import com.appgolive.meescanner.entity.ScanHistoryEntity
import com.agl.ml.home.util.AnalyzerType
import com.agl.ml.home.util.ScanResult
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ScanHistoryUseCase(
    private val repository: HistoryRepository
) {

    @OptIn(ExperimentalTime::class)
    suspend fun saveHistory(
        result: ScanResult,
        analyzerType: AnalyzerType,
    ) {

        val history = when (result) {

            is ScanResult.QrResult -> {
                ScanHistoryEntity(
                    scanType = analyzerType.name,
                    title = "QR Code",
                    data = result.rawValue,
                    createdAt = Clock.System.now().toEpochMilliseconds()
                )
            }

            is ScanResult.TextResult -> {
                ScanHistoryEntity(
                    scanType = analyzerType.name,
                    title = "Text Scan",
                    data = result.text,
                    createdAt = Clock.System.now().toEpochMilliseconds()
                )
            }

            is ScanResult.DocumentResult -> {
                ScanHistoryEntity(
                    scanType = analyzerType.name,
                    title = "Document",
                    data = result.pdfUri.orEmpty(),
                    createdAt = Clock.System.now().toEpochMilliseconds()
                )
            }

            is ScanResult.ObjectResult -> {
                ScanHistoryEntity(
                    scanType = analyzerType.name,
                    title = "Object Detection",
                    data = result.objects.joinToString(",") { it.label },
                    createdAt = Clock.System.now().toEpochMilliseconds()
                )
            }
        }

        repository.insertHistory(history)
    }


    fun getHistory() = repository.getHistory()
}