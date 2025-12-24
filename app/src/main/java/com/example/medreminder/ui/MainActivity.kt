package com.example.medreminder.ui

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.GridLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.medreminder.R
import com.example.medreminder.data.MedReminderDatabase
import com.example.medreminder.data.MedicationRepository
import com.example.medreminder.logic.DoseAvailabilityChecker
import com.example.medreminder.models.Medication
import com.example.medreminder.notifications.NotificationScheduler
import com.example.medreminder.utils.PreferencesManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var gridMedications: GridLayout
    private lateinit var scrollViewMedications: ScrollView
    private lateinit var tvNoMedications: TextView
    private lateinit var btnAddMedication: Button
    private lateinit var repository: MedicationRepository
    private lateinit var doseChecker: DoseAvailabilityChecker
    private lateinit var prefsManager: PreferencesManager
    private val medicationViews = mutableMapOf<Int, MedicationButtonView>()

    private val updateHandler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            // Update all medication button colors
            lifecycleScope.launch {
                for ((_, view) in medicationViews) {
                    view.updateStatus()
                }
            }
            // Schedule next update in 60 seconds
            updateHandler.postDelayed(this, 60000)
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, schedule notification checks
            NotificationScheduler.scheduleNotificationChecks(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefsManager = PreferencesManager(this)
        initializeViews()
        initializeDatabase()
        setupListeners()
        loadMedications()
        requestNotificationPermissionIfNeeded()
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted, schedule notifications
                    NotificationScheduler.scheduleNotificationChecks(this)
                }
                else -> {
                    // Request permission
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // No runtime permission needed for older Android versions
            NotificationScheduler.scheduleNotificationChecks(this)
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu?.findItem(R.id.menu_toggle_countdown)?.isChecked = prefsManager.showCountdown
        menu?.findItem(R.id.menu_toggle_24hour)?.isChecked = prefsManager.use24HourFormat
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_toggle_countdown -> {
                prefsManager.showCountdown = !prefsManager.showCountdown
                item.isChecked = prefsManager.showCountdown
                // Refresh all medication views to show the updated display mode
                lifecycleScope.launch {
                    for ((_, view) in medicationViews) {
                        view.updateStatus()
                    }
                }
                true
            }
            R.id.menu_toggle_24hour -> {
                prefsManager.use24HourFormat = !prefsManager.use24HourFormat
                item.isChecked = prefsManager.use24HourFormat
                // Refresh all medication views to show the updated time format
                lifecycleScope.launch {
                    for ((_, view) in medicationViews) {
                        view.updateStatus()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        // Start periodic updates when app comes to foreground
        updateHandler.post(updateRunnable)
    }

    override fun onPause() {
        super.onPause()
        // Stop periodic updates when app goes to background
        updateHandler.removeCallbacks(updateRunnable)
    }
}
