package com.agl.ml.qr.viewmodel

import androidx.lifecycle.ViewModel
import com.agl.ml.home.util.QrContent
import com.agl.ml.home.util.QrContentAnalyzer
import com.agl.ml.qr.model.AppInfo
import com.agl.ml.qr.ui.AppResolver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QrViewModel(private val appResolver: AppResolver) : ViewModel() {

    private val _qrContent = MutableStateFlow<QrContent?>(null)
    val qrContent : StateFlow<QrContent?> = _qrContent.asStateFlow()

    private val _availableApps = MutableStateFlow<List<AppInfo>>(emptyList())

    val availableApps = _availableApps.asStateFlow()

    fun processQr(
        rawValue: String
    ) {
        val content = QrContentAnalyzer.analyze(rawValue)
        _qrContent.value = content

        when(content) {

            is QrContent.Upi -> {
                _availableApps.value =
                    appResolver.getUpiApps(
                        content.rawValue
                    )
            }

            is QrContent.Url -> {
                _availableApps.value =
                    appResolver.getUrlApps(
                        content.rawValue
                    )
            }

            else -> {
                _availableApps.value =
                    emptyList()
            }
        }
    }

    fun clearQr(){
        _qrContent.value = null
    }
}