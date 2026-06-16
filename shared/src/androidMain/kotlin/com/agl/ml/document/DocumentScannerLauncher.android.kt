package com.agl.ml.document

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import kotlin.collections.orEmpty


private tailrec fun Context.findActivity(): ComponentActivity? {
    return when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}


@Composable
actual fun DocumentScanner(
    trigger: Boolean,
    onResult: (DocumentScanResult) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current

    val activity = remember(context) { context.findActivity() }

    val options = remember {
        GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true)
            .setPageLimit(20)
            .setResultFormats(
                GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
                GmsDocumentScannerOptions.RESULT_FORMAT_PDF
            )
            .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
            .build()
    }

    val scanner = remember { GmsDocumentScanning.getClient(options) }

    val launcher = rememberLauncherForActivityResult(
        contract = StartIntentSenderForResult()
    ) { activityResult ->

        if (activityResult.resultCode != Activity.RESULT_OK) {
            onError("User cancelled scan")
            return@rememberLauncherForActivityResult
        }

        val gmsResult = GmsDocumentScanningResult
            .fromActivityResultIntent(activityResult.data)

        if (gmsResult == null) {
            onError("Scan returned no data")
            return@rememberLauncherForActivityResult
        }

        onResult(
            DocumentScanResult(
                pageCount = gmsResult.pages?.size ?: 0,
                pdfUri = gmsResult.pdf?.uri?.toString(),
                pages = gmsResult.pages
                    ?.map { page -> ScannedDocumentPage(page.imageUri.toString()) }
                    ?: emptyList()
            )
        )
    }

    LaunchedEffect(trigger) {
        if (!trigger) return@LaunchedEffect

        if (activity == null) {
            onError("Cannot open scanner")
            return@LaunchedEffect
        }

        scanner.getStartScanIntent(activity)
            .addOnSuccessListener { intentSender ->
                launcher.launch(
                    IntentSenderRequest.Builder(intentSender).build()
                )
            }
            .addOnFailureListener { error ->
                onError(error.message ?: "Scanner failed to start")
            }
    }
}