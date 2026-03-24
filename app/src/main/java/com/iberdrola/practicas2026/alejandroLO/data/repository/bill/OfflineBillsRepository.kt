package com.iberdrola.practicas2026.alejandroLO.data.repository.bill

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.data.network.bill.BillsApiService
import kotlinx.coroutines.flow.Flow
import com.google.gson.JsonDeserializer
import java.util.Date

class OfflineBillsRepository(
    private val billDao: BillsDao,
    private val apiService: BillsApiService,
    private val context: Context
) : BillsRepository {

    override fun getAllBills(): Flow<List<Bill>> = billDao.getAllBills()
    override fun getBillsByType(type: String): Flow<List<Bill>> = billDao.getBillsByType(type)
    override fun getBillById(id: Int): Flow<Bill> = billDao.getBill(id)
    override suspend fun insert(bill: Bill) = billDao.insert(bill)
    override suspend fun delete(bill: Bill) = billDao.delete(bill)
    override suspend fun update(bill: Bill) = billDao.update(bill)
    override suspend fun deleteAll() { billDao.deleteAll() }

    override suspend fun refreshBillsOnline() {
        try {
            val remoteBills = apiService.getBills()
            // Podrías guardarlas en Room para tener caché offline
            billDao.deleteAll()
            remoteBills.forEach { billDao.insert(it) }
        } catch (e: Exception) {
            // Manejar error de conexión
        }
    }

    override suspend fun insertMockBillsFromAssets() {
        val jsonString = context.assets.open("bills_mock.json").bufferedReader().use { it.readText() }

        // Configuración especial para leer Timestamps (milisegundos) como Date
        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, JsonDeserializer { json, _, _ ->
                Date(json.asJsonPrimitive.asLong)
            })
            .create()

        val listType = object : TypeToken<List<Bill>>() {}.type
        val bills: List<Bill> = gson.fromJson(jsonString, listType)

        // Limpiamos e insertamos
        billDao.deleteAll()
        bills.forEach { billDao.insert(it) }
    }
}