package com.example.medreminder.utils

object TimeFormatter {
    fun formatTimestamp(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    fun formatTimestampFull(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    fun getTimeDifferenceMinutes(earlierTime: Long, laterTime: Long): Long {
        return (laterTime - earlierTime) / (1000 * 60)
    }
}
