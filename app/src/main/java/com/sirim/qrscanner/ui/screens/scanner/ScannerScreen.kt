package com.sirim.qrscanner.ui.screens.scanner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.ui.components.LoadingOverlay
import com.sirim.qrscanner.ui.components.PrimaryButton
import com.sirim.qrscanner.ui.state.ScannerUiState

@Composable
fun ScannerScreen(
    state: ScannerUiState,
    onRecordConfirmed: (SirimRecord) -> Unit,
    onCancel: () -> Unit,
    onManualEntry: (SirimRecord) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "Scan a SIRIM QR code", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "Align the QR code within the frame. Data is captured securely and stays encrypted on this device.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            if (state.lastScannedRecord != null) {
                SirimRecordSummary(record = state.lastScannedRecord)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            val draft = state.lastScannedRecord ?: SirimRecord()
            TextField(
                value = draft.sirimSerialNo,
                onValueChange = { onManualEntry(draft.copy(sirimSerialNo = it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Serial number") },
                singleLine = true
            )
            TextField(
                value = draft.brandTrademark.orEmpty(),
                onValueChange = { onManualEntry(draft.copy(brandTrademark = it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Brand or trademark") },
                singleLine = true
            )
            TextField(
                value = draft.model.orEmpty(),
                onValueChange = { onManualEntry(draft.copy(model = it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Model") }
            )
            TextField(
                value = draft.batchNo.orEmpty(),
                onValueChange = { onManualEntry(draft.copy(batchNo = it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Batch number") }
            )
            PrimaryButton(
                text = "Save record",
                enabled = draft.sirimSerialNo.isNotBlank() && !state.isScanning,
                onClick = { onRecordConfirmed(draft) }
            )
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }

    LoadingOverlay(isVisible = state.isScanning)
}

@Composable
private fun SirimRecordSummary(record: SirimRecord) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(text = "Scanned record", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Serial: ${record.sirimSerialNo}")
        Text(text = "Brand: ${record.brandTrademark ?: "-"}")
        Text(text = "Model: ${record.model ?: "-"}")
        Text(text = "Batch: ${record.batchNo ?: "-"}")
    }
}
