package com.appgolive.meescanner

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.appgolive.meescanner.home.util.AnalyzerType
import com.appgolive.meescanner.home.util.ScanResult

@Composable
expect fun CameraPreview(
    analyzerType: AnalyzerType,
    onScanResult: (ScanResult) -> Unit,
    triggerCapture: Boolean,
    onCaptureCompleted : () -> Unit,
    modifier: Modifier = Modifier
)