//package com.agl.ml
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.viewinterop.UIKitView
//import com.agl.ml.home.util.AnalyzerType
//import com.agl.ml.home.util.ScanResult
//import kotlinx.cinterop.ExperimentalForeignApi
//import platform.AVFoundation.*
//
//@Composable
//actual fun CameraPreview(
//    analyzerType: AnalyzerType,
//    onScanResult: (ScanResult) -> Unit,
//    triggerCapture: Boolean,
//    onCaptureCompleted: () -> Unit,
//    modifier: Modifier
//) {
//
//    var permissionStatus by remember {
//        mutableStateOf(
//            AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
//        )
//    }
//
//    LaunchedEffect(Unit) {
//        if (permissionStatus == AVAuthorizationStatusNotDetermined) {
//            AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
//                permissionStatus = if (granted)
//                    AVAuthorizationStatusAuthorized
//                else
//                    AVAuthorizationStatusDenied
//            }
//        }
//    }
//
//    when (permissionStatus) {
//        AVAuthorizationStatusAuthorized -> {
//            // Step C: Permission granted — show camera
//            CameraView(
//                analyzerType = analyzerType,
//                onScanResult = onScanResult,
//                modifier = modifier
//            )
//        }
//
//        AVAuthorizationStatusDenied, AVAuthorizationStatusRestricted -> {
//            Column(
//                modifier = modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(
//                    text = "Need Camera Permission",
//                    color = Color.White
//                )
//                Button(
//                    onClick = {
//
//                    }
//                ) {
//                    Text("Open Settings")
//                }
//            }
//        }
//
//        else -> {
//            // AVAuthorizationStatusNotDetermined — waiting for dialog response
//            Box(modifier = modifier, contentAlignment = Alignment.Center) {
//                CircularProgressIndicator()
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalForeignApi::class)
//@Composable
//private fun CameraView(
//    analyzerType: AnalyzerType,
//    onScanResult: (ScanResult) -> Unit,
//    modifier: Modifier
//) {
//    // AVCaptureSession = iOS equivalent of ProcessCameraProvider + CameraSelector
//    val session = remember {
//        AVCaptureSession().apply {
//            beginConfiguration()
//
//            // Pick the back camera — same as CameraSelector.DEFAULT_BACK_CAMERA
//            val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
//            val input: AVCaptureDeviceInput? = device?.let {
//                AVCaptureDeviceInput.deviceInputWithDevice(it, null) as? AVCaptureDeviceInput
//            }
//            if (input != null && canAddInput(input)) {
//                addInput(input)
//            }
//
//            commitConfiguration()
//        }
//    }
//
//    // Start the session (like cameraProvider.bindToLifecycle)
//    LaunchedEffect(Unit) {
//        session.startRunning()
//    }
//
//    // Stop the session when the composable leaves the screen
//    // Equivalent of cameraProvider.unbindAll() on Android
//    DisposableEffect(Unit) {
//        onDispose {
//            session.stopRunning()
//        }
//    }
//
//    Box(modifier = modifier) {
//        // Show the camera feed using UIViewRepresentable
//        UIKitView(
//            factory = {
//                // Creates CameraPreviewUIView from Step 2
//                makeCameraPreviewView(session)
//            },
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//}