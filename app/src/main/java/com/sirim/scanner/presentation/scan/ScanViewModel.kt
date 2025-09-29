package com.sirim.scanner.presentation.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirim.scanner.domain.model.SirimRecord
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

class ScanViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun simulateScan() {
        if (_uiState.value.isScanning) return
        _uiState.value = _uiState.value.copy(isScanning = true, error = null)
        viewModelScope.launch {
            delay(1500)
            val record = SirimRecord(
                sirimSerialNo = UUID.randomUUID().toString().take(12),
                createdAt = Instant.now().toEpochMilli(),
                updatedAt = Instant.now().toEpochMilli(),
                batchNumber = "SIM-${'$'}{(1000..9999).random()}",
                brandTrademark = "Sample Brand",
                model = "Model ${(1..5).random()}",
                type = "Type ${(1..5).random()}",
                rating = "${(100..240).random()}V",
                size = "Standard",
                isSynced = false,
                deviceId = "LOCAL"
            )
            _uiState.value = _uiState.value.copy(isScanning = false, scannedRecord = record)
        }
    }

    fun clearResult() {
        _uiState.value = ScanUiState()
    }
}

data class ScanUiState(
    val isScanning: Boolean = false,
    val scannedRecord: SirimRecord? = null,
    val error: String? = null,
)
