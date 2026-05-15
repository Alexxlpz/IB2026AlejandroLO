package com.iberdrola.practicas2026.alejandroLO.data.repository.remoteConfig

interface RemoteConfigRepository {
    suspend fun fetchAndActivate(): Boolean
    fun isGasContractsEnabled(): Boolean
}