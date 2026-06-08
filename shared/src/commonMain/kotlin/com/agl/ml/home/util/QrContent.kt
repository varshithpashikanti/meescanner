package com.agl.ml.home.util

sealed class QrContent {

    data class Upi(
        val rawValue: String
    ) : QrContent()

    data class Url(
        val rawValue: String
    ) : QrContent()

    data class Wifi(
        val rawValue: String
    ) : QrContent()

    data class Contact(
        val rawValue: String
    ) : QrContent()

    data class Email(
        val rawValue: String
    ) : QrContent()

    data class Phone(
        val rawValue: String
    ) : QrContent()

    data class Sms(
        val rawValue: String
    ) : QrContent()

    data class PlainText(
        val rawValue: String
    ) : QrContent()
}