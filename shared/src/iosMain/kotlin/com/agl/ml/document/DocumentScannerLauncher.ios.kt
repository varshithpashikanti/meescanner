package com.agl.ml.document

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.UIKit.UIApplication
import platform.UIKit.UIGraphicsBeginPDFContextToFile
import platform.UIKit.UIGraphicsBeginPDFPageWithInfo
import platform.UIKit.UIGraphicsEndPDFContext
import platform.UIKit.UIImageJPEGRepresentation
import platform.VisionKit.VNDocumentCameraScan
import platform.VisionKit.VNDocumentCameraViewController
import platform.VisionKit.VNDocumentCameraViewControllerDelegateProtocol
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
actual fun DocumentScanner(
    trigger: Boolean,
    onResult: (DocumentScanResult) -> Unit,
    onError: (String) -> Unit,
) {
    val launcherRef = remember { mutableStateOf<IOSDocumentScannerLauncher?>(null) }

    LaunchedEffect(Unit) {
        launcherRef.value = IOSDocumentScannerLauncher(
            onResult = onResult,
            onError = onError,
        )
    }

    LaunchedEffect(trigger) {
        if (!trigger) return@LaunchedEffect
        launcherRef.value?.launch()
    }
}

private class IOSDocumentScannerLauncher(
    private val onResult: (DocumentScanResult) -> Unit,
    private val onError: (String) -> Unit,
) {
    private var activeDelegate: DocumentCameraDelegate? = null

    fun launch() {
        dispatch_async(dispatch_get_main_queue()) {
            if (!VNDocumentCameraViewController.supported) {
                onError("This iPhone or iPad does not support document scanning.")
                return@dispatch_async
            }

            val presenter = topViewController()
            if (presenter == null) {
                onError("Unable to open the iOS document scanner from the current screen.")
                return@dispatch_async
            }

            val controller = VNDocumentCameraViewController()
            val delegate = DocumentCameraDelegate(
                onResult = onResult,
                onError = onError,
                onFinished = { activeDelegate = null },
            )
            activeDelegate = delegate
            controller.delegate = delegate
            presenter.presentViewController(controller, true, null)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class DocumentCameraDelegate(
    private val onResult: (DocumentScanResult) -> Unit,
    private val onError: (String) -> Unit,
    private val onFinished: () -> Unit,
) : NSObject(), VNDocumentCameraViewControllerDelegateProtocol {

    override fun documentCameraViewControllerDidCancel(controller: VNDocumentCameraViewController) {
        controller.dismissViewControllerAnimated(true, null)
        onError("User cancelled scan")
        onFinished()
    }

    override fun documentCameraViewController(
        controller: VNDocumentCameraViewController,
        didFailWithError: NSError,
    ) {
        controller.dismissViewControllerAnimated(true, null)
        onError(didFailWithError.localizedDescription ?: "Document scan failed on iOS.")
        onFinished()
    }

    override fun documentCameraViewController(
        controller: VNDocumentCameraViewController,
        didFinishWithScan: VNDocumentCameraScan,
    ) {
        controller.dismissViewControllerAnimated(true, null)

        runCatching {
            val savedPages = mutableListOf<ScannedDocumentPage>()
            val pageCount = didFinishWithScan.pageCount.toInt()

            for (index in 0 until pageCount) {
                val image = didFinishWithScan.imageOfPageAtIndex(index.toULong())
                val pagePath = temporaryFilePath(extension = "jpg")
                val pageUrl = NSURL.fileURLWithPath(pagePath)
                val jpegData = UIImageJPEGRepresentation(image, 0.94)
                    ?: error("Unable to encode scanned page.")
                NSFileManager.defaultManager.createFileAtPath(
                    path = pagePath,
                    contents = jpegData,
                    attributes = null,
                )
                savedPages += ScannedDocumentPage(
                    imageUri = pageUrl.absoluteString ?: pagePath,
                )
            }

            val pdfPath = temporaryFilePath(extension = "pdf")
            createPdfFromScan(
                scan = didFinishWithScan,
                pdfPath = pdfPath,
            )

            DocumentScanResult(
                pageCount = pageCount,
                pdfUri = NSURL.fileURLWithPath(pdfPath).absoluteString ?: pdfPath,
                pages = savedPages,
            )
        }.onSuccess { result ->
            onResult(result)
        }.onFailure { throwable ->
            onError(throwable.message ?: "Unable to build PDF from scanned pages.")
        }

        onFinished()
    }
}

// --- Helpers (unchanged) ---

private fun topViewController() = UIApplication.sharedApplication
    .keyWindow
    ?.rootViewController
    ?.topMostPresented()

private fun platform.UIKit.UIViewController.topMostPresented(): platform.UIKit.UIViewController {
    var controller = this
    while (controller.presentedViewController != null) {
        controller = controller.presentedViewController!!
    }
    return controller
}

private fun temporaryFilePath(extension: String): String {
    val fileName = "scan-${NSUUID().UUIDString}.$extension"
    return NSTemporaryDirectory().trimEnd('/') + "/" + fileName
}

@OptIn(ExperimentalForeignApi::class)
private fun createPdfFromScan(
    scan: VNDocumentCameraScan,
    pdfPath: String,
) {
    UIGraphicsBeginPDFContextToFile(pdfPath, CGRectMake(0.0, 0.0, 0.0, 0.0), null)
    try {
        val pageCount = scan.pageCount.toInt()
        for (index in 0 until pageCount) {
            val image = scan.imageOfPageAtIndex(index.toULong())
            val size = image.size
            val rect = size.useContents {
                CGRectMake(0.0, 0.0, width, height)
            }
            UIGraphicsBeginPDFPageWithInfo(rect, null)
            image.drawInRect(rect)
        }
    } finally {
        UIGraphicsEndPDFContext()
    }
}