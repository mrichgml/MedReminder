package com.example.medreminder.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    private const val WORK_NAME = "medication_check_worker"
    private const val CHECK_INTERVAL_MINUTES = 15L

    fun scheduleNotificationChecks(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<MedicationCheckWorker>(
            CHECK_INTERVAL_MINUTES,
            TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    fun cancelNotificationChecks(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}

