package com.agl.ml

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
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
import com.agl.ml.home.util.AnalyzerType
import com.agl.ml.home.util.ScanResult
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@OptIn(ExperimentalGetImage::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
actual fun CameraPreview(
    analyzerType: AnalyzerType,
    onScanResult: (ScanResult) -> Unit,
    modifier: Modifier
) {

    val context = LocalContext.current

    var barcodeValue by remember { mutableStateOf("") }
    var barcodeFormat by remember { mutableStateOf("") }
    var barcodeBounds by remember { mutableStateOf("") }
    var barcodeCorners by remember { mutableStateOf("") }


    var frame by remember {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(analyzerType) {
        frame = null

        barcodeValue = ""
        barcodeFormat = ""
        barcodeBounds = ""
        barcodeCorners = ""
    }

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

        Box(modifier = modifier) {

            if (frame != null) {

                Image(
                    bitmap = frame!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

            } else {

                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize()
                )
            }

        }

        LaunchedEffect(
            previewView,
            analyzerType
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
                                            rawValue = barcode.rawValue.orEmpty()
                                        )
                                    )

                                    frame = previewView.bitmap

                                    barcodeValue =
                                        barcode.rawValue.orEmpty()

                                    barcodeFormat =
                                        barcode.format.toString()

                                    barcodeBounds =
                                        barcode.boundingBox.toString()

                                    barcodeCorners =
                                        barcode.cornerPoints.toString()

                                    cameraProvider.unbindAll()
                                }
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    }

                    AnalyzerType.TEXT,
                    AnalyzerType.OBJECT,
                    AnalyzerType.DOCUMENT,
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


