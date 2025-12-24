package com.example.medreminder.utils

object TimeFormatter {
    fun formatTimestamp(timestamp: Long, use24Hour: Boolean = false): String {
        val pattern = if (use24Hour) "HH:mm" else "hh:mm a"
        val sdf = java.text.SimpleDateFormat(pattern, java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    fun formatTimestampFull(timestamp: Long, use24Hour: Boolean = false): String {
        val pattern = if (use24Hour) "MMM dd, HH:mm" else "MMM dd, hh:mm a"
        val sdf = java.text.SimpleDateFormat(pattern, java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    fun getTimeDifferenceMinutes(earlierTime: Long, laterTime: Long): Long {
        return (laterTime - earlierTime) / (1000 * 60)
    }

    fun formatTimeUntil(milliseconds: Long): String {
        val totalMinutes = milliseconds / (1000 * 60)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            else -> "${minutes}m"
        }
    }

    fun getEarliestNextDoseTime(lastDoseTimestamp: Long, minTimeBetweenHours: Double): Long {
        return lastDoseTimestamp + (minTimeBetweenHours * 60 * 60 * 1000).toLong()
    }

    fun formatCountdown(targetTimestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = targetTimestamp - now

        if (diff <= 0) {
            return "Now"
        }

        val totalMinutes = diff / (1000 * 60)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            else -> "${minutes}m"
        }
    }
}
