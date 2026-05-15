package com.iberdrola.practicas2026.alejandroLO.data.repository.direction

import com.iberdrola.practicas2026.alejandroLO.data.model.Direction
import kotlinx.coroutines.flow.Flow

interface DirectionRepository {
    suspend fun insert(direction: Direction)
    suspend fun update(direction: Direction)
    suspend fun delete(direction: Direction)
    suspend fun deleteAll(direction: Direction)
    fun deleteAllSync()
    fun getDirection(id: Int): Flow<Direction>
    fun getAllDirections(): Flow<List<Direction>>

    suspend fun refreshDirectionsOnline()
    suspend fun insertMockDirectionsFromAssets()

}