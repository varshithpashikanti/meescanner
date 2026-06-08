package com.agl.ml

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.agl.ml.home.util.AnalyzerType
import com.agl.ml.home.util.ScanResult

@Composable
expect fun CameraPreview(
    analyzerType: AnalyzerType,
    onScanResult: (ScanResult) -> Unit,
    modifier: Modifier = Modifier
)