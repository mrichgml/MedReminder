package com.example.medreminder.ui

import android.content.Context
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.medreminder.R
import com.example.medreminder.data.MedicationRepository
import com.example.medreminder.models.Medication

class MedicationDialog(
    private val context: Context,
    private val medication: Medication?,
    private val repository: MedicationRepository
) {

    fun show(onSave: (Medication) -> Unit) {
        val dialogView = android.view.LayoutInflater.from(context)
            .inflate(R.layout.dialog_medication, null)

        val etName = dialogView.findViewById<EditText>(R.id.etMedicationName)
        val etDosage = dialogView.findViewById<EditText>(R.id.etDosage)
        val etMinTime = dialogView.findViewById<EditText>(R.id.etMinTimeBetweenDoses)
        val etMaxDoses = dialogView.findViewById<EditText>(R.id.etMaxDosesPerDay)

        // Populate fields if editing
        if (medication != null) {
            etName.setText(medication.name)
            etDosage.setText(medication.dosage)
            etMinTime.setText(medication.minTimeBetweenDoses.toString())
            etMaxDoses.setText(medication.maxDosesPerDay.toString())
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle(if (medication == null) "Add Medication" else "Edit Medication")
            .setView(dialogView)
            .setNegativeButton("Cancel", null)
            .create()

        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val dosage = etDosage.text.toString().trim()
            val minTime = etMinTime.text.toString().toLongOrNull() ?: 0L
            val maxDoses = etMaxDoses.text.toString().toIntOrNull() ?: 0

            if (name.isNotEmpty() && dosage.isNotEmpty() && minTime > 0 && maxDoses > 0) {
                val newMedication = if (medication != null) {
                    medication.copy(
                        name = name,
                        dosage = dosage,
                        minTimeBetweenDoses = minTime,
                        maxDosesPerDay = maxDoses
                    )
                } else {
                    Medication(
                        name = name,
                        dosage = dosage,
                        minTimeBetweenDoses = minTime,
                        maxDosesPerDay = maxDoses
                    )
                }
                onSave(newMedication)
                dialog.dismiss()
            } else {
                etName.error = if (name.isEmpty()) "Required" else null
                etDosage.error = if (dosage.isEmpty()) "Required" else null
                etMinTime.error = if (minTime <= 0) "Must be > 0" else null
                etMaxDoses.error = if (maxDoses <= 0) "Must be > 0" else null
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
