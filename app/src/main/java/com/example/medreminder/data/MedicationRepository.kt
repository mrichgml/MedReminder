package com.example.medreminder.data

import com.example.medreminder.models.Dose
import com.example.medreminder.models.Medication
import kotlinx.coroutines.flow.Flow

class MedicationRepository(
    private val medicationDao: MedicationDao,
    private val doseDao: DoseDao
) {

    // Medication operations
    suspend fun addMedication(medication: Medication): Long {
        return medicationDao.insertMedication(medication)
    }

    suspend fun updateMedication(medication: Medication) {
        medicationDao.updateMedication(medication)
    }

    suspend fun deleteMedication(medication: Medication) {
        medicationDao.deleteMedication(medication)
        doseDao.deleteAllDosesForMedication(medication.id)
    }

    fun getActiveMedications(): Flow<List<Medication>> {
        return medicationDao.getActiveMedications()
    }

    fun getAllMedications(): Flow<List<Medication>> {
        return medicationDao.getAllMedications()
    }

    fun getActiveMedicationCount(): Flow<Int> {
        return medicationDao.getActiveMedicationCount()
    }

    suspend fun getMedicationById(id: Int): Medication? {
        return medicationDao.getMedicationById(id)
    }

    // Dose operations
    suspend fun recordDose(medicationId: Int): Long {
        return doseDao.insertDose(Dose(medicationId = medicationId))
    }

    suspend fun getLastDoseForMedication(medicationId: Int): Dose? {
        return doseDao.getLastDose(medicationId)
    }

    suspend fun getDosesInTimeRange(medicationId: Int, startTime: Long): List<Dose> {
        return doseDao.getDosesInTimeRange(medicationId, startTime)
    }

    suspend fun countDosesInTimeRange(medicationId: Int, startTime: Long): Int {
        return doseDao.countDosesInTimeRange(medicationId, startTime)
    }

    fun getDoseHistoryForMedication(medicationId: Int): Flow<List<Dose>> {
        return doseDao.getDoseHistoryForMedication(medicationId)
    }
}
