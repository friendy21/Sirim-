package com.sirim.qrscanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirim.qrscanner.core.common.DispatcherProvider
import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.core.domain.usecase.SaveRecordUseCase
import com.sirim.qrscanner.feature.scanner.SirimQrParser
import com.sirim.qrscanner.ui.state.ScannerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val saveRecordUseCase: SaveRecordUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _state.asStateFlow()
    private var lastDetectedSerial: String? = null
    private var lastDetectionTime: Instant = Instant.EPOCH

    fun updateDraft(record: SirimRecord) {
        _state.value = _state.value.copy(
            lastScannedRecord = record.copy(updatedAt = Instant.now()),
            isScanning = false,
            errorMessage = null
        )
    }

    fun onScanResult(rawPayload: String, recognizedText: String?) {
        val now = Instant.now()
        if (now.minusSeconds(2).isBefore(lastDetectionTime)) {
            return
        }
        val parsed = SirimQrParser.parse(rawPayload, recognizedText)
        if (parsed != null && parsed.sirimSerialNo.isNotBlank()) {
            if (parsed.sirimSerialNo == lastDetectedSerial) {
                return
            }
            lastDetectedSerial = parsed.sirimSerialNo
            lastDetectionTime = now
            _state.value = _state.value.copy(
                lastScannedRecord = parsed,
                isScanning = false,
                errorMessage = null
            )
        } else {
            lastDetectionTime = now
            _state.value = _state.value.copy(errorMessage = "Unable to parse QR payload")
        }
    }

    fun saveRecord(record: SirimRecord) {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.value = _state.value.copy(isScanning = true, errorMessage = null)
            val enriched = record.copy(
                createdAt = record.createdAt.takeIf { it != Instant.EPOCH } ?: Instant.now(),
                updatedAt = Instant.now(),
                isSynced = false
            )
            runCatching { saveRecordUseCase(enriched) }
                .onSuccess {
                    _state.value = _state.value.copy(isScanning = false, lastScannedRecord = enriched)
                }
                .onFailure { throwable ->
                    _state.value = _state.value.copy(
                        isScanning = false,
                        errorMessage = throwable.message
                    )
                }
        }
    }
}
