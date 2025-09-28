package com.sirim.qrscanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirim.qrscanner.core.common.DispatcherProvider
import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.core.domain.usecase.SaveRecordUseCase
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

    fun updateDraft(record: SirimRecord) {
        _state.value = _state.value.copy(
            lastScannedRecord = record.copy(updatedAt = Instant.now()),
            isScanning = false
        )
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
