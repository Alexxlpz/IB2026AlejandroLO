package com.iberdrola.practicas2026.alejandroLO.billsViewModel

import com.iberdrola.practicas2026.alejandroLO.data.repository.remoteConfig.RemoteConfigRepository

class FakeRemoteConfigRepository(
    private val gasEnabled: Boolean = true
) : RemoteConfigRepository {
    override suspend fun fetchAndActivate(): Boolean = true
    override fun isGasContractsEnabled(): Boolean = gasEnabled
}
