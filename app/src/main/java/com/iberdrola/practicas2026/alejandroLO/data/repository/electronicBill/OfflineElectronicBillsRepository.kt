package com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
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

    override suspend fun insert(electronicBill: ElectronicBill) = electronicBillDao.insert(electronicBill)
    override suspend fun update(electronicBill: ElectronicBill) = electronicBillDao.update(electronicBill)
    override suspend fun delete(electronicBill: ElectronicBill) = electronicBillDao.delete(electronicBill)
    override suspend fun deleteAll() = electronicBillDao.deleteAll()
    override suspend fun getElectronicBillById(id: Int) = electronicBillDao.getElectronicBillById(id)
    override suspend fun getAllElectronicBills() = electronicBillDao.getAllElectronicBills()



    override suspend fun refreshElectronicBillsOnline(): Unit = withContext(Dispatchers.IO) {
        try {
            val remoteBills = apiService.getElectronicBills()

            try {
                electronicBillDao.deleteAll()
                remoteBills.forEach { electronicBillDao.insert(it) }
            } catch (_: SQLiteConstraintException) {
                directionsRepository.refreshDirectionsOnline()

                electronicBillDao.deleteAll()
                remoteBills.forEach { electronicBillDao.insert(it) }
            }

        } catch (_: Exception) {
            electronicBillDao.deleteAll()
            throw Exception("Error en refreshBillsOnline")
        }
    }

    override suspend fun insertMockElectronicBillsFromAssets(): Unit = withContext(Dispatchers.IO) {
        val jsonString = context.assets.open("electronicBills_mock.json").bufferedReader().use { it.readText() }
        val bills = JsonToElectronicBill(jsonString)

        try {
            electronicBillDao.deleteAll()
            bills?.forEach { electronicBillDao.insert(it) }
        } catch (_: SQLiteConstraintException) {
            directionsRepository.insertMockDirectionsFromAssets()

            electronicBillDao.deleteAll()
            bills?.forEach { electronicBillDao.insert(it) }
        }
    }

    private fun JsonToElectronicBill(jsonString: String): List<ElectronicBill>? {

        val listType = object : TypeToken<List<ElectronicBill>>() {}.type
        val electronicBill: List<ElectronicBill>? = gson.fromJson(jsonString, listType)

        return electronicBill
    }
}
