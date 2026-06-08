package com.agl.ml.home.util


object QrContentAnalyzer {

    fun analyze(
        rawValue: String
    ): QrContent {

        return when {

            rawValue.startsWith(
                prefix = "upi://pay",
                ignoreCase = true
            ) -> {
                QrContent.Upi(rawValue)
            }

            rawValue.startsWith(
                prefix = "http://",
                ignoreCase = true
            ) || rawValue.startsWith(
                prefix = "https://",
                ignoreCase = true
            ) -> {
                QrContent.Url(rawValue)
            }

            rawValue.startsWith(
                prefix = "WIFI:",
                ignoreCase = true
            ) -> {
                QrContent.Wifi(rawValue)
            }

            rawValue.startsWith(
                prefix = "BEGIN:VCARD",
                ignoreCase = true
            ) -> {
                QrContent.Contact(rawValue)
            }

            rawValue.startsWith(
                prefix = "mailto:",
                ignoreCase = true
            ) -> {
                QrContent.Email(rawValue)
            }

            rawValue.startsWith(
                prefix = "tel:",
                ignoreCase = true
            ) -> {
                QrContent.Phone(rawValue)
            }

            rawValue.startsWith(
                prefix = "sms:",
                ignoreCase = true
            ) || rawValue.startsWith(
                prefix = "smsto:",
                ignoreCase = true
            ) -> {
                QrContent.Sms(rawValue)
            }

            else -> {
                QrContent.PlainText(rawValue)
            }
        }
    }
}