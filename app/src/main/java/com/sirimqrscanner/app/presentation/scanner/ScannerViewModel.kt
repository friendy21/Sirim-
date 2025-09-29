package com.sirimqrscanner.app.presentation.scanner

import android.graphics.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.BarcodeScannerOptions
import com.google.mlkit.vision.common.InputImage
import com.sirimqrscanner.app.domain.model.SirimRecord
import com.sirimqrscanner.app.domain.usecase.UpsertRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ScannerEvent {
    data class RecordSaved(val record: SirimRecord) : ScannerEvent
    data class Error(val message: String) : ScannerEvent
}

data class ScannerUiState(
    val isProcessing: Boolean = false,
    val lastScannedText: String? = null,
    val boundingBox: Rect? = null
)

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val upsertRecordUseCase: UpsertRecordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ScannerUiState())
    val state: StateFlow<ScannerUiState> = _state.asStateFlow()

    private val _events = Channel<ScannerEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )

    fun process(image: InputImage) {
        if (_state.value.isProcessing) return
        _state.value = _state.value.copy(isProcessing = true)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                val qrBarcode = barcodes.firstOrNull()
                if (qrBarcode != null && !qrBarcode.rawValue.isNullOrBlank()) {
                    handleScannedText(qrBarcode)
                } else {
                    _state.value = _state.value.copy(isProcessing = false)
                }
            }
            .addOnFailureListener { error ->
                viewModelScope.launch {
                    _events.send(ScannerEvent.Error(error.localizedMessage ?: "Failed to scan"))
                }
                _state.value = _state.value.copy(isProcessing = false)
            }
    }

    private fun handleScannedText(barcode: Barcode) {
        val payload = barcode.rawValue ?: return
        _state.value = _state.value.copy(
            lastScannedText = payload,
            boundingBox = barcode.boundingBox,
            isProcessing = false
        )

        // Basic parser expecting newline-delimited key:value pairs
        val fields = payload.lines()
            .mapNotNull { line ->
                val parts = line.split(":", limit = 2)
                if (parts.size == 2) parts[0].trim().lowercase() to parts[1].trim() else null
            }
            .toMap()

        val record = SirimRecord(
            sirimSerialNo = fields["serial"] ?: fields["sirim"] ?: payload.take(64),
            batchNo = fields["batch"],
            brandTrademark = fields["brand"],
            model = fields["model"],
            type = fields["type"],
            rating = fields["rating"],
            size = fields["size"]
        )

        viewModelScope.launch {
            upsertRecordUseCase(record)
            _events.send(ScannerEvent.RecordSaved(record))
        }
    }
}
