package com.iberdrola.practicas2026.alejandroLO.data.network.direction

import com.iberdrola.practicas2026.alejandroLO.data.model.Direction
import retrofit2.http.GET

interface DirectionApiService {
    @GET("directions")
    suspend fun getDirections(): List<Direction>
}