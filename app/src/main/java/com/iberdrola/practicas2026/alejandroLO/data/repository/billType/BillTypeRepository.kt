package com.iberdrola.practicas2026.alejandroLO.data.repository.billType

import com.iberdrola.practicas2026.alejandroLO.data.model.BillType
import kotlinx.coroutines.flow.Flow

interface BillTypeRepository {
    suspend fun insert(billType: BillType)
    suspend fun update(billType: BillType)
    suspend fun delete(billType: BillType)
    fun getBillType(id: Int): Flow<BillType>
    fun getAllBillTypes(): Flow<List<BillType>>
}