package com.example.medreminder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.medreminder.models.Dose
import com.example.medreminder.models.Medication

@Database(
    entities = [Medication::class, Dose::class],
    version = 2,
    exportSchema = false
)
abstract class MedReminderDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun doseDao(): DoseDao

    companion object {
        @Volatile
        private var INSTANCE: MedReminderDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE medications ADD COLUMN notificationsEnabled INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): MedReminderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedReminderDatabase::class.java,
                    "med_reminder_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
