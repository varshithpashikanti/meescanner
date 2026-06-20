package com.agl.ml.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agl.ml.history.ui.HistoryUiState
import com.agl.ml.history.usecase.ScanHistoryUseCase
import com.agl.ml.home.util.AnalyzerType
import com.agl.ml.home.util.ScanResult
import com.appgolive.meescanner.entity.ScanHistoryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val scanHistoryUseCase: ScanHistoryUseCase
) : ViewModel() {

    private val _selectedFilterTab = MutableStateFlow("ALL")
    val selectedFilterTab : StateFlow<String> = _selectedFilterTab.asStateFlow()

    private val _historyUiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val historyUiState : StateFlow<HistoryUiState> = _historyUiState.asStateFlow()

    private val _selectedHistory = MutableStateFlow<ScanHistoryEntity?>(null)
    val selectedHistory : StateFlow<ScanHistoryEntity?> = _selectedHistory.asStateFlow()
    private val _showDetailSheet = MutableStateFlow(false)
    val showDetailSheet : StateFlow<Boolean> = _showDetailSheet.asStateFlow()

    fun storeResult(result: ScanResult , analyzerType: AnalyzerType)  {
        viewModelScope.launch {
            println("result taken")
            scanHistoryUseCase.saveHistory(
                result = result,
                analyzerType = analyzerType,
            )
        }
    }

    fun onHistorySelected(history: ScanHistoryEntity?){
        _selectedHistory.value = history
        _showDetailSheet.value = true
    }

    fun historyDismiss(){
        _showDetailSheet.value = false
        _selectedHistory.value = null
    }


    fun onTabSelected(tab: String) {
        _selectedFilterTab.value = tab
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