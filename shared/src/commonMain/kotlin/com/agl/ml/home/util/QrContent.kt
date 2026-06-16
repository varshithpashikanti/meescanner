package com.appgolive.meescanner.util

import coil3.Bitmap

sealed class QrContent(open val frame: Bitmap?) {

    data class Upi(
        val rawValue: String,
        override val frame: Bitmap?
    ) : QrContent(frame)

    data class Url(
        val rawValue: String,
        override val frame: Bitmap?
    ) : QrContent(frame)

    data class Wifi(
        val rawValue: String,
        override val frame: Bitmap?
    ) : QrContent(frame)

    data class Contact(
        val rawValue: String,
        override val frame: Bitmap?
    ) : QrContent(frame)

    data class Email(
        val rawValue: String,
        override val frame: Bitmap?
    ) : QrContent(frame)

    data class Phone(
        val rawValue: String,
        override val frame: Bitmap?
    ) : QrContent(frame)

    data class Sms(
        val rawValue: String,
        override val frame: Bitmap?
    ) : QrContent(frame)

    data class PlainText(
        val rawValue: String,
        override val frame: Bitmap?
    ) : QrContent(frame)
}