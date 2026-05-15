package com.iberdrola.practicas2026.alejandroLO.data

import android.content.Context
import android.os.Build
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.data.network.bill.BillsApiService
import com.iberdrola.practicas2026.alejandroLO.data.network.direction.DirectionApiService
import com.iberdrola.practicas2026.alejandroLO.data.network.electronicBill.ElectronicBillApiService
import com.iberdrola.practicas2026.alejandroLO.data.repository.analyticsRepository.AnalyticsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.analyticsRepository.OfflineAnalyticsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.OfflineBillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.OfflineConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.direction.DirectionRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.direction.OfflineDirectionRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill.ElectronicBillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill.OfflineElectronicBillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.remoteConfig.OfflineRemoteConfigRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.remoteConfig.RemoteConfigRepository
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
    val electronicBillsRepository: ElectronicBillsRepository
    val remoteConfigRepository: RemoteConfigRepository
    val analyticsRepository: AnalyticsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    // %LOCALAPPDATA%\Android\Sdk\platform-tools\adb reverse tcp:3000 tcp:3000
    private val deviceUrl = "https://localhost:3000/"
    private val emulatorUrl = "https://10.0.2.2:3000/"


    private val gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, JsonDeserializer { json, _, _ ->
            Date(json.asJsonPrimitive.asLong)
        })
        .setLenient()
        .create()

    val usedUrl = if(isEmulator()) {
        emulatorUrl
    } else {
        deviceUrl
    }
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(usedUrl)
        .client(getUnsafeOkHttpClient(context))
        .addConverterFactory(GsonConverterFactory.create(gson))
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
            gson = gson,
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

    override val connectivityRepository: ConnectivityRepository by lazy {
        OfflineConnectivityRepository()
    }

    override val remoteConfigRepository: RemoteConfigRepository by lazy {
        OfflineRemoteConfigRepository()
    }

    override val analyticsRepository: AnalyticsRepository by lazy {
        OfflineAnalyticsRepository(context)
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

    fun isEmulator(): Boolean {
        val result = (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || Build.PRODUCT == "google_sdk"
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("emulator"))

        return result
    }
}