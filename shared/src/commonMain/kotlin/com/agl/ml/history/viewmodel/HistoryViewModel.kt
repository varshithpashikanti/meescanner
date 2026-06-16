package com.agl.ml.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agl.ml.history.ui.HistoryUiState
import com.agl.ml.history.usecase.ScanHistoryUseCase
import com.appgolive.meescanner.home.util.AnalyzerType
import com.appgolive.meescanner.home.util.ScanResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock

class HistoryViewModel(
    private val scanHistoryUseCase: ScanHistoryUseCase
) : ViewModel() {

    private val _selectedFilterTab = MutableStateFlow("ALL")
    val selectedFilterTab : StateFlow<String> = _selectedFilterTab.asStateFlow()

    private val _historyUiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val historyUiState : StateFlow<HistoryUiState> = _historyUiState.asStateFlow()

    fun storeResult(result: ScanResult , analyzerType: AnalyzerType)  {
        viewModelScope.launch {
            println("result taken")
            scanHistoryUseCase.saveHistory(
                result = result,
                analyzerType = analyzerType,
            )
        }
    }

    fun loadHistory(){
        viewModelScope.launch {
            _historyUiState.value = HistoryUiState.Loading
            try{
                scanHistoryUseCase.getHistory().collect{
                    _historyUiState.value = HistoryUiState.Success(it)
                }
            }catch (e: Exception){
                _historyUiState.value = HistoryUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

}