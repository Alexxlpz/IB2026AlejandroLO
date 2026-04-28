package com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill

import com.iberdrola.practicas2026.alejandroLO.data.model.ElectronicBill

interface ElectronicBillsRepository {
    suspend fun insert(electronicBill: ElectronicBill)
    suspend fun update(electronicBill: ElectronicBill)
    suspend fun delete(electronicBill: ElectronicBill)
    suspend fun deleteAll()
    suspend fun getElectronicBillById(id: Int): ElectronicBill?
    suspend fun getAllElectronicBills(): List<ElectronicBill>

    suspend fun refreshBillsOnline()
    suspend fun insertMockBillsFromAssets()
}