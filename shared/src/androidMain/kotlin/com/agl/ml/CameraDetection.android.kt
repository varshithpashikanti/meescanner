package com.agl.ml

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions


// Custom View that draws bounding boxes over detected objects
class ObjectOverlayView(context: android.content.Context) : View(context) {

    private val boxPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }

    private val textPaint = Paint().apply {
        color = Color.GREEN
        textSize = 40f
        style = Paint.Style.FILL
    }

    private val bgPaint = Paint().apply {
        color = Color.argb(160, 0, 0, 0)
        style = Paint.Style.FILL
    }

    var detectedObjects: List<DetectedObject> = emptyList()
        set(value) {
            field = value
            invalidate() // triggers redraw
        }

    // The preview dimensions are needed to scale bounding boxes correctly
    var imageWidth: Int = 1
    var imageHeight: Int = 1

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scaleX = width.toFloat() / imageHeight
        val scaleY = height.toFloat() / imageWidth

        for (obj in detectedObjects) {
            val box = obj.boundingBox

            val scaledBox = RectF(
                box.left * scaleX,
                box.top * scaleY,
                box.right * scaleX,
                box.bottom * scaleY
            )

            canvas.drawRect(scaledBox, boxPaint)

            val label = obj.labels.firstOrNull()?.text ?: "Unknown"
            val confidence = obj.labels.firstOrNull()?.confidence?.let {
                " ${(it * 100).toInt()}%"
            } ?: ""
            val displayText = "$label$confidence"

            val textWidth = textPaint.measureText(displayText)
            canvas.drawRect(
                scaledBox.left,
                scaledBox.top - 50f,
                scaledBox.left + textWidth + 8f,
                scaledBox.top,
                bgPaint
            )

            canvas.drawText(displayText, scaledBox.left + 4f, scaledBox.top - 10f, textPaint)
        }
    }
}


@OptIn(ExperimentalGetImage::class)
@Composable
actual fun CameraDetection(modifier: Modifier) {
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
        val lifecycleOwner = LocalLifecycleOwner.current

        // Holds detected objects from ML Kit — updated on each frame
        var detectedObjects by remember { mutableStateOf<List<DetectedObject>>(emptyList()) }
        var imageWidth by remember { mutableStateOf(1) }
        var imageHeight by remember { mutableStateOf(1) }

        val objectDetector = remember {
            val options = ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                .enableMultipleObjects()
                .enableClassification() // needed to get label.text
                .build()
            ObjectDetection.getClient(options)
        }

        Box(modifier = modifier.fillMaxSize()) {

            // Layer 1: Camera preview
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
                        }

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()

                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(ctx)
                        ) { imageProxy ->
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                // Capture frame dimensions for scaling the overlay
                                imageWidth = mediaImage.width
                                imageHeight = mediaImage.height

                                val image = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )

                                objectDetector.process(image)
                                    .addOnSuccessListener { objects ->
                                        detectedObjects = objects // ← triggers overlay redraw
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }
                            } else {
                                imageProxy.close()
                            }
                        }

                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageAnalysis ,
                        )

                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            // Layer 2: Bounding box overlay drawn on top of preview
            AndroidView(
                factory = { ctx -> ObjectOverlayView(ctx) },
                update = { overlayView ->
                    overlayView.detectedObjects = detectedObjects
                    overlayView.imageWidth = imageWidth
                    overlayView.imageHeight = imageHeight
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}