package com.iberdrola.practicas2026.alejandroLO.data.repository.direction

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.iberdrola.practicas2026.alejandroLO.data.BillDatabase
import com.iberdrola.practicas2026.alejandroLO.data.model.Direction
import com.iberdrola.practicas2026.alejandroLO.data.network.direction.DirectionApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class OfflineDirectionRepository(
    private val directionDao: DirectionDao,
    private val apiService: DirectionApiService,
    private val context: Context
) : DirectionRepository  {

    override suspend fun insert(direction: Direction) = directionDao.insert(direction)

    override suspend fun update(direction: Direction) = directionDao.update(direction)

    override suspend fun delete(direction: Direction) = directionDao.delete(direction)

    override suspend fun deleteAll(direction: Direction) = directionDao.delete(direction)

    override fun deleteAllSync() = directionDao.deleteAllSync()

    override fun getDirection(id: Int): Flow<Direction> = directionDao.getDirection(id)

    override fun getAllDirections(): Flow<List<Direction>> = directionDao.getAllDirections()

    override suspend fun refreshDirectionsOnline() {
        val database = BillDatabase.getDatabase(context)
        try {
            val remoteDir = apiService.getDirections()
            withContext(Dispatchers.IO) {
                database.clearDatabase()
                remoteDir.forEach { directionDao.insert(it) }
            }
        } catch (e: Exception) {
            database.clearDatabase()
            throw e
        }
    }

    override suspend fun insertMockDirectionsFromAssets() {
        val jsonString = context.assets.open("directions_mock.json").bufferedReader().use { it.readText() }
        val gson = GsonBuilder().create()
        val listType = object : TypeToken<List<Direction>>() {}.type
        val directions: List<Direction> = gson.fromJson(jsonString, listType)

        withContext(Dispatchers.IO) {
            val database = BillDatabase.getDatabase(context)
            database.clearDatabase()
            directions.forEach { directionDao.insert(it) }
        }
    }
}