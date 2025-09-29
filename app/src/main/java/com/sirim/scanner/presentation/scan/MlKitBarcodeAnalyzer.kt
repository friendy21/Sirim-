package com.sirim.scanner.presentation.scan

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class MlKitBarcodeAnalyzer(
    private val onResult: (List<Barcode>) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()

    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image ?: run {
            image.close()
            return
        }
        val rotationDegrees = image.imageInfo.rotationDegrees
        val inputImage = InputImage.fromMediaImage(mediaImage, rotationDegrees)
        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    onResult(barcodes)
                }
            }
            .addOnCompleteListener { image.close() }
    }
}
