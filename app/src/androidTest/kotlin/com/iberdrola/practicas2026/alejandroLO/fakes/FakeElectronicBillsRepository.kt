package com.iberdrola.practicas2026.alejandroLO.fakes

import com.iberdrola.practicas2026.alejandroLO.data.model.ElectronicBill
import com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill.ElectronicBillsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeElectronicBillsRepository : ElectronicBillsRepository {
    override suspend fun insert(electronicBill: ElectronicBill) = Unit
    override suspend fun update(electronicBill: ElectronicBill) = Unit
    override suspend fun delete(electronicBill: ElectronicBill) = Unit
    override suspend fun deleteAll() = Unit
    override suspend fun getElectronicBillById(id: Int): Flow<ElectronicBill> = flowOf()
    override suspend fun getAllElectronicBills(): Flow<List<ElectronicBill>> = flowOf(emptyList())
    override suspend fun refreshElectronicBillsOnline() = Unit
    override suspend fun insertMockElectronicBillsFromAssets() = Unit
}
