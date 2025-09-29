package com.sirim.scanner.presentation.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sirim.scanner.domain.model.SirimRecord

@Composable
fun ScanScreen(
    viewModel: ScanViewModel,
    onRecordCaptured: (SirimRecord) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Scan QR Code",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "This prototype uses a simulated scan. Connect the CameraX implementation to enable live capture.",
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = { viewModel.simulateScan() }, enabled = !state.isScanning) {
            Text(text = if (state.isScanning) "Scanning..." else "Simulate Scan")
        }
        state.scannedRecord?.let { record ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Serial: ${record.sirimSerialNo}")
                    record.brandTrademark?.let { Text(text = "Brand: $it") }
                    record.model?.let { Text(text = "Model: $it") }
                    Button(onClick = {
                        onRecordCaptured(record)
                        viewModel.clearResult()
                    }) {
                        Text(text = "Save Record")
                    }
                }
            }
        }
    }
}
