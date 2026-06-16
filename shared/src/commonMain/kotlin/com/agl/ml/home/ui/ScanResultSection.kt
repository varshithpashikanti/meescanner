package com.appgolive.meescanner.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.agl.ml.document.DocumentResultScreen
import com.agl.ml.result.ui.ObjectResultScreen
import com.agl.ml.text.TextResultScreen
import com.appgolive.meescanner.home.util.ScanResult
import com.appgolive.meescanner.qr.ui.QrResultScreen
import com.appgolive.meescanner.qr.viewmodel.QrViewModel

@Composable
fun ScanResultSection(
    scanResult: ScanResult,
    qrViewModel: QrViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
){
    when(scanResult) {

        is ScanResult.QrResult -> {
            QrResultScreen(
                modifier = modifier,
                qrViewModel = qrViewModel,
                onBackClick = onBackClick,
            )
        }

        is ScanResult.TextResult -> {
            TextResultScreen(
                text = scanResult.text,
                frame = scanResult.frame,
                modifier = modifier
            )
        }

        is ScanResult.ObjectResult -> {
            ObjectResultScreen(
                frame = scanResult.frame,
                objects = scanResult.objects,
                modifier = modifier
            )
        }

        is ScanResult.DocumentResult -> {
            DocumentResultScreen(
                uri = scanResult.pdfUri.toString(),
                pages = scanResult.pageCount,
                allPages = scanResult.pages,
                modifier = modifier
            )
        }
    }
}