package com.example.medreminder.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dosage: String,
    val minTimeBetweenDoses: Double, // hours (supports decimals like 0.5, 1.5, etc)
    val maxDosesPerDay: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val notificationsEnabled: Boolean = false
)
