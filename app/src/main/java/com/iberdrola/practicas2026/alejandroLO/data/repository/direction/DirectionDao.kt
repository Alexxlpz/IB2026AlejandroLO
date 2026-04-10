package com.iberdrola.practicas2026.alejandroLO.data.repository.direction

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iberdrola.practicas2026.alejandroLO.data.model.Direction
import kotlinx.coroutines.flow.Flow

@Dao
interface DirectionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(direction: Direction)

    @Update
    suspend fun update(direction: Direction)

    @Delete
    suspend fun delete(direction: Direction)

    @Query("DELETE FROM direction")
    suspend fun deleteAll()

    @Query("DELETE FROM direction")
    fun deleteAllSync()

    @Query("SELECT * from direction WHERE id = :id")
    fun getDirection(id: Int): Flow<Direction>

    @Query("SELECT * from direction ORDER BY street ASC")
    fun getAllDirections(): Flow<List<Direction>>
}