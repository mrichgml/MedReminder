package com.example.medreminder.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dosage: String,
    val minTimeBetweenDoses: Long, // minutes
    val maxDosesPerDay: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)
