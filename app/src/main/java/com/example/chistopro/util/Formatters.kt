package com.example.chistopro.util

import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val ru: Locale = Locale.forLanguageTag("ru")

/** "Пятница, 13 июня 2025" */
fun formatFullDate(dateTime: LocalDateTime): String =
    dateTime.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", ru))
        .replaceFirstChar { it.uppercase() }

/** "8 400 ₽" */
fun formatPrice(value: Double): String =
    NumberFormat.getIntegerInstance(ru).format(value) + " ₽"

/** 120.0 -> "120", 45.5 -> "45.5" */
fun formatArea(value: Double): String =
    value.toBigDecimal().stripTrailingZeros().toPlainString()
