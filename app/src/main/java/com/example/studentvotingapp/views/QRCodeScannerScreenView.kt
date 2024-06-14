package com.example.studentvotingapp.views

import android.util.Size
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer

class QRCodeScannerScreenView {
    companion object {
        @Composable
        fun QRCodeScannerScreen() {
            var displayDialog by remember { mutableStateOf(false) }
            var dialogText by remember { mutableStateOf("") }

            if (displayDialog) {
                AlertDialog(
                    title = {
                        Text(text = "dialogTitle")
                    },
                    text = {
                        Text(text = dialogText)
                    },
                    onDismissRequest = {
                        displayDialog = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                displayDialog = false
                            }
                        ) {
                            Text("Confirm")
                        }
                    }
                )
            }

            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current
            val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

            AndroidView(factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val qrCodeAnalyzer = QRCodeAnalyzer { result ->
                    HandleQRCodeScanResult(result) {
                        dialogText = it
                        displayDialog = true
                    }
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                val cameraProvider = cameraProviderFuture.get()

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(1280, 720))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(ContextCompat.getMainExecutor(ctx), qrCodeAnalyzer)
                    }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )

                previewView
            })
        }

        private fun HandleQRCodeScanResult(result: String, callback: (String) -> Unit) {
            val db = Firebase.firestore

            val parts = result.split(",")
            if (parts.size == 2) {
                val votingId = parts[0]
                val candidate = parts[1]

                val user = Firebase.auth.currentUser
                val userId = user?.uid ?: return

                db.collection("votings").document(votingId)
                    .update("votes.$candidate", FieldValue.increment(1))
                    .addOnSuccessListener {
                        callback("Voted successfully!")
                    }
                    .addOnFailureListener {
                        callback("Failed to vote")
                    }

                db.collection("users").document(userId)
                    .update("votes", FieldValue.arrayUnion(votingId))
                    .addOnSuccessListener {
                        // Handle success
                    }
                    .addOnFailureListener {
                        // Handle failure
                    }
            }
        }

        class QRCodeAnalyzer(private val onQRCodeScanned: (String) -> Unit) : ImageAnalysis.Analyzer {
            private val reader = MultiFormatReader().apply {
                setHints(mapOf(DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE)))
            }

            @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
            override fun  analyze(imageProxy: ImageProxy) {
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    val buffer = mediaImage.planes[0].buffer
                    val bytes = ByteArray(buffer.remaining())
                    buffer.get(bytes)
                    val source = PlanarYUVLuminanceSource(bytes, mediaImage.width, mediaImage.height, 0, 0, mediaImage.width, mediaImage.height, false)
                    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
                    try {
                        val result = reader.decode(binaryBitmap)
                        onQRCodeScanned(result.text)
                    } catch (e: Exception) {
                        // QR code not found
                    } finally {
                        imageProxy.close()
                    }
                }
            }
        }
    }
}