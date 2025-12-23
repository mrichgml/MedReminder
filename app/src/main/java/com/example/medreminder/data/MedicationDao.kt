package com.example.medreminder.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medreminder.models.Medication
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Insert
    suspend fun insertMedication(medication: Medication): Long

    @Update
    suspend fun updateMedication(medication: Medication)

    @Delete
    suspend fun deleteMedication(medication: Medication)

    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getMedicationById(id: Int): Medication?

    @Query("SELECT * FROM medications WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveMedications(): Flow<List<Medication>>

    @Query("SELECT * FROM medications ORDER BY name ASC")
    fun getAllMedications(): Flow<List<Medication>>

    @Query("SELECT COUNT(*) FROM medications WHERE isActive = 1")
    fun getActiveMedicationCount(): Flow<Int>

    @Query("DELETE FROM medications WHERE id = :id")
    suspend fun deleteMedicationById(id: Int)
}
