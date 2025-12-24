package com.example.medreminder.logic

import com.example.medreminder.models.Dose
import com.example.medreminder.models.Medication
import java.util.Calendar

class DoseAvailabilityChecker {

    fun canTakeDose(
        medication: Medication,
        lastDose: Dose?,
        dosesTodayCount: Int,
        currentTime: Long = System.currentTimeMillis()
    ): Boolean {
        // Check if maximum doses per day exceeded
        if (dosesTodayCount >= medication.maxDosesPerDay) {
            return false
        }

        // If no dose has been taken, it's available
        if (lastDose == null) {
            return true
        }

        // Check if minimum time between doses has elapsed
        val minTimeMs = (medication.minTimeBetweenDoses * 60 * 60 * 1000).toLong()
        val timeSinceLastDose = currentTime - lastDose.timestamp

        return timeSinceLastDose >= minTimeMs
    }

    fun getTimeUntilNextDose(
        medication: Medication,
        lastDose: Dose?,
        currentTime: Long = System.currentTimeMillis()
    ): Long {
        if (lastDose == null) {
            return 0
        }

        val minTimeMs = (medication.minTimeBetweenDoses * 60 * 60 * 1000).toLong()
        val timeSinceLastDose = currentTime - lastDose.timestamp

        return if (timeSinceLastDose < minTimeMs) {
            minTimeMs - timeSinceLastDose
        } else {
            0
        }
    }

    fun getStartOfDay(timestamp: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
