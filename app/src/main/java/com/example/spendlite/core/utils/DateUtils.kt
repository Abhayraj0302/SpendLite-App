package com.example.spendlite.core.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    fun formatDate(millis: Long): String {
        return formatter.format(Date(millis))
    }

    fun parseDate(dateStr: String): Date? {
        return try {
            formatter.parse(dateStr)
        } catch (e: Exception) {
            null
        }
    }
    fun monthYear(dateStr: String): Pair<Int, Int>? {
        val date = parseDate(dateStr) ?: return null
        val cal = Calendar.getInstance().apply { time = date }
        return cal.get(Calendar.MONTH) to cal.get(Calendar.YEAR)
    }
}