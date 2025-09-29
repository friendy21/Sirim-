package com.sirim.qrscanner.ui.screens.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.core.domain.repository.ExportFormat
import com.sirim.qrscanner.ui.components.LoadingOverlay
import com.sirim.qrscanner.ui.components.SectionHeader
import com.sirim.qrscanner.ui.state.RecordListUiState
import com.sirim.qrscanner.util.ShareUtils
import java.io.File

@Composable
fun RecordListScreen(
    state: RecordListUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onDeleteRecord: (SirimRecord) -> Unit,
    onQueryChange: (String) -> Unit,
    onExport: (ExportFormat) -> Unit,
    onExportConsumed: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var exportDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onRefresh()
    }

    if (state.errorMessage != null) {
        LaunchedEffect(state.errorMessage) {
            snackbarHostState.showSnackbar(state.errorMessage)
        }
    }

    val exportedPath = state.exportedFilePath
    val exportedMimeType = state.exportedMimeType
    if (exportedPath != null && exportedMimeType != null) {
        LaunchedEffect(exportedPath, exportedMimeType) {
            ShareUtils.shareFile(context, File(exportedPath), exportedMimeType)
            onExportConsumed()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Records") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { exportDialogVisible = true }) {
                        Icon(
                            imageVector = Icons.Default.IosShare,
                            contentDescription = "Export"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SectionHeader(text = "Search")
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                placeholder = { Text("Search by serial, brand, or batch") }
            )
            SectionHeader(text = "Results")
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.records, key = { it.id }) { record ->
                    RecordRow(record = record, onDeleteRecord = onDeleteRecord)
                }
            }
        }
    }

    LoadingOverlay(isVisible = state.isLoading || state.isExporting)

    if (exportDialogVisible) {
        ExportFormatDialog(
            onDismiss = { exportDialogVisible = false },
            onFormatSelected = {
                exportDialogVisible = false
                onExport(it)
            }
        )
    }
}

@Composable
private fun RecordRow(record: SirimRecord, onDeleteRecord: (SirimRecord) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = record.brandTrademark ?: "Unknown brand", style = MaterialTheme.typography.titleMedium)
                Text(text = record.sirimSerialNo, style = MaterialTheme.typography.bodyMedium)
                Text(text = record.model ?: "-", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onDeleteRecord(record) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
private fun ExportFormatDialog(
    onDismiss: () -> Unit,
    onFormatSelected: (ExportFormat) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Export records") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ExportActionRow("Excel (.xlsx)") { onFormatSelected(ExportFormat.EXCEL) }
                ExportActionRow("PDF (.pdf)") { onFormatSelected(ExportFormat.PDF) }
                ExportActionRow("ZIP (.zip)") { onFormatSelected(ExportFormat.ZIP) }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun ExportActionRow(label: String, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(text = label, modifier = Modifier.fillMaxWidth())
    }
}
