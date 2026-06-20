package com.agl.ml.history.ui

import kotlinx.datetime.*

fun Long.toFormattedDateTime(): String {
    val dateTime = Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())

    return dateTime.format(
        LocalDateTime.Format {
            dayOfMonth()
            chars("/")
            monthNumber()
            chars("/")
            year()
            chars(" ")
            hour()
            chars(":")
            minute()
        }
    )
}