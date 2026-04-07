package com.iberdrola.practicas2026.alejandroLO

import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeBillsRepository : BillsRepository {

    private val billsFlow = MutableStateFlow<List<Bill>>(kotlin.collections.emptyList())

    suspend fun emit(bills: List<Bill>) {
        billsFlow.emit(bills)
    }

    override fun getAllBills(): Flow<List<Bill>> = billsFlow

    // Implementa el resto de métodos de la interfaz con "TODO()" o vacíos
    override fun getBillById(id: Int): Flow<Bill> = billsFlow.map { it.first() }
    override fun getBillsByType(typeId: Int): Flow<List<Bill>> {
        return billsFlow.map { list -> list.filter { it.typeId == typeId } }
    }

    override suspend fun insert(bill: Bill) {
        val newList = billsFlow.value.toMutableList()
        newList.add(bill)
        billsFlow.emit(newList)
    }
    override suspend fun delete(bill: Bill) {
        val newList = billsFlow.value.toMutableList()
        newList.remove(bill)
        billsFlow.emit(newList)
    }
    override suspend fun update(bill: Bill) {
        val newList = billsFlow.value.toMutableList()
        newList.removeIf { it.id == bill.id }
        newList.add(bill)
        billsFlow.emit(newList)
    }
    override suspend fun deleteAll() {
        billsFlow.emit(emptyList())
    }
    override suspend fun refreshBillsOnline() { }
    override suspend fun insertMockBillsFromAssets() { }
}