package com.appgolive.meescanner.result.viewmodel

import androidx.lifecycle.ViewModel
import com.appgolive.meescanner.home.util.ScanResult
import com.appgolive.meescanner.qr.viewmodel.QrViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.compose.viewmodel.koinViewModel

class ResultViewModel(
    private val qrViewModel: QrViewModel
) : ViewModel() {

    private val _scanResult =
        MutableStateFlow<ScanResult?>(null)

    val scanResult: StateFlow<ScanResult?> =
        _scanResult.asStateFlow()

    fun setResult(
        result: ScanResult
    ) {
        _scanResult.value = result
    }

    fun clearResult() {
        _scanResult.value = null
    }
}