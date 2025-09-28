package com.sirim.qrscanner.ui.state

import com.sirim.qrscanner.core.domain.model.SirimRecord

data class DashboardUiState(
    val recentRecords: List<SirimRecord> = emptyList(),
    val totalRecords: Int = 0,
    val lastSyncAt: String? = null,
    val isSyncing: Boolean = false
)
