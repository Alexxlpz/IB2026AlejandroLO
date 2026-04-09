package com.iberdrola.practicas2026.alejandroLO.data.repository.bill

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import kotlinx.coroutines.flow.Flow

@Dao
interface BillsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bill: Bill)

    @Update
    suspend fun update(bill: Bill)

    @Delete
    suspend fun delete(bill: Bill)

    @Query("DELETE FROM bills")
    suspend fun deleteAll()

    @Query("DELETE FROM bills")
    fun deleteAllSync()


    @Query("SELECT * from bills WHERE id = :id")
    fun getBill(id: Int): Flow<Bill>

    @Query("SELECT * from bills WHERE typeId = :typeId ORDER BY date DESC")
    fun getBillsByType(typeId: Int): Flow<List<Bill>>

    @Query("SELECT * from bills ORDER BY date DESC")
    fun getAllBills(): Flow<List<Bill>>

    @Query("SELECT * from bills WHERE directionId = :directionId ORDER BY date DESC")
    fun getAllBillsByDirectionId(directionId: Int): Flow<List<Bill>>
}