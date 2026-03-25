package com.iberdrola.practicas2026.alejandroLO.data.network.bill

import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import retrofit2.http.GET
import retrofit2.http.Path

interface BillsApiService {
    @GET("bills") // La ruta que configuraste en Mockoon
    suspend fun getBills(): List<Bill>
}