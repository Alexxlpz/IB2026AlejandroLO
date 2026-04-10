package com.iberdrola.practicas2026.alejandroLO.homeViewModel

import com.iberdrola.practicas2026.alejandroLO.data.model.Direction
import com.iberdrola.practicas2026.alejandroLO.data.repository.direction.DirectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeDirectionRepository : DirectionRepository {
    private val directionsFlow = MutableStateFlow<List<Direction>>(emptyList())

    suspend fun emit(directions: List<Direction>) {
        directionsFlow.emit(directions)
    }

    override fun getAllDirections(): Flow<List<Direction>> = directionsFlow
    override fun getDirection(id: Int): Flow<Direction> = TODO()
    override suspend fun insert(direction: Direction) { }
    override suspend fun update(direction: Direction) { }
    override suspend fun delete(direction: Direction) { }
    override suspend fun deleteAll(direction: Direction) { }
    override fun deleteAllSync() { }
    override suspend fun refreshDirectionsOnline() { }
    override suspend fun insertMockDirectionsFromAssets() { }
}