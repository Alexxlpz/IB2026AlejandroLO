package com.iberdrola.practicas2026.alejandroLO.data.repository.Bill

import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import kotlinx.coroutines.flow.Flow

interface BillsRepository {
    suspend fun insert(bill: Bill) // AÑADIR UNA FACTURA
    suspend fun update(bill: Bill) // MODIFICAR UNA FACTURA
    suspend fun delete(bill: Bill) // BORRAR LA FACTURA PASADA POR PARAMETRO

    fun getBillById(id: Int): Flow<Bill> // OBTENER LA FACTURA CON EL ID DEL PARAMETRO ORDENADAS POR FECHA DE MANERA DESCENDIENTE
    fun getBillsByType(type: String): Flow<List<Bill>> // OBTENER LA FACTURA CON EL TIPO DEL PARAMETRO ORDENADAS POR FECHA DE MANERA DESCENDIENTE
    fun getAllBills(): Flow<List<Bill>> // OBTENER TODAS LAS FACTURAS ORDENADAS POR FECHA DE MANERA DESCENDIENTE
}