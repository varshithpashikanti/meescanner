package com.agl.ml.home.util

import coil3.Bitmap

object QrContentAnalyzer {

    fun analyze(
        rawValue: String,
        frame: Bitmap?
    ): QrContent {

        return when {

            rawValue.startsWith(
                prefix = "upi://pay",
                ignoreCase = true
            ) -> {
                QrContent.Upi(rawValue, frame)
            }

            rawValue.startsWith(
                prefix = "http://",
                ignoreCase = true
            ) || rawValue.startsWith(
                prefix = "https://",
                ignoreCase = true
            ) -> {
                QrContent.Url(rawValue, frame)
            }

            rawValue.startsWith(
                prefix = "WIFI:",
                ignoreCase = true
            ) -> {
                QrContent.Wifi(rawValue, frame)
            }

            rawValue.startsWith(
                prefix = "BEGIN:VCARD",
                ignoreCase = true
            ) -> {
                QrContent.Contact(rawValue, frame)
            }

            rawValue.startsWith(
                prefix = "mailto:",
                ignoreCase = true
            ) -> {
                QrContent.Email(rawValue, frame)
            }

            rawValue.startsWith(
                prefix = "tel:",
                ignoreCase = true
            ) -> {
                QrContent.Phone(rawValue, frame)
            }

            rawValue.startsWith(
                prefix = "sms:",
                ignoreCase = true
            ) || rawValue.startsWith(
                prefix = "smsto:",
                ignoreCase = true
            ) -> {
                QrContent.Sms(rawValue, frame)
            }

            else -> {
                QrContent.PlainText(rawValue, frame)
            }
        }
    }
}
