package com.iberdrola.practicas2026.alejandroLO.data.network.electronicBill

import com.iberdrola.practicas2026.alejandroLO.data.model.ElectronicBill
import retrofit2.http.GET

interface ElectronicBillApiService {
    @GET("electronicBills")
    suspend fun getElectronicBills(): List<ElectronicBill>
}