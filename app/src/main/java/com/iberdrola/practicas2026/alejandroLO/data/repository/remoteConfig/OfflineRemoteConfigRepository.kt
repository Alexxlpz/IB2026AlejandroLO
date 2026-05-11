package com.iberdrola.practicas2026.alejandroLO.data.repository.remoteConfig

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

class OfflineRemoteConfigRepository: RemoteConfigRepository {
    private val remoteConfig = Firebase.remoteConfig

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(
            mapOf("toggle_gas_contracts" to true)
        )
    }

    override suspend fun fetchAndActivate(): Boolean {
        return remoteConfig.fetchAndActivate().await()
    }

    override fun isGasContractsEnabled(): Boolean {
        return remoteConfig.getBoolean("toggle_gas_contracts")
    }
}