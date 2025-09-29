package com.sirim.qrscanner.feature.scanner

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class SirimQrAnalyzer(
    private val scope: CoroutineScope,
    private val onResult: (String, String?) -> Unit
) : ImageAnalysis.Analyzer {

    private val barcodeScanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val isProcessing = AtomicBoolean(false)

    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image ?: run {
            image.close()
            return
        }
        if (!isProcessing.compareAndSet(false, true)) {
            image.close()
            return
        }
        val input = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
        scope.launch {
            try {
                val barcodes = barcodeScanner.process(input).await()
                val rawValue = barcodes.firstOrNull()?.rawValue
                val ocrText = textRecognizer.process(input).await().text
                if (!rawValue.isNullOrBlank()) {
                    onResult(rawValue, ocrText.takeIf { it.isNotBlank() })
                } else if (ocrText.isNotBlank()) {
                    onResult(ocrText, ocrText)
                }
            } catch (throwable: Exception) {
                Timber.e(throwable, "Failed to process camera frame")
            } finally {
                isProcessing.set(false)
                image.close()
            }
        }
    }
}
