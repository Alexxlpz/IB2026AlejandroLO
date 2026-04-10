package com.iberdrola.practicas2026.alejandroLO.fakes

import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeConnectivityRepository : ConnectivityRepository {
    private val _isOnline = MutableStateFlow(false)
    override val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    override fun setOnlineMode(isOnline: Boolean) {
        _isOnline.value = isOnline
    }
}