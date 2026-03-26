package com.iberdrola.practicas2026.alejandroLO.data.repository.billStatus

import com.iberdrola.practicas2026.alejandroLO.data.model.BillStatus
import kotlinx.coroutines.flow.Flow

interface BillStatusRepository {
    suspend fun insert(billStatus: BillStatus)
    suspend fun update(billStatus: BillStatus)
    suspend fun delete(billStatus: BillStatus)
    fun getBillStatus(id: Int): Flow<BillStatus>
    fun getAllBillStatus(): Flow<List<BillStatus>>
}