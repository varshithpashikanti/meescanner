package com.appgolive.meescanner.result.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.appgolive.meescanner.home.util.ScanResult
import com.appgolive.meescanner.qr.viewmodel.QrViewModel
import com.appgolive.meescanner.result.viewmodel.ResultViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.appgolive.meescanner.document.DocumentViewModel
import com.appgolive.meescanner.home.ui.ScanResultSection

@Composable
fun ResultScreen(
    resultViewModel: ResultViewModel,
    qrViewModel: QrViewModel,
    documentViewModel: DocumentViewModel,
    modifier: Modifier = Modifier,
    onBackClick : () -> Unit
){

    val scanResult by resultViewModel.scanResult.collectAsState()

    LaunchedEffect(scanResult) {
        val result = scanResult

        when(result){
            is ScanResult.QrResult ->{
                qrViewModel.processQr(
                    rawValue = result.rawValue,
                    frame = result.frame
                )
            }
            is ScanResult.DocumentResult ->{
                documentViewModel.processDoc(
                    link = result.pdfUri
                )
            }
            else -> {}
        }

    }

    scanResult?.let {
        ScanResultSection(
            scanResult = it,
            qrViewModel = qrViewModel,
            modifier = modifier,
            onBackClick = onBackClick,
        )
    }
}
