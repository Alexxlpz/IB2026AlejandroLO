package com.iberdrola.practicas2026.alejandroLO.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.data.model.BillStatus
import com.iberdrola.practicas2026.alejandroLO.data.model.BillType
import com.iberdrola.practicas2026.alejandroLO.data.model.Direction
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsDao
import com.iberdrola.practicas2026.alejandroLO.data.repository.billStatus.BillStatusDao
import com.iberdrola.practicas2026.alejandroLO.data.repository.billType.BillTypeDao
import com.iberdrola.practicas2026.alejandroLO.data.repository.direction.DirectionDao
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum


@Database(
    entities = [
        Bill::class,
        BillType::class,
        BillStatus::class,
        Direction::class
    ],
    version = 18,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BillDatabase : RoomDatabase() {
    abstract fun billDao(): BillsDao
    abstract fun billTypeDao(): BillTypeDao
    abstract fun billStatusDao(): BillStatusDao

    abstract fun directionDao(): DirectionDao

    val TAG: String = "BillDatabase"

    suspend fun clearDatabase() {
        this.withTransaction { // todxo en una transicion para que si se borra algo que no sea a medias
            Log.d(TAG, "clearDatabase: Iniciando limpieza de tablas...")
            billDao().deleteAllSync()
            Log.d(TAG, "clearDatabase: Bills eliminadas")

            directionDao().deleteAllSync()
            Log.d(TAG, "clearDatabase: Direcciones eliminadas")
        }
    }

    companion object {
        @Volatile
        internal var Instance: BillDatabase? = null

        fun getDatabase(context: Context): BillDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BillDatabase::class.java,
                    "bill_database"
                )
                    .addCallback(BillDatabaseCallback())
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

// voy a realizarla  de forma sincrona para que se añadan antes los tipos y status,
// ya que si insertamos las facturas antes en el bill nos da violacion de claves foraneas
private class BillDatabaseCallback: RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        populateMasterData(db)
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        populateMasterData(db)
    }

    private fun populateMasterData(db: SupportSQLiteDatabase) {
        BillTypeEnum.entries.forEach { type ->
            db.execSQL("INSERT OR IGNORE INTO billType (id, type) VALUES (${type.ordinal}, '${type.title}')")
        }
        BillStatusEnum.entries.forEach { status ->
            db.execSQL("INSERT OR IGNORE INTO billStatus (id, status) VALUES (${status.ordinal}, '${status.title}')")
        }
    }
}

//private suspend fun verSiHayDatos() {
//    val typeDao = BillDatabase.Instance?.billTypeDao()
//    val statusDao = BillDatabase.Instance?.billStatusDao()
//
//    if (typeDao != null && statusDao != null) {
//        val types = typeDao.getAllBillTypes()
//        val statuses = statusDao.getAllBillStatus()
//
//        types.collect { types ->
//            Log.d("BillDatabaseCallback", "Tipo: ${types}")
//        }
//
//        statuses.collect { status ->
//            Log.d("BillDatabaseCallback", "Status: ${status}")
//        }
//    }
//}


//private class BillDatabaseCallback( // cogera todos los valores que tenemos en el enum y los carga en base de datos
//    private val scope: CoroutineScope
//) : RoomDatabase.Callback() {
//
//    override fun onCreate(db: SupportSQLiteDatabase) {
//        super.onCreate(db)
//        BillDatabase.Instance?.let { database ->
//            scope.launch(Dispatchers.IO) {
//                val typeDao = database.billTypeDao()
//                val statusDao = database.billStatusDao()
//
//                BillTypeEnum.entries.forEach { type ->
//                    typeDao.insert(BillType(id = type.ordinal, type = type.title))
//                }
//
//                BillStatusEnum.entries.forEach { status ->
//                    statusDao.insert(BillStatus(id = status.ordinal, status = status.title))
//                }
//            }
//        }
//    }
//}