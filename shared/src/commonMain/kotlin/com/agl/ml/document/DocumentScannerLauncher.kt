package com.agl.ml.document

import androidx.compose.runtime.Composable


interface DocumentScannerLauncher {
    fun launch()
}




@Composable
expect fun DocumentScanner(
    trigger: Boolean,
    onResult: (DocumentScanResult) -> Unit,
    onError: (String) -> Unit
)