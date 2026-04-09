package com.iberdrola.practicas2026.alejandroLO.data.repository.direction

import android.content.Context
import android.util.Log
import com.iberdrola.practicas2026.alejandroLO.data.model.Direction
import com.iberdrola.practicas2026.alejandroLO.data.network.direction.DirectionApiService
import kotlinx.coroutines.flow.Flow
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.iberdrola.practicas2026.alejandroLO.data.BillDatabase
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import kotlinx.coroutines.withContext


class OfflineDirectionRepository(
    private val directionDao: DirectionDao,
    private val apiService: DirectionApiService,
    private val context: Context
) : DirectionRepository  {

    val TAG = "OfflineDirectionRepository"

    override suspend fun insert(direction: Direction) = directionDao.insert(direction)

    override suspend fun update(direction: Direction) = directionDao.update(direction)

    override suspend fun delete(direction: Direction) = directionDao.delete(direction)

    override suspend fun deleteAll(direction: Direction) = directionDao.delete(direction)

    override fun deleteAllSync() = directionDao.deleteAllSync()

    override fun getDirection(id: Int): Flow<Direction> = directionDao.getDirection(id)

    override fun getAllDirections(): Flow<List<Direction>> = directionDao.getAllDirections()

    override suspend fun refreshDirectionsOnline() {
        try {
            val remoteDir = apiService.getDirections()
            val database = BillDatabase.getDatabase(context)
            withContext(kotlinx.coroutines.Dispatchers.IO) {
                database.clearDatabase()
                Log.d(
                    TAG,
                    "refreshDirectionsOnline: vamos a insertar los datos en la base de datos: $remoteDir"
                )
                remoteDir.forEach { directionDao.insert(it) }
                Log.d(
                    TAG,
                    "refreshDirectionsOnline: se han insertado los datos en la base de datos"
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al conectar con Mockoon: ${e.message}, COMPRUEBA SI LE HAS DADO AL PLAY")
        }
    }

    override suspend fun insertMockDirectionsFromAssets() {
        val jsonString = context.assets.open("directions_mock.json").bufferedReader().use { it.readText() }
        val gson = GsonBuilder().create()
        val listType = object : TypeToken<List<Direction>>() {}.type
        val directions: List<Direction> = gson.fromJson(jsonString, listType)
        val database = BillDatabase.getDatabase(context)


        // Limpiamos e insertamos
        withContext(kotlinx.coroutines.Dispatchers.IO) {
            val database = BillDatabase.getDatabase(context)

            Log.d(TAG, "Iniciando clearDatabase desde Assets...")
            database.clearDatabase()

            Log.d(TAG, "insertMockDirectionsFromAssets: vamos a insertar los datos en la base de datos: $directions")
            directions.forEach { directionDao.insert(it) }
            Log.d(TAG, "insertMockDirectionsFromAssets: se han insertado los datos en la base de datos")
        }
    }
}