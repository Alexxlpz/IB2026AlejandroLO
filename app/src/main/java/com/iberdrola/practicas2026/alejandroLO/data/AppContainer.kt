package com.iberdrola.practicas2026.alejandroLO.data

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.iberdrola.practicas2026.alejandroLO.BuildConfig
import com.iberdrola.practicas2026.alejandroLO.data.network.bill.BillsApiService
import com.iberdrola.practicas2026.alejandroLO.data.network.direction.DirectionApiService
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.OfflineBillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.OfflineConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.direction.DirectionRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.direction.OfflineDirectionRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date


interface AppContainer {
    val billsRepository: BillsRepository
    val directionsRepository: DirectionRepository
    val connectivityRepository: ConnectivityRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val baseUrl = BuildConfig.MOCKOON_URL // url para conectarnos con mockoon
//    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)


    // convierte el json a bill aplicando las operaciones necesarias sobre el timestamp para convertirlo en date
    private val gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, JsonDeserializer { json, _, _ ->
            Date(json.asJsonPrimitive.asLong)
        })
        .setLenient() // para que Gson sea mas tolerable con el json
        .create()
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson)) // se lo pasamos para que lo use
        .baseUrl(baseUrl)                                       // para crear los objetos bill
        .build()

    private val billsRetrofitService: BillsApiService by lazy {
        retrofit.create(BillsApiService::class.java)
    }

    private val directionRetrofitService: DirectionApiService by lazy {
        retrofit.create(DirectionApiService::class.java)
    }

    override val billsRepository: BillsRepository by lazy {
        OfflineBillsRepository(
            billDao = BillDatabase.getDatabase(context).billDao(),
            apiService = billsRetrofitService,
            context = context,
            gson = gson, // se lo pasamos para que no lo tenga que crear otra vez si carga los datos
                        // localmente
            directionsRepository = directionsRepository
        )
    }

    override val directionsRepository: DirectionRepository by lazy {
        OfflineDirectionRepository(
            directionDao = BillDatabase.getDatabase(context).directionDao(),
            apiService = directionRetrofitService,
            context = context
        )
    }

    // patron de diseño "Shared Repository State"
    // patron usado para centralizar la variable isOnline
    override val connectivityRepository: ConnectivityRepository by lazy {
        OfflineConnectivityRepository()
    }
}