package com.sirim.scanner.presentation.export

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sirim.scanner.domain.usecase.ExportRecordsUseCase

@Composable
fun ExportScreen(viewModel: ExportViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.exportedUri) {
        uiState.exportedUri?.let { uri ->
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/octet-stream"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share export"))
            viewModel.consumeResult()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Export records", style = MaterialTheme.typography.headlineSmall)
        ExportOption(
            label = "Export as Excel",
            onClick = { viewModel.export(ExportRecordsUseCase.Format.EXCEL) }
        )
        ExportOption(
            label = "Export as PDF",
            onClick = { viewModel.export(ExportRecordsUseCase.Format.PDF) }
        )
        ExportOption(
            label = "Export as ZIP",
            onClick = { viewModel.export(ExportRecordsUseCase.Format.ZIP) }
        )
        if (uiState.isProcessing) {
            Text(text = "Generating file…")
        }
        uiState.error?.let { error ->
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun ExportOption(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(text = label)
    }
}
