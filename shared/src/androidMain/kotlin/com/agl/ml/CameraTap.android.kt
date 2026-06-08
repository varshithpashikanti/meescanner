package com.agl.ml

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@Composable
actual fun CameraTap(modifier: Modifier) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var scanResult by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) launcher.launch(Manifest.permission.CAMERA)
    }

    val previewView = remember { PreviewView(context) }

    val cameraProvider = remember {
        ProcessCameraProvider.getInstance(context)
    }

    LaunchedEffect(hasPermission) {
        if (!hasPermission) return@LaunchedEffect

        val cameraProvider = cameraProvider.get()

        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        cameraProvider.unbindAll()

        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            imageAnalysis
        )
    }

    Box(modifier = modifier.fillMaxSize()) {

        if (capturedBitmap == null) {

            if (hasPermission) {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize()
                )
            }

        } else {

            Image(
                bitmap = capturedBitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0xFF323232))
                .padding(40.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if(scanResult != null) {
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp) , horizontalArrangement = Arrangement.Center){
                    Text(
                        text = scanResult!!,
                        color = Color.White
                    )
                }
            }

            if(capturedBitmap == null) {
                Button(
                    onClick = {
                        capturedBitmap = previewView.bitmap
                        analyzeBarcode(capturedBitmap!!) { result ->
                            scanResult = result
                        }
                    }
                ) {
                    Text("Capture")
                }
            }


            if (capturedBitmap != null) {
                Button(
                    onClick = {
                        capturedBitmap = null
                    }
                ) {
                    Text("Retake")
                }
            }
        }
    }
}

fun analyzeBarcode(bitmap: Bitmap, onResult: (String) -> Unit) {

    val image = InputImage.fromBitmap(bitmap,0)

    val scanner = BarcodeScanning.getClient()

    scanner.process(image)
        .addOnSuccessListener { barcodes ->

            val result = barcodes.firstOrNull()?.rawValue

            onResult(result ?: "No QR found")
        }
        .addOnFailureListener {
            onResult("Error: ${it.message}")
        }
}