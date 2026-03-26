package com.iberdrola.practicas2026.alejandroLO.data

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.iberdrola.practicas2026.alejandroLO.data.network.bill.BillsApiService
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.OfflineBillsRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.iberdrola.practicas2026.alejandroLO.BuildConfig
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.Date


interface AppContainer {
    val billsRepository: BillsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val baseUrl = BuildConfig.MOCKOON_URL // url para conectarnos con mockoon
//    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)


    // convierte el json a bill aplicando las operaciones necesarias sobre el timestamp para convertirlo en date
    private val gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, JsonDeserializer { json, _, _ ->
            Date(json.asJsonPrimitive.asLong)
        })
        .create()
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson)) // se lo pasamos para que lo use
        .baseUrl(baseUrl)                                       // para crear los objetos bill
        .build()

    private val retrofitService: BillsApiService by lazy {
        retrofit.create(BillsApiService::class.java)
    }

    override val billsRepository: BillsRepository by lazy {
        OfflineBillsRepository(
            billDao = BillDatabase.getDatabase(context).billDao(),
            apiService = retrofitService,
            context = context,
            gson = gson // se lo pasamos para que no lo tenga que crear otra vez si carga los datos
                        // localmente
        )
    }
}