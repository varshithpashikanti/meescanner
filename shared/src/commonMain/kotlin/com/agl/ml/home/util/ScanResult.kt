package com.agl.ml.home.util


sealed class ScanResult {
    data class QrResult(val rawValue: String) : ScanResult()
    data class TextResult(val text: String) : ScanResult()
    data class ObjectResult(val label: String) : ScanResult()
    data class DocumentResult(val text: String) : ScanResult()
}