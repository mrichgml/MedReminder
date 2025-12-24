package com.example.medreminder.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import androidx.lifecycle.lifecycleScope
import com.example.medreminder.R
import com.example.medreminder.data.MedicationRepository
import com.example.medreminder.logic.DoseAvailabilityChecker
import com.example.medreminder.models.Medication
import com.example.medreminder.utils.TimeFormatter
import com.example.medreminder.utils.PreferencesManager
import kotlinx.coroutines.launch

class MedicationButtonView(
    private val context: Context,
    private val medication: Medication,
    private val repository: MedicationRepository,
    private val doseChecker: DoseAvailabilityChecker,
    private val onEdit: () -> Unit,
    private val onDelete: () -> Unit,
    private val onDoseTaken: () -> Unit
) {
    val view: View = LayoutInflater.from(context).inflate(R.layout.medication_button_item, null)
    private val btnMedication: Button = view.findViewById(R.id.btnMedication)
    private val btnEdit: Button = view.findViewById(R.id.btnEdit)
    private val btnDelete: Button = view.findViewById(R.id.btnDelete)
    private val prefsManager = PreferencesManager(context)

    init {
        // Ensure each row spans full width in the GridLayout
        val params = GridLayout.LayoutParams().apply {
            width = GridLayout.LayoutParams.MATCH_PARENT
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        }
        view.layoutParams = params
        setupListeners()
        // Initial status update will be called by the parent activity/fragment
    }

    private fun setupListeners() {
        btnMedication.setOnClickListener {
            recordDose()
        }
        btnEdit.setOnClickListener {
            onEdit()
        }
        btnDelete.setOnClickListener {
            onDelete()
        }
    }

    suspend fun updateStatus() {
        val lastDose = repository.getLastDoseForMedication(medication.id)
        val dosesToday = repository.countDosesInTimeRange(
            medication.id,
            doseChecker.getStartOfDay()
        )
        val canTake = doseChecker.canTakeDose(medication, lastDose, dosesToday)

        val context = view.context
        val textColor = context.getColor(android.R.color.white)
        val backgroundColor = if (canTake) {
            context.getColor(R.color.dose_available)
        } else {
            context.getColor(R.color.dose_not_available)
        }

        btnMedication.setBackgroundColor(backgroundColor)
        btnMedication.setTextColor(textColor)

        val use24Hour = prefsManager.use24HourFormat
        val timeText = if (lastDose != null) {
            val timeStr = TimeFormatter.formatTimestamp(lastDose.timestamp, use24Hour)
            val line1 = "${medication.name}\n(Taken: $timeStr)"

            // Add "Earliest Next" if dose is not currently available
            if (!canTake && dosesToday < medication.maxDosesPerDay) {
                val nextDoseTime = TimeFormatter.getEarliestNextDoseTime(lastDose.timestamp, medication.minTimeBetweenDoses)
                val nextDoseDisplay = if (prefsManager.showCountdown) {
                    TimeFormatter.formatCountdown(nextDoseTime)
                } else {
                    TimeFormatter.formatTimestamp(nextDoseTime, use24Hour)
                }
                "$line1\nEarliest Next: $nextDoseDisplay"
            } else {
                line1
            }
        } else {
            "${medication.name}\n(Never taken)"
        }

        btnMedication.text = timeText
        btnMedication.isEnabled = canTake
    }

    private fun recordDose() {
        val activity = context as? MainActivity
        activity?.lifecycleScope?.launch {
            repository.recordDose(medication.id)
            updateStatus()
            onDoseTaken()
        }
    }
}
