package com.agl.ml.history.ui

import com.appgolive.meescanner.entity.ScanHistoryEntity

sealed class HistoryUiState {
    data object Loading : HistoryUiState()

    data class Success( val history: List<ScanHistoryEntity>) : HistoryUiState()
    data class Error(val message: String) : HistoryUiState()
}