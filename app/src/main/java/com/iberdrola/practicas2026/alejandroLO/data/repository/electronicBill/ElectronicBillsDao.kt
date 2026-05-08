package com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.iberdrola.practicas2026.alejandroLO.data.model.ElectronicBill
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectronicBillsDao {
    @Insert
    suspend fun insert(electronicBill: ElectronicBill)
    @Update
    suspend fun update(electronicBill: ElectronicBill)
    @Delete
    suspend fun delete(electronicBill: ElectronicBill)

    @Query("DELETE FROM electronicBills")
    suspend fun deleteAll()

    @Query("SELECT * FROM electronicBills WHERE id = :id")
    fun getElectronicBillById(id: Int): Flow<ElectronicBill>

    @Query("SELECT * FROM electronicBills")
    fun getAllElectronicBills(): Flow<List<ElectronicBill>>
}