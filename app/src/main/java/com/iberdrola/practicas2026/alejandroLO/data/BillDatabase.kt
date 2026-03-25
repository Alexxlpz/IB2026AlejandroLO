package com.iberdrola.practicas2026.alejandroLO.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.data.model.BillStatus
import com.iberdrola.practicas2026.alejandroLO.data.model.BillType
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsDao
import com.iberdrola.practicas2026.alejandroLO.data.repository.billStatus.BillStatusDao
import com.iberdrola.practicas2026.alejandroLO.data.repository.billType.BillTypeDao
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(
    entities = [Bill::class, BillType::class, BillStatus::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BillDatabase : RoomDatabase() {
    abstract fun billDao(): BillsDao
    abstract fun billTypeDao(): BillTypeDao
    abstract fun billStatusDao(): BillStatusDao

    companion object {
        @Volatile
        internal var Instance: BillDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BillDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BillDatabase::class.java,
                    "bill_database"
                )
                    .addCallback(BillDatabaseCallback(scope))
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

private class BillDatabaseCallback( // cogera todos los valores que tenemos en el enum y los carga en base de datos
    private val scope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        BillDatabase.Instance?.let { database ->
            scope.launch(Dispatchers.IO) {
                val typeDao = database.billTypeDao()
                val statusDao = database.billStatusDao()

                BillTypeEnum.entries.forEach { type ->
                    typeDao.insert(BillType(id = type.ordinal, type = type.title))
                }

                BillStatusEnum.entries.forEach { status ->
                    statusDao.insert(BillStatus(id = status.ordinal, status = status.title))
                }
            }
        }
    }
}