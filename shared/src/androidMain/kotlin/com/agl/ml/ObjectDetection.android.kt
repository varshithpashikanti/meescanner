package com.agl.ml

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.coroutines.resume

actual class ObjectDetection {

    private val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
        .enableMultipleObjects()
        .enableClassification()
        .build()

    private val detector = ObjectDetection.getClient(options)



    actual suspend fun detectFromUrl(url: String): String {

        val bitmap = withContext(Dispatchers.IO) {
            BitmapFactory.decodeStream(
                URL(url).openConnection().getInputStream()
            )
        }

        val image = InputImage.fromBitmap(bitmap, 0)

        return suspendCancellableCoroutine { continuation ->

            detector.process(image)
                .addOnSuccessListener { objects ->

                    val result = buildString {

                        appendLine("Objects Found: ${objects.size}")

                        objects.forEachIndexed { index, obj ->

                            appendLine("Object ${index + 1}")

                            if (obj.labels.isEmpty()) {
                                appendLine("No classification")
                            }

                            obj.labels.forEach { label ->
                                appendLine(
                                    "${label.text} (${(label.confidence * 100).toInt()}%)"
                                )
                            }

                            appendLine()
                        }
                    }

                    continuation.resume(result)
                }
                .addOnFailureListener {
                    continuation.resume("Error: ${it.message}")
                }
        }
    }

//    actual suspend fun detectFromCode(url: String): String {
//        val bitmap = withContext(Dispatchers.IO) {
//            BitmapFactory.decodeStream(
//                URL(url).openConnection().getInputStream()
//            )
//        }
//
//        val image = InputImage.fromBitmap(bitmap, 0)
//
//        return suspendCancellableCoroutine { continuation ->
//            qrdetector.process(image)
//                .addOnSuccessListener {
//                    val result = buildString {
//                        appendLine("Objects Found: ${it.size}")
//                        appendLine(it.fi)
//                }
//    }
//    }
}