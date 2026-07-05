package com.example.spendlite.core.utils

import java.text.NumberFormat
import java.util.Locale

fun Double.toFormattedAmount(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}