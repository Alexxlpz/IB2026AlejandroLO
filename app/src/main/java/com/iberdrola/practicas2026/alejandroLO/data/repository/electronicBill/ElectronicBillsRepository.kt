package com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill

import com.iberdrola.practicas2026.alejandroLO.data.model.ElectronicBill
import kotlinx.coroutines.flow.Flow

interface ElectronicBillsRepository {
    suspend fun insert(electronicBill: ElectronicBill)
    suspend fun update(electronicBill: ElectronicBill)
    suspend fun delete(electronicBill: ElectronicBill)
    suspend fun deleteAll()
    suspend fun getElectronicBillById(id: Int): Flow<ElectronicBill>
    suspend fun getAllElectronicBills(): Flow<List<ElectronicBill>>

    suspend fun refreshElectronicBillsOnline()
    suspend fun insertMockElectronicBillsFromAssets()
}