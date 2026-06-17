package com.agl.ml.qr.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import coil3.Bitmap
import com.agl.ml.qr.model.AppInfo
import com.agl.ml.qr.ui.AppResolver
import com.agl.ml.home.util.QrContent

class QrViewModel(private val appResolver: AppResolver) : ViewModel() {

    private val _qrContent = MutableStateFlow<QrContent?>(null)
    val qrContent : StateFlow<QrContent?> = _qrContent.asStateFlow()

    private val _availableApps = MutableStateFlow<List<AppInfo>>(emptyList())

    val availableApps = _availableApps.asStateFlow()

    fun processQr(
        rawValue: String,
        frame: Bitmap?
    ) {
//        val content = QrContentAnalyzer.analyze(rawValue, frame)
      //  _qrContent.value = content

        appResolver.getApps(rawValue)

//        when(content) {
//
////            is QrContent.Upi -> {
////                appResolver.getApps(content.rawValue)
////                _availableApps.value =
////                    appResolver.getUpiApps(
////                        content.rawValue
////                    )
////            }
////
////            is QrContent.Url -> {
////                appResolver.getApps(content.rawValue)
////                _availableApps.value =
////                    appResolver.getUrlApps(
////                        content.rawValue
////                    )
////            }
//
//            else -> {
//                appResolver.getApps(rawValue)
//            }
//        }
    }

    fun clearQr(){
        _qrContent.value = null
    }
}
