package com.iberdrola.practicas2026.alejandroLO.data.repository.bill

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.data.network.bill.BillsApiService
import kotlinx.coroutines.flow.Flow
import com.google.gson.JsonDeserializer
import java.util.Date
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OfflineBillsRepository(
    private val billDao: BillsDao,
    private val apiService: BillsApiService,
    private val context: Context,
    private val gson: Gson
) : BillsRepository {

    val TAG: String = "OfflineBillsRepository"

    override fun getAllBills(): Flow<List<Bill>> = billDao.getAllBills()
    override fun getBillsByType(typeId: Int): Flow<List<Bill>> = billDao.getBillsByType(typeId)
    override fun getBillById(id: Int): Flow<Bill> = billDao.getBill(id)
    override suspend fun insert(bill: Bill) = billDao.insert(bill)
    override suspend fun delete(bill: Bill) = billDao.delete(bill)
    override suspend fun update(bill: Bill) = billDao.update(bill)
    override suspend fun deleteAll() { billDao.deleteAll() }

    override suspend fun refreshBillsOnline(): Unit = withContext(Dispatchers.IO) {
        try {
            val remoteBills = apiService.getBills()
            billDao.deleteAll()
            Log.d(TAG, "refreshBillsOnline: vamos a insertar los datos en la base de datos: $remoteBills")
            remoteBills.forEach { billDao.insert(it) }
            Log.d(TAG, "refreshBillsOnline: se han insertado los datos en la base de datos")
        } catch (e: Exception) {
            Log.e(TAG, "Error al conectar con Mockoon: ${e.message}, COMPRUEBA SI LE HAS DADO AL PLAY")
        }
    }

    override suspend fun insertMockBillsFromAssets(): Unit = withContext(Dispatchers.IO) {
        val jsonString = context.assets.open("bills_mock.json").bufferedReader().use { it.readText() }

        val bills = JsonToBill(jsonString)


        // Limpiamos e insertamos
        billDao.deleteAll()
        Log.d(TAG, "insertMockBillsFromAssets: vamos a insertar los datos en la base de datos: $bills")
        bills.forEach { billDao.insert(it) }
        Log.d(TAG, "insertMockBillsFromAssets: se han insertado los datos en la base de datos")
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