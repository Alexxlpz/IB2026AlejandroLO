package com.iberdrola.practicas2026.alejandroLO.data

import android.content.Context
import com.iberdrola.practicas2026.alejandroLO.data.network.bill.BillsApiService
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.OfflineBillsRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val billsRepository: BillsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val baseUrl = "http://10.0.2.2:3000/" // url para conectarnos con mockoon
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
    private val retrofitService: BillsApiService by lazy {
        retrofit.create(BillsApiService::class.java)
    }

    override val billsRepository: BillsRepository by lazy {
        OfflineBillsRepository(
            billDao = BillDatabase.getDatabase(context).billDao(),
            apiService = retrofitService,
            context = context
        )
    }
//    override val billsRepository: BillsRepository by lazy {
//        OfflineBillsRepository(BillDatabase.getDatabase(context).billDao())
//    }
}