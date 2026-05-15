package com.iberdrola.practicas2026.alejandroLO.billsViewModel

import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeConnectivityRepository(
    initialOnline: Boolean = false
) : ConnectivityRepository {
    private val _isOnline = MutableStateFlow(initialOnline)
    override val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    override fun setOnlineMode(isOnline: Boolean) {
        _isOnline.value = isOnline
    }
}
