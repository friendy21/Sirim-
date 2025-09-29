package com.sirim.scanner.presentation.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sirim.scanner.domain.model.SirimRecord
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    state: RecordsUiState,
    onSearchChange: (String) -> Unit,
    onSaveRecord: (SirimRecord) -> Unit,
    onDeleteRecord: (Long) -> Unit,
    onExportExcel: () -> Unit,
    onExportPdf: () -> Unit,
    onExportBundle: () -> Unit,
    onMessageShown: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val showEditor = remember { mutableStateOf(false) }
    val recordToEdit = remember { mutableStateOf<SirimRecord?>(null) }

    LaunchedEffect(state.error, state.successMessage) {
        state.error?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
            onMessageShown()
        }
        state.successMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
            onMessageShown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Records") }, actions = {
                IconButton(onClick = onExportExcel) {
                    Icon(Icons.Default.Description, contentDescription = "Export Excel")
                }
                IconButton(onClick = onExportPdf) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF")
                }
                IconButton(onClick = onExportBundle) {
                    Icon(Icons.Default.Download, contentDescription = "Export Bundle")
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                recordToEdit.value = null
                showEditor.value = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.searchQuery,
                onValueChange = onSearchChange,
                label = { Text(text = "Search") }
            )
            LazyColumn(
                modifier = Modifier.weight(1f, fill = true),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.records) { record ->
                    RecordCard(
                        record = record,
                        onEdit = {
                            recordToEdit.value = record
                            showEditor.value = true
                        },
                        onDelete = { onDeleteRecord(record.id) }
                    )
                }
            }
        }
    }

    if (showEditor.value) {
        RecordEditorDialog(
            initialRecord = recordToEdit.value,
            onDismiss = {
                showEditor.value = false
                recordToEdit.value = null
            },
            onSave = { record ->
                onSaveRecord(record)
                showEditor.value = false
                recordToEdit.value = null
            }
        )
    }

    state.exportedFile?.let { exported ->
        LaunchedEffect(exported) {
            snackbarHostState.showSnackbar(
                message = "${exported.fileName} ready to share"
            )
            onMessageShown()
        }
    }
}

@Composable
private fun RecordCard(record: SirimRecord, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = record.sirimSerialNo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            record.brandTrademark?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
            record.model?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@Composable
private fun RecordEditorDialog(
    initialRecord: SirimRecord?,
    onDismiss: () -> Unit,
    onSave: (SirimRecord) -> Unit,
) {
    val serial = remember(initialRecord) { mutableStateOf(initialRecord?.sirimSerialNo.orEmpty()) }
    val brand = remember(initialRecord) { mutableStateOf(initialRecord?.brandTrademark.orEmpty()) }
    val model = remember(initialRecord) { mutableStateOf(initialRecord?.model.orEmpty()) }
    val type = remember(initialRecord) { mutableStateOf(initialRecord?.type.orEmpty()) }
    val rating = remember(initialRecord) { mutableStateOf(initialRecord?.rating.orEmpty()) }
    val size = remember(initialRecord) { mutableStateOf(initialRecord?.size.orEmpty()) }
    val batch = remember(initialRecord) { mutableStateOf(initialRecord?.batchNumber.orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (initialRecord == null) "Create Record" else "Edit Record") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = serial.value, onValueChange = { serial.value = it }, label = { Text("Serial") })
                OutlinedTextField(value = brand.value, onValueChange = { brand.value = it }, label = { Text("Brand") })
                OutlinedTextField(value = model.value, onValueChange = { model.value = it }, label = { Text("Model") })
                OutlinedTextField(value = type.value, onValueChange = { type.value = it }, label = { Text("Type") })
                OutlinedTextField(value = rating.value, onValueChange = { rating.value = it }, label = { Text("Rating") })
                OutlinedTextField(value = size.value, onValueChange = { size.value = it }, label = { Text("Size") })
                OutlinedTextField(value = batch.value, onValueChange = { batch.value = it }, label = { Text("Batch") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    SirimRecord(
                        id = initialRecord?.id ?: 0L,
                        sirimSerialNo = serial.value,
                        brandTrademark = brand.value,
                        model = model.value,
                        type = type.value,
                        rating = rating.value,
                        size = size.value,
                        batchNumber = batch.value,
                        createdAt = initialRecord?.createdAt ?: 0L,
                        updatedAt = initialRecord?.updatedAt ?: 0L,
                        isSynced = initialRecord?.isSynced ?: false,
                        deviceId = initialRecord?.deviceId ?: "LOCAL"
                    )
                )
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
