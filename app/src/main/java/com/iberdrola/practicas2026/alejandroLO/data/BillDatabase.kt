package com.iberdrola.practicas2026.alejandroLO.data

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsDao


@Database(entities = [Bill::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BillDatabase : RoomDatabase() {
    abstract fun billDao(): BillsDao

    companion object {
        @Volatile
        private var Instance: BillDatabase? = null

        fun getDatabase(context: Context): BillDatabase {
            return Instance ?: synchronized(this) {
                androidx.room.Room.databaseBuilder(context, BillDatabase::class.java, "bill_database")
                    .build().also { Instance = it }
            }
        }
    }
}