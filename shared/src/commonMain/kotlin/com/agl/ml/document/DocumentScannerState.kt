package com.agl.ml.document

sealed class DocumentScanState {
    data object Idle : DocumentScanState()
    data object Scanning : DocumentScanState()
    data class Success(
        val result: DocumentScanResult
    ) : DocumentScanState()
    data class Error(
        val message: String
    ) : DocumentScanState()
}