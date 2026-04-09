package com.iberdrola.practicas2026.alejandroLO.data.repository.bill

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iberdrola.practicas2026.alejandroLO.data.BillDatabase
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.data.network.bill.BillsApiService
import com.iberdrola.practicas2026.alejandroLO.data.repository.direction.DirectionRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OfflineBillsRepository(
    private val billDao: BillsDao,
    private val apiService: BillsApiService,
    private val context: Context,
    private val gson: Gson,
    private val directionsRepository: DirectionRepository
) : BillsRepository {

    val TAG: String = "OfflineBillsRepository"

    override fun getAllBills(): Flow<List<Bill>> = billDao.getAllBills()
    override fun getAllBillsByDirectionId(directionId: Int): Flow<List<Bill>> = billDao.getAllBillsByDirectionId(directionId)
    override fun getBillsByType(typeId: Int): Flow<List<Bill>> = billDao.getBillsByType(typeId)
    override fun getBillById(id: Int): Flow<Bill> = billDao.getBill(id)
    override suspend fun insert(bill: Bill) = billDao.insert(bill)
    override suspend fun delete(bill: Bill) = billDao.delete(bill)
    override suspend fun update(bill: Bill) = billDao.update(bill)
    override suspend fun deleteAll() = billDao.deleteAll()
    override fun deleteAllSync() = billDao.deleteAllSync()


    override suspend fun refreshBillsOnline(): Unit = withContext(Dispatchers.IO) {
        try {
            val remoteBills = apiService.getBills()
            val database = BillDatabase.getDatabase(context)

            try {
                billDao.deleteAll()
                Log.d(TAG, "Insertando facturas desde API...")
                remoteBills.forEach { billDao.insert(it) }
            } catch (_: SQLiteConstraintException) {
                Log.w(TAG, "Fallo de ForeignKey online. Refrescando direcciones desde API...")
                directionsRepository.refreshDirectionsOnline()

                BillTypeEnum.entries.forEach { type ->
                    database.billTypeDao().insert(com.iberdrola.practicas2026.alejandroLO.data.model.BillType(type.ordinal, type.title))
                }
                BillStatusEnum.entries.forEach { status ->
                    database.billStatusDao().insert(com.iberdrola.practicas2026.alejandroLO.data.model.BillStatus(status.ordinal, status.title))
                }

                billDao.deleteAll()
                remoteBills.forEach { billDao.insert(it) }
                Log.d(TAG, "Reintento online exitoso")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error en refreshBillsOnline: ${e.message}")
        }
    }

    override suspend fun insertMockBillsFromAssets(): Unit = withContext(Dispatchers.IO) {
        val jsonString = context.assets.open("bills_mock.json").bufferedReader().use { it.readText() }
        val bills = JsonToBill(jsonString)
        val database = BillDatabase.getDatabase(context)

        try {
            billDao.deleteAll()
            Log.d(TAG, "Insertando facturas mock...")
            bills.forEach { billDao.insert(it) }
        } catch (_: SQLiteConstraintException) {
            Log.w(TAG, "Fallo de ForeignKey en mock. Cargando direcciones locales...")
            directionsRepository.insertMockDirectionsFromAssets()

            BillTypeEnum.entries.forEach { type ->
                database.billTypeDao().insert(com.iberdrola.practicas2026.alejandroLO.data.model.BillType(type.ordinal, type.title))
            }
            BillStatusEnum.entries.forEach { status ->
                database.billStatusDao().insert(com.iberdrola.practicas2026.alejandroLO.data.model.BillStatus(status.ordinal, status.title))
            }

            billDao.deleteAll()
            bills.forEach { billDao.insert(it) }
            Log.d(TAG, "Base de datos reparada y facturas insertadas.")
        }
    }

    /*
    Convierte un string json a una lista de bills, convirtiendo mediante un TypeConverter los
    timestamp en tipos date.
     */
    private fun JsonToBill(jsonString: String): List<Bill> {

        val listType = object : TypeToken<List<Bill>>() {}.type
        val bills: List<Bill> = gson.fromJson(jsonString, listType)

        return bills
    }
}