package com.agl.ml.document

import androidx.lifecycle.ViewModel
import com.agl.ml.qr.ui.AppResolver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DocumentViewModel(private val appResolver: AppResolver) : ViewModel() {

    private val _openScanner : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val openScanner : StateFlow<Boolean> = _openScanner.asStateFlow()

    private val _documentScanState = MutableStateFlow<DocumentScanState>(DocumentScanState.Idle)
    val documentScanState: StateFlow<DocumentScanState> = _documentScanState.asStateFlow()

    fun onDocumentTabTapped() {
        _openScanner.value = true
        _documentScanState.value = DocumentScanState.Scanning
    }

    fun onScanSuccess(result: DocumentScanResult) {
        _openScanner.value = false
        _documentScanState.value = DocumentScanState.Success(result)

    }


    fun processDoc(
        link: String?,
    ) {
        appResolver.getDocApp(link)
    }

    fun onScanError(message: String) {
        _openScanner.value = false
        _documentScanState.value = DocumentScanState.Error(message)
    }

    fun onErrorDismissed() {
        _documentScanState.value = DocumentScanState.Idle
    }
}