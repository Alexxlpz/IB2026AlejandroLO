package com.iberdrola.practicas2026.alejandroLO.data.repository.billType

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iberdrola.practicas2026.alejandroLO.data.model.BillType
import kotlinx.coroutines.flow.Flow

@Dao
interface BillTypeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(billType: BillType)
    @Update
    suspend fun update(billType: BillType)
    @Delete
    suspend fun delete(billType: BillType)
    @Query("SELECT * from billType WHERE id = :id")
    fun getBillType(id: Int): Flow<BillType>
    @Query("SELECT * from billType ORDER BY id ASC")
    fun getAllBillTypes(): Flow<List<BillType>>

}