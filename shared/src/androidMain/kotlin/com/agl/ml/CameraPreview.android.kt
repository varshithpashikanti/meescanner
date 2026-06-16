package com.appgolive.meescanner

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.appgolive.meescanner.home.util.AnalyzerType
import com.appgolive.meescanner.home.util.DetectedObjectInfo
import com.appgolive.meescanner.home.util.ScanResult
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.*
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@OptIn(ExperimentalGetImage::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
actual fun CameraPreview(
    analyzerType: AnalyzerType,
    onScanResult: (ScanResult) -> Unit,
    triggerCapture: Boolean,
    onCaptureCompleted: () -> Unit,
    modifier: Modifier
) {

    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    if (hasCameraPermission) {

        val previewView = remember {
            PreviewView(context)
        }

        val lifecycleOwner = LocalLifecycleOwner.current

        val cameraProviderFuture = remember {
            ProcessCameraProvider.getInstance(context)
        }

        val barcodeScanner = remember {
            BarcodeScanning.getClient()
        }

        val objectDetector = remember {
            val options = ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build()
            ObjectDetection.getClient(options)
        }

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)




        Box(modifier = modifier) {

            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )

        }

        LaunchedEffect(triggerCapture) {


            if (triggerCapture && analyzerType == AnalyzerType.OBJECT) {
                val originalBitmap = previewView.bitmap ?: return@LaunchedEffect
                val image = InputImage.fromBitmap(originalBitmap, 0)
                objectDetector.process(image)
                    .addOnSuccessListener { objects ->
                        val detectedObjects = objects.map { obj ->
                            val box = obj.boundingBox

                            // 1. Constrain coordinates within original bitmap boundaries to prevent crashes
                            val left = box.left.coerceIn(0, originalBitmap.width)
                            val top = box.top.coerceIn(0, originalBitmap.height)
                            val right = box.right.coerceIn(0, originalBitmap.width)
                            val bottom = box.bottom.coerceIn(0, originalBitmap.height)

                            // 2. Calculate valid width and height
                            val width = right - left
                            val height = bottom - top

                            // 3. Perform the crop only if the dimensions are valid
                            val croppedBitmap = if (width > 0 && height > 0) {
                                Bitmap.createBitmap(originalBitmap, left, top, width, height)
                            } else {
                                null
                            }

                            DetectedObjectInfo(
                                label = obj.labels.firstOrNull()?.text ?: "Unknown",
                                confidence = obj.labels.firstOrNull()?.confidence ?: 0f,
                                boundingLeft = left,
                                boundingTop = top,
                                boundingRight = right,
                                boundingBottom = bottom,
                                croppedImage = croppedBitmap
                            )
                        }
                        onScanResult(
                            ScanResult.ObjectResult(
                                frame = previewView.bitmap,
                                objects = detectedObjects
                            )
                        )
                        onCaptureCompleted()
                    }
            }
        }


        LaunchedEffect(
            previewView,
            analyzerType,
            triggerCapture
        ) {

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()

            val imageAnalysis = ImageAnalysis.Builder().build()

            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(context)
            ) { imageProxy ->

                val mediaImage = imageProxy.image

                if (mediaImage == null) {
                    imageProxy.close()
                    return@setAnalyzer
                }

                val image = InputImage.fromMediaImage(
                    mediaImage,
                    imageProxy.imageInfo.rotationDegrees
                )



                when (analyzerType) {

                    AnalyzerType.QR -> {

                        barcodeScanner.process(image)
                            .addOnSuccessListener { barcodes ->

                                if (barcodes.isNotEmpty()) {

                                    val barcode = barcodes.first()
                                    onScanResult(
                                        ScanResult.QrResult(
                                            frame = previewView.bitmap,
                                            rawValue = barcode.rawValue.orEmpty()

                                        )
                                    )

                                    cameraProvider.unbindAll()
                                }
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    }

                    AnalyzerType.OBJECT -> {
                        imageProxy.close()
                    }

                    AnalyzerType.TEXT -> {
                        if(triggerCapture) {
                            recognizer.process(image)
                                .addOnSuccessListener { visionText ->
                                    // Task completed successfully
                                    // ...
                                    onScanResult(
                                        ScanResult.TextResult(
                                            frame = previewView.bitmap,
                                            text = visionText.text
                                        )
                                    )
                                    onCaptureCompleted()
                                    imageProxy.close()

                                }
                                .addOnFailureListener { e ->
                                    imageProxy.close()
                                }
                        }
                    }

                    AnalyzerType.DOCUMENT -> {
                        imageProxy.close()
                    }

                    AnalyzerType.PHOTO -> {
                        imageProxy.close()
                    }
                }
            }

            preview.surfaceProvider =
                previewView.surfaceProvider

            cameraProvider.unbindAll()

            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalysis
            )
        }

    } else {

        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Need Camera Permission",
                color = Color.White
            )

            Button(
                onClick = {
                    launcher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            ) {
                Text("Grant Permission")
            }
        }
    }

}


