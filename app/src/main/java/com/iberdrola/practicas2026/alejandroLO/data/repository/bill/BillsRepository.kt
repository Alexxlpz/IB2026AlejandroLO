package com.iberdrola.practicas2026.alejandroLO.data.repository.bill

import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import kotlinx.coroutines.flow.Flow

interface BillsRepository {
    suspend fun insert(bill: Bill) // AÑADIR UNA FACTURA
    suspend fun update(bill: Bill) // MODIFICAR UNA FACTURA
    suspend fun delete(bill: Bill) // BORRAR LA FACTURA PASADA POR PARAMETRO
    suspend fun deleteAll() // Borra todox de la base de datos
    fun deleteAllSync() // borra todxo de forma sincrona
    suspend fun refreshBillsOnline() // Mete los datos de la API en la base de datos
    suspend fun insertMockBillsFromAssets() // Mete los datos de un json que tenemos en local
    fun getBillById(id: Int): Flow<Bill> // OBTENER LA FACTURA CON EL ID DEL PARAMETRO ORDENADAS POR FECHA DE MANERA DESCENDIENTE
    fun getBillsByType(typeId: Int): Flow<List<Bill>> // OBTENER LA FACTURA CON EL TIPO DEL PARAMETRO ORDENADAS POR FECHA DE MANERA DESCENDIENTE
    fun getAllBills(): Flow<List<Bill>> // OBTENER TODAS LAS FACTURAS ORDENADAS POR FECHA DE MANERA DESCENDIENTE

    fun getAllBillsByDirectionId(directionId: Int): Flow<List<Bill>> // OBTENER TODAS LAS FACTURAS ORDENADAS POR FECHA DE MANERA DESCENDIENTE

    fun getMaxPrice(): Float // devuelve el precio mas alto para hacer el slider de filtrado

    fun getMinPrice(): Float // devuelve el precio mas bajo para hacer el slider de filtrado
}