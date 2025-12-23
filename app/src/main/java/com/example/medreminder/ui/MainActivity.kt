package com.example.medreminder.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.medreminder.R
import com.example.medreminder.data.MedReminderDatabase
import com.example.medreminder.data.MedicationRepository
import com.example.medreminder.logic.DoseAvailabilityChecker
import com.example.medreminder.models.Medication
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var gridMedications: GridLayout
    private lateinit var scrollViewMedications: ScrollView
    private lateinit var tvNoMedications: TextView
    private lateinit var btnAddMedication: Button
    private lateinit var repository: MedicationRepository
    private lateinit var doseChecker: DoseAvailabilityChecker
    private val medicationViews = mutableMapOf<Int, MedicationButtonView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        initializeDatabase()
        setupListeners()
        loadMedications()
    }

    private fun initializeViews() {
        gridMedications = findViewById(R.id.gridMedications)
        scrollViewMedications = findViewById(R.id.scrollViewMedications)
        tvNoMedications = findViewById(R.id.tvNoMedications)
        btnAddMedication = findViewById(R.id.btnAddMedication)
    }

    private fun initializeDatabase() {
        val database = MedReminderDatabase.getDatabase(this)
        repository = MedicationRepository(
            database.medicationDao(),
            database.doseDao()
        )
        doseChecker = DoseAvailabilityChecker()
    }

    private fun setupListeners() {
        btnAddMedication.setOnClickListener {
            showMedicationDialog(null)
        }
    }

    private fun loadMedications() {
        lifecycleScope.launch {
            repository.getActiveMedications().collect { medications ->
                updateUI(medications)
            }
        }
    }

    private fun updateUI(medications: List<Medication>) {
        gridMedications.removeAllViews()
        medicationViews.clear()

        if (medications.isEmpty()) {
            scrollViewMedications.visibility = ScrollView.GONE
            tvNoMedications.visibility = TextView.VISIBLE
        } else {
            scrollViewMedications.visibility = ScrollView.VISIBLE
            tvNoMedications.visibility = TextView.GONE

            for (medication in medications) {
                val medicationView = MedicationButtonView(
                    context = this,
                    medication = medication,
                    repository = repository,
                    doseChecker = doseChecker,
                    onEdit = { showMedicationDialog(medication) },
                    onDelete = { confirmDelete(medication) },
                    onDoseTaken = { refreshMedicationStatus(medication) }
                )
                medicationViews[medication.id] = medicationView
                gridMedications.addView(medicationView.view)
            }

            refreshAllMedicationStatus(medications)
        }
    }

    private fun refreshMedicationStatus(medication: Medication) {
        lifecycleScope.launch {
            medicationViews[medication.id]?.updateStatus()
        }
    }

    private fun refreshAllMedicationStatus(medications: List<Medication>) {
        lifecycleScope.launch {
            for (medication in medications) {
                medicationViews[medication.id]?.updateStatus()
            }
        }
    }

    private fun showMedicationDialog(medication: Medication?) {
        val dialog = MedicationDialog(this, medication, repository)
        dialog.show { newMedication ->
            lifecycleScope.launch {
                if (medication == null) {
                    repository.addMedication(newMedication)
                } else {
                    repository.updateMedication(newMedication)
                }
                loadMedications()
            }
        }
    }

    private fun confirmDelete(medication: Medication) {
        AlertDialog.Builder(this)
            .setTitle("Delete Medication")
            .setMessage("Are you sure you want to delete ${medication.name}?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    repository.deleteMedication(medication)
                    loadMedications()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
