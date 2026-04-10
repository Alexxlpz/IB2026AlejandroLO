package com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
// patron de diseño "Shared Repository State"
// patron usado para centralizar la variable isOnline
class OfflineConnectivityRepository : ConnectivityRepository {
    private val _isOnline = MutableStateFlow(false)
    override val isOnline = _isOnline.asStateFlow()

    override fun setOnlineMode(isOnline: Boolean) {
        _isOnline.value = isOnline
    }
}