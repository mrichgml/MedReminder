package com.example.medreminder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medreminder.models.Dose
import com.example.medreminder.models.Medication

@Database(
    entities = [Medication::class, Dose::class],
    version = 1,
    exportSchema = false
)
abstract class MedReminderDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun doseDao(): DoseDao

    companion object {
        @Volatile
        private var INSTANCE: MedReminderDatabase? = null

        fun getDatabase(context: Context): MedReminderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedReminderDatabase::class.java,
                    "med_reminder_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
