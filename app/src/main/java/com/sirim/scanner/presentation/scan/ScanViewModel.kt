package com.sirim.scanner.presentation.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirim.scanner.domain.model.SirimRecord
import com.sirim.scanner.domain.usecase.SaveRecordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScanViewModel(
    private val saveRecord: SaveRecordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState

    fun onBarcodeParsed(parsed: ParsedSirimPayload) {
        if (_uiState.value.isSaving) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            val now = System.currentTimeMillis()
            val record = SirimRecord(
                sirimSerialNumber = parsed.serial,
                batchNumber = parsed.batch,
                brandTrademark = parsed.brand,
                model = parsed.model,
                type = parsed.type,
                rating = parsed.rating,
                size = parsed.size,
                createdAt = now,
                updatedAt = now
            )
            saveRecord(record)
            _uiState.value = _uiState.value.copy(
                isSaving = false,
                lastSavedSerial = parsed.serial,
                statusMessage = "Record saved"
            )
        }
    }

    fun onStatusConsumed() {
        _uiState.value = _uiState.value.copy(statusMessage = null)
    }
}

data class ScanUiState(
    val isSaving: Boolean = false,
    val lastSavedSerial: String? = null,
    val statusMessage: String? = null
)

data class ParsedSirimPayload(
    val serial: String,
    val batch: String? = null,
    val brand: String? = null,
    val model: String? = null,
    val type: String? = null,
    val rating: String? = null,
    val size: String? = null
)
