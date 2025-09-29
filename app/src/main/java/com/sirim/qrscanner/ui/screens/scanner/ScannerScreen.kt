package com.sirim.qrscanner.ui.screens.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.viewinterop.AndroidView
import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.feature.scanner.SirimQrAnalyzer
import com.sirim.qrscanner.ui.components.PrimaryButton
import com.sirim.qrscanner.ui.state.ScannerUiState
import java.util.concurrent.Executors

@Composable
fun ScannerScreen(
    state: ScannerUiState,
    onRecordConfirmed: (SirimRecord) -> Unit,
    onCancel: () -> Unit,
    onManualEntry: (SirimRecord) -> Unit,
    onScanResult: (String, String?) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val analyzerExecutor = remember { Executors.newSingleThreadExecutor() }
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setImageAnalysisAnalyzer(analyzerExecutor, SirimQrAnalyzer(coroutineScope, onScanResult))
        }
    }
    val hasPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission.value = granted }

    LaunchedEffect(Unit) {
        if (!hasPermission.value) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    DisposableEffect(lifecycleOwner, hasPermission.value) {
        if (hasPermission.value) {
            cameraController.bindToLifecycle(lifecycleOwner)
        }
        onDispose {
            cameraController.unbind()
        }
    }

    DisposableEffect(Unit) {
        onDispose { analyzerExecutor.shutdown() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Align the QR code within the frame for automatic capture.",
            style = MaterialTheme.typography.bodyMedium
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            if (hasPermission.value) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { previewContext ->
                        PreviewView(previewContext).apply {
                            controller = cameraController
                        }
                    }
                )
            } else {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Camera permission is required for scanning",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    TextButton(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                        Text("Grant permission")
                    }
                }
            }
            if (state.isScanning) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                }
            }
        }

        state.lastScannedRecord?.let { record ->
            ScanResultSummary(record = record)
        }

        val draft = state.lastScannedRecord ?: SirimRecord()
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TextField(
                value = draft.sirimSerialNo,
                onValueChange = { onManualEntry(draft.copy(sirimSerialNo = it.trim())) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Serial number") },
                singleLine = true
            )
            TextField(
                value = draft.brandTrademark.orEmpty(),
                onValueChange = { onManualEntry(draft.copy(brandTrademark = it.trim())) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Brand or trademark") },
                singleLine = true
            )
            TextField(
                value = draft.model.orEmpty(),
                onValueChange = { onManualEntry(draft.copy(model = it.trim())) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Model") }
            )
            TextField(
                value = draft.batchNo.orEmpty(),
                onValueChange = { onManualEntry(draft.copy(batchNo = it.trim())) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Batch number") }
            )
        }

        state.errorMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PrimaryButton(
                text = "Save record",
                enabled = draft.sirimSerialNo.isNotBlank(),
                onClick = { onRecordConfirmed(draft) },
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onCancel, modifier = Modifier.align(Alignment.CenterVertically)) {
                Text("Cancel")
            }
        }
    }
 }

@Composable
private fun ScanResultSummary(record: SirimRecord) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Text(text = "Latest detection", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Serial: ${record.sirimSerialNo}")
        Text(text = "Brand: ${record.brandTrademark ?: "-"}")
        Text(text = "Model: ${record.model ?: "-"}")
        Text(text = "Batch: ${record.batchNo ?: "-"}")
    }
 }
