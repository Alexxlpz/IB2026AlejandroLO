package com.iberdrola.practicas2026.alejandroLO.data

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.iberdrola.practicas2026.alejandroLO.BuildConfig
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.data.network.bill.BillsApiService
import com.iberdrola.practicas2026.alejandroLO.data.network.direction.DirectionApiService
import com.iberdrola.practicas2026.alejandroLO.data.network.electronicBill.ElectronicBillApiService
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.OfflineBillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.OfflineConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.direction.DirectionRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.direction.OfflineDirectionRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill.ElectronicBillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill.OfflineElectronicBillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.filter.FilterRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.filter.OfflineFilterRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.util.Date
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


interface AppContainer {
    val billsRepository: BillsRepository
    val directionsRepository: DirectionRepository
    val connectivityRepository: ConnectivityRepository

    val filterRepository: FilterRepository
    val electronicBillsRepository: ElectronicBillsRepository
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
        .baseUrl(baseUrl)
        .client(getUnsafeOkHttpClient(context))
        .addConverterFactory(GsonConverterFactory.create(gson)) // se lo pasamos para que lo use
        // para crear los objetos bill
        .build()

    private val billsRetrofitService: BillsApiService by lazy {
        retrofit.create(BillsApiService::class.java)
    }

    private val directionRetrofitService: DirectionApiService by lazy {
        retrofit.create(DirectionApiService::class.java)
    }

    private val electronicBillRetrofitService: ElectronicBillApiService by lazy {
        retrofit.create(ElectronicBillApiService::class.java)
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

    override val electronicBillsRepository: ElectronicBillsRepository by lazy {
        OfflineElectronicBillsRepository(
            electronicBillDao = BillDatabase.getDatabase(context).electronicBillDao(),
            apiService = electronicBillRetrofitService,
            context = context,
            gson = gson,
            directionsRepository = directionsRepository
        )
    }

    // patron de diseño "Shared Repository State"
    // patron usado para centralizar la variable isOnline
    override
    val connectivityRepository: ConnectivityRepository by lazy {
        OfflineConnectivityRepository()
    }

    override
    val filterRepository: FilterRepository by lazy {
        OfflineFilterRepository()
    }

    private fun getUnsafeOkHttpClient(context: Context): OkHttpClient {
        val cf = CertificateFactory.getInstance("X.509")
        val certInput =
            context.resources.openRawResource(R.raw.alejandro_cert)
        val certificate = cf.generateCertificate(certInput)
        certInput.close()

        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", certificate)

        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)

        return OkHttpClient.Builder()
            .sslSocketFactory(
                sslContext.socketFactory,
                tmf.trustManagers[0] as X509TrustManager
            )
            .hostnameVerifier { _, _ -> true }
            .build()
    }
}