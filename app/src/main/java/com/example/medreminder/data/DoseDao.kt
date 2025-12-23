package com.example.medreminder.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.medreminder.models.Dose
import kotlinx.coroutines.flow.Flow

@Dao
interface DoseDao {
    @Insert
    suspend fun insertDose(dose: Dose): Long

    @Delete
    suspend fun deleteDose(dose: Dose)

    @Query("SELECT * FROM doses WHERE medicationId = :medicationId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastDose(medicationId: Int): Dose?

    @Query("SELECT * FROM doses WHERE medicationId = :medicationId AND timestamp >= :startTime ORDER BY timestamp DESC")
    suspend fun getDosesInTimeRange(medicationId: Int, startTime: Long): List<Dose>

    @Query("SELECT COUNT(*) FROM doses WHERE medicationId = :medicationId AND timestamp >= :startTime")
    suspend fun countDosesInTimeRange(medicationId: Int, startTime: Long): Int

    @Query("DELETE FROM doses WHERE medicationId = :medicationId")
    suspend fun deleteAllDosesForMedication(medicationId: Int)

    @Query("SELECT * FROM doses WHERE medicationId = :medicationId ORDER BY timestamp DESC")
    fun getDoseHistoryForMedication(medicationId: Int): Flow<List<Dose>>
}
