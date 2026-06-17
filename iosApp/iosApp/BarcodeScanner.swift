//
//  BarcodeScanner.swift
//  iosApp
//
//  Created by Varshith on 17/06/26.
//

import Foundation
import MLKit
import MLKitBarcodeScanning
import AVFoundation

@objcMembers
public class IOSBarcodeScanner : NSObject {

    private let scanner = BarcodeScanner.barcodeScanner()

    @objc
    public func scan(
        sampleBuffer: CMSampleBuffer,
        completion: @escaping (String?) -> Void
    ) {

        let image = VisionImage(buffer: sampleBuffer)

        image.orientation = .up

        scanner.process(image) { barcodes, error in

            if let barcode = barcodes?.first {

                completion(barcode.rawValue)

            } else {

                completion(nil)

            }
        }
    }
}
