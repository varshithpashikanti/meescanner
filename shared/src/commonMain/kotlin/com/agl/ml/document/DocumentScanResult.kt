package com.agl.ml.document

data class ScannedDocumentPage(
    val imageUri: String,
)

data class DocumentScanResult(
    val pageCount: Int,
    val pdfUri: String? = null,
    val pages: List<ScannedDocumentPage> = emptyList(),
)
