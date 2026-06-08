package com.agl.ml.home.viewmodel

import androidx.lifecycle.ViewModel
import com.agl.ml.home.util.AnalyzerType
import com.agl.ml.home.util.ScanResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel(){

    private val _selectedTab = MutableStateFlow("QR Scanner")
    val selectedTab : StateFlow<String> = _selectedTab.asStateFlow()

    private val _selectedAnalyzer = MutableStateFlow(AnalyzerType.QR)
    val selectedAnalyzer: StateFlow<AnalyzerType> = _selectedAnalyzer.asStateFlow()

    private val _scanResult = MutableStateFlow<ScanResult?>(null)

    val scanResult : StateFlow<ScanResult?> = _scanResult.asStateFlow()


    fun onScanResult(
        result: ScanResult
    ) {
        _scanResult.value = result
    }

    fun onTabSelected(
        tab: String
    ) {
        _selectedTab.value = tab

        _selectedAnalyzer.value = when (tab) {

            "QR Scanner" -> AnalyzerType.QR

            "Text Analyzer" -> AnalyzerType.TEXT

            "Object Detection" -> AnalyzerType.OBJECT

            "Document" -> AnalyzerType.DOCUMENT

            "Photo" -> AnalyzerType.PHOTO

            else -> AnalyzerType.QR
        }
    }
}