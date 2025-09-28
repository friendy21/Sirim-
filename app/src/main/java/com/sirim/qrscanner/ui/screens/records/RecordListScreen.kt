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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.ui.components.LoadingOverlay
import com.sirim.qrscanner.ui.components.SectionHeader
import com.sirim.qrscanner.ui.state.RecordListUiState

@Composable
fun RecordListScreen(
    state: RecordListUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onDeleteRecord: (SirimRecord) -> Unit,
    onQueryChange: (String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        onRefresh()
    }

    if (state.errorMessage != null) {
        LaunchedEffect(state.errorMessage) {
            snackbarHostState.showSnackbar(state.errorMessage)
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

    LoadingOverlay(isVisible = state.isLoading)
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
