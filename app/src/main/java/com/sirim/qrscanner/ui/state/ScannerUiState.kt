package com.sirim.qrscanner.ui.state

import com.sirim.qrscanner.core.domain.model.SirimRecord

data class ScannerUiState(
    val isScanning: Boolean = false,
    val lastScannedRecord: SirimRecord? = null,
    val errorMessage: String? = null
)
