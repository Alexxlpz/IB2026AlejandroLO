package com.iberdrola.practicas2026.alejandroLO.data.repository.Bill

import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import kotlinx.coroutines.flow.Flow

class OfflineBillsRepository(private val billDao: BillsDao) : BillsRepository {
    override fun getAllBills(): Flow<List<Bill>> = billDao.getAllBills()
    override fun getBillsByType(type: String): Flow<List<Bill>> = billDao.getBillsByType(type)
    override fun getBillById(id: Int): Flow<Bill> = billDao.getBill(id)
    override suspend fun insert(bill: Bill) = billDao.insert(bill)
    override suspend fun delete(bill: Bill) = billDao.delete(bill)
    override suspend fun update(bill: Bill) = billDao.update(bill)
}