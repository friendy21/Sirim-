package com.sirim.qrscanner.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sirim.qrscanner.ui.components.PrimaryButton
import com.sirim.qrscanner.ui.state.DashboardUiState

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onOpenScanner: () -> Unit,
    onOpenRecords: () -> Unit,
    onLogout: () -> Unit,
    onSync: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "SIRIM QR Scanner",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Securely capture, review, and export compliance data.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            TextButton(onClick = onLogout) {
                Text(text = "Logout")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(text = "Overview", style = MaterialTheme.typography.titleMedium)
                Text(text = "Total records: ${state.totalRecords}")
                Divider()
                Text(text = "Recent activity", fontWeight = FontWeight.SemiBold)
                state.recentRecords.take(3).forEach { record ->
                    Text(text = "${record.brandTrademark ?: "Unknown brand"} • ${record.sirimSerialNo}")
                }
                if (state.lastSyncAt != null) {
                    Text(text = "Last synced: ${state.lastSyncAt}")
                }
                PrimaryButton(
                    text = if (state.isSyncing) "Synchronizing…" else "Sync now",
                    enabled = !state.isSyncing,
                    onClick = onSync
                )
            }
        }

        PrimaryButton(text = "Start scanning", onClick = onOpenScanner)
        PrimaryButton(text = "View records", onClick = onOpenRecords)
    }
}
