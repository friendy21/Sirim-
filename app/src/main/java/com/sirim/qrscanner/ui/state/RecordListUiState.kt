package com.sirim.qrscanner.ui.state

import com.sirim.qrscanner.core.domain.model.SirimRecord

data class RecordListUiState(
    val query: String = "",
    val records: List<SirimRecord> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isExporting: Boolean = false,
    val exportedFilePath: String? = null,
    val exportedMimeType: String? = null
)
