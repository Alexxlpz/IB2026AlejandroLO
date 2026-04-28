package com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.iberdrola.practicas2026.alejandroLO.data.model.ElectronicBill

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
    suspend fun getElectronicBillById(id: Int): ElectronicBill?

    @Query("SELECT * FROM electronicBills")
    suspend fun getAllElectronicBills(): List<ElectronicBill>
}