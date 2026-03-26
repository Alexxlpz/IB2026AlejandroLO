package com.iberdrola.practicas2026.alejandroLO.data.repository.billStatus

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iberdrola.practicas2026.alejandroLO.data.model.BillStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface BillStatusDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(billStatus: BillStatus)

    @Update
    suspend fun update(billStatus: BillStatus)

    @Delete
    suspend fun delete(billStatus: BillStatus)

    @Query("SELECT * from billStatus WHERE id = :id")
    fun getBillStatus(id: Int): Flow<BillStatus>

    @Query("SELECT * from billStatus ORDER BY id ASC")
    fun getAllBillStatus(): Flow<List<BillStatus>>
}