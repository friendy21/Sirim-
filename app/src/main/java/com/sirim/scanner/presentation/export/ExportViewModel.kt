package com.sirim.scanner.presentation.export

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirim.scanner.domain.usecase.ExportRecordsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExportViewModel(
    private val exportRecords: ExportRecordsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExportUiState())
    val uiState: StateFlow<ExportUiState> = _uiState

    fun export(format: ExportRecordsUseCase.Format) {
        viewModelScope.launch {
            _uiState.value = ExportUiState(isProcessing = true)
            try {
                val uri = exportRecords(format)
                _uiState.value = ExportUiState(isProcessing = false, exportedUri = uri)
            } catch (error: Throwable) {
                _uiState.value = ExportUiState(isProcessing = false, error = error.message)
            }
        }
    }

    fun consumeResult() {
        _uiState.value = ExportUiState()
    }
}

data class ExportUiState(
    val isProcessing: Boolean = false,
    val exportedUri: Uri? = null,
    val error: String? = null
)
