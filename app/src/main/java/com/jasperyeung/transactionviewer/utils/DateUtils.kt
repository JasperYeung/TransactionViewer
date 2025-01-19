package com.jasperyeung.transactionviewer.utils

import android.icu.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val inputFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    fun formatTimestamp(timestamp: String?): Pair<String?, String?> {
        if (timestamp == null) return Pair(null, null)

        return try {
            val date = inputFormat.parse(timestamp)
            val dateString = dateFormat.format(date)
            val timeString = timeFormat.format(date)
            Pair(dateString, timeString)
        } catch (e: Exception) {
            Pair(null, null)
        }
    }
}