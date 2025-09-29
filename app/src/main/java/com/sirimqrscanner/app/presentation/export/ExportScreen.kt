package com.sirimqrscanner.app.presentation.export

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExportScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "Export Records",
            style = MaterialTheme.typography.headlineSmall
        )
        Text("Export options will generate Excel, PDF, and ZIP archives in future iterations.")
        Button(onClick = { /* TODO: implement export */ }) {
            Text("Export to Excel")
        }
        Button(onClick = { /* TODO: implement export */ }) {
            Text("Export to PDF")
        }
    }
}
