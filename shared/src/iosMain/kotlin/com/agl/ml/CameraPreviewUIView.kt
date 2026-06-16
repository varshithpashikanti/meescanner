@file:OptIn(ExperimentalForeignApi::class)

package com.agl.ml

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.CoreGraphics.CGRectZero
import platform.QuartzCore.CATransaction
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
fun makeCameraPreviewView(session: AVCaptureSession): UIView {

    val previewLayer = AVCaptureVideoPreviewLayer(session = session)
    previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill

    // Subclass UIView so layoutSubviews() fires when the view gets its real size
    val view = object : UIView(frame = CGRectZero.readValue()) {
        override fun layoutSubviews() {
            super.layoutSubviews()
            // Now bounds is correct — update layer frame to match
            CATransaction.begin()
            CATransaction.setDisableActions(true)   // no animation when resizing
            previewLayer.setFrame(this.bounds)
            CATransaction.commit()
        }
    }

    view.layer.addSublayer(previewLayer)
    return view
}