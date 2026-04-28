package com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iberdrola.practicas2026.alejandroLO.data.model.ElectronicBill
import com.iberdrola.practicas2026.alejandroLO.data.network.electronicBill.ElectronicBillApiService
import com.iberdrola.practicas2026.alejandroLO.data.repository.direction.DirectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OfflineElectronicBillsRepository(
    private val electronicBillDao: ElectronicBillsDao,
    private val apiService: ElectronicBillApiService,
    private val context: Context,
    private val gson: Gson,
    private val directionsRepository: DirectionRepository
) : ElectronicBillsRepository {

    val TAG: String = "OfflineElectronicBillsRepository"

    override suspend fun insert(electronicBill: ElectronicBill) = electronicBillDao.insert(electronicBill)
    override suspend fun update(electronicBill: ElectronicBill) = electronicBillDao.update(electronicBill)
    override suspend fun delete(electronicBill: ElectronicBill) = electronicBillDao.delete(electronicBill)
    override suspend fun deleteAll() = electronicBillDao.deleteAll()
    override suspend fun getElectronicBillById(id: Int) = electronicBillDao.getElectronicBillById(id)
    override suspend fun getAllElectronicBills() = electronicBillDao.getAllElectronicBills()



    override suspend fun refreshBillsOnline(): Unit = withContext(Dispatchers.IO) {
        try {
            val remoteBills = apiService.getElectronicBills()

            try {
                electronicBillDao.deleteAll()
                Log.d(TAG, "Insertando facturas electronicas desde API...")
                remoteBills.forEach { electronicBillDao.insert(it) }
            } catch (_: SQLiteConstraintException) {
                Log.w(TAG, "Fallo de ForeignKey online. Refrescando direcciones desde API...")
                directionsRepository.refreshDirectionsOnline()

                electronicBillDao.deleteAll()
                remoteBills.forEach { electronicBillDao.insert(it) }
                Log.d(TAG, "Reintento online exitoso")
            }

        } catch (e: Exception) {
            electronicBillDao.deleteAll()
            Log.e(TAG, "Error en refreshBillsOnline: ${e.message}")
            throw e
        }
    }

    override suspend fun insertMockBillsFromAssets(): Unit = withContext(Dispatchers.IO) {
        val jsonString = context.assets.open("electronicBills_mock.json").bufferedReader().use { it.readText() }
        val bills = JsonToElectronicBill(jsonString)

        try {
            electronicBillDao.deleteAll()
            Log.d(TAG, "Insertando facturas mock...")
            bills?.forEach { electronicBillDao.insert(it) }
        } catch (_: SQLiteConstraintException) {
            Log.w(TAG, "Fallo de ForeignKey en mock. Cargando direcciones locales...")
            directionsRepository.insertMockDirectionsFromAssets()

            electronicBillDao.deleteAll()
            bills?.forEach { electronicBillDao.insert(it) }
            Log.d(TAG, "Base de datos reparada y facturas insertadas.")
        }
    }

    /*
    Convierte un string json a una lista de bills, convirtiendo mediante un TypeConverter los
    timestamp en tipos date.
     */
    private fun JsonToElectronicBill(jsonString: String): List<ElectronicBill>? {

        val listType = object : TypeToken<List<ElectronicBill>>() {}.type
        val electronicBill: List<ElectronicBill>? = gson.fromJson(jsonString, listType)

        return electronicBill
    }
}
