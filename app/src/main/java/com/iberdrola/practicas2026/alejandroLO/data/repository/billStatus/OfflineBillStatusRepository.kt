package com.iberdrola.practicas2026.alejandroLO.data.repository.billStatus

import com.iberdrola.practicas2026.alejandroLO.data.model.BillStatus
import kotlinx.coroutines.flow.Flow

class OfflineBillStatusRepository(
    private val billStatusDao: BillStatusDao
): BillStatusRepository {
    override suspend fun insert(billStatus: BillStatus) = billStatusDao.insert(billStatus)

    override suspend fun update(billStatus: BillStatus) = billStatusDao.update(billStatus)

    override suspend fun delete(billStatus: BillStatus) = billStatusDao.delete(billStatus)

    override fun getBillStatus(id: Int): Flow<BillStatus> = billStatusDao.getBillStatus(id)

    override fun getAllBillStatus(): Flow<List<BillStatus>> = billStatusDao.getAllBillStatus()
}