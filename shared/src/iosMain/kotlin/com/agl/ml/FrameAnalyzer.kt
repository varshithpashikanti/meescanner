package com.agl.ml

import com.agl.ml.home.util.AnalyzerType
import com.agl.ml.home.util.ScanResult
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.CoreMedia.CMSampleBufferRef
import platform.darwin.NSObject

class IOSFrameAnalyzer(
    private val analyzerType: AnalyzerType,
    private val onScanResult: (ScanResult) -> Unit,
    private val triggerCaptureProvider: () -> Boolean,
    private val onCaptureCompleted: () -> Unit
) : NSObject(), AVCaptureVideoDataOutputSampleBufferDelegateProtocol {


    @OptIn(ExperimentalForeignApi::class)
    override fun captureOutput(
        output: AVCaptureOutput,
        didOutputSampleBuffer: CMSampleBufferRef?,
        fromConnection: AVCaptureConnection
    ) {


        if (didOutputSampleBuffer == null) return

       println("Frame received")
    }
}
