package com.agl.ml.result.viewmodel

import androidx.lifecycle.ViewModel
import com.agl.ml.home.util.ScanResult
import com.agl.ml.qr.viewmodel.QrViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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