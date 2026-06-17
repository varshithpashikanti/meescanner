package com.agl.ml.result.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.agl.ml.home.util.ScanResult
import com.agl.ml.qr.viewmodel.QrViewModel
import com.agl.ml.result.viewmodel.ResultViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.agl.ml.document.DocumentViewModel
import com.agl.ml.home.ui.ScanResultSection

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
//            is ScanResult.DocumentResult ->{
//                documentViewModel.processDoc(
//                    link = result.pdfUri
//                )
//            }
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
