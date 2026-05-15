package com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OfflineConnectivityRepository : ConnectivityRepository {
    private val _isOnline = MutableStateFlow(false)
    override val isOnline = _isOnline.asStateFlow()

    override fun setOnlineMode(isOnline: Boolean) {
        _isOnline.value = isOnline
    }
}