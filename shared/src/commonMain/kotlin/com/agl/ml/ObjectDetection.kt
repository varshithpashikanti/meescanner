package com.agl.ml

import androidx.compose.ui.graphics.ImageBitmap
expect class ObjectDetection() {
    suspend fun detectFromUrl(url: String): String

//    suspend fun detectFromCode(url : String): String
}