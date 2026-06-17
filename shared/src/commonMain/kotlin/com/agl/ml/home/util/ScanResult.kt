package com.agl.ml.home.util

import coil3.Bitmap
import com.agl.ml.document.ScannedDocumentPage


sealed class ScanResult {
    data class QrResult(val rawValue: String , val frame: Bitmap?) : ScanResult()
    data class TextResult(val text: String , val frame: Bitmap?) : ScanResult()
    data class ObjectResult(
        val frame: Bitmap?,
        val objects: List<DetectedObjectInfo>
    ) : ScanResult()
    data class DocumentResult(
        val pageCount: Int,
        val pdfUri: String? = null,
        val pages: List<ScannedDocumentPage> = emptyList(),
    ) : ScanResult()
}

data class DetectedObjectInfo(
    val label: String,
    val confidence: Float,
    val boundingLeft: Int,
    val boundingTop: Int,
    val boundingRight: Int,
    val boundingBottom: Int,
    val croppedImage: Bitmap?
)