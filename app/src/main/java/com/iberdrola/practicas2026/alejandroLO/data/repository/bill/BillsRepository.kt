package com.iberdrola.practicas2026.alejandroLO.data.repository.bill

import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import kotlinx.coroutines.flow.Flow

interface BillsRepository {
    suspend fun insert(bill: Bill) 
    suspend fun update(bill: Bill)
    suspend fun delete(bill: Bill)
    suspend fun deleteAll()
    fun deleteAllSync()
    suspend fun refreshBillsOnline()
    suspend fun insertMockBillsFromAssets()
    fun getBillById(id: Int): Flow<Bill>
    fun getBillsByType(typeId: Int): Flow<List<Bill>>
    fun getAllBills(): Flow<List<Bill>>

    fun getAllBillsByDirectionId(directionId: Int): Flow<List<Bill>>

    fun getMaxPrice(): Float

    fun getMinPrice(): Float
}