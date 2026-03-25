package com.iberdrola.practicas2026.alejandroLO.data.repository.billType

import com.iberdrola.practicas2026.alejandroLO.data.model.BillType
import kotlinx.coroutines.flow.Flow

class OfflineBillTypeRepository(
    private val billTypeDao: BillTypeDao
): BillTypeRepository {
    override suspend fun insert(billType: BillType) = billTypeDao.insert(billType)

    override suspend fun update(billType: BillType) = billTypeDao.update(billType)

    override suspend fun delete(billType: BillType) = billTypeDao.delete(billType)

    override fun getBillType(id: Int): Flow<BillType> = billTypeDao.getBillType(id)

    override fun getAllBillTypes(): Flow<List<BillType>> = billTypeDao.getAllBillTypes()

}