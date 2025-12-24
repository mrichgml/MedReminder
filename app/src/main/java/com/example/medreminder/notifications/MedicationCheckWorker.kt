package com.example.medreminder.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.medreminder.data.MedReminderDatabase
import com.example.medreminder.data.MedicationRepository
import com.example.medreminder.logic.DoseAvailabilityChecker
import kotlinx.coroutines.flow.first

class MedicationCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val database = MedReminderDatabase.getDatabase(applicationContext)
            val repository = MedicationRepository(
                database.medicationDao(),
                database.doseDao()
            )
            val doseChecker = DoseAvailabilityChecker()
            val notificationHelper = NotificationHelper(applicationContext)

            // Get all active medications
            val medications = repository.getActiveMedications().first()

            for (medication in medications) {
                // Only check medications with notifications enabled
                if (medication.notificationsEnabled) {
                    val lastDose = repository.getLastDoseForMedication(medication.id)
                    val dosesToday = repository.countDosesInTimeRange(
                        medication.id,
                        doseChecker.getStartOfDay()
                    )
                    val canTake = doseChecker.canTakeDose(medication, lastDose, dosesToday)

                    if (canTake) {
                        notificationHelper.sendDoseAvailableNotification(
                            medication.id,
                            medication.name,
                            medication.dosage
                        )
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

