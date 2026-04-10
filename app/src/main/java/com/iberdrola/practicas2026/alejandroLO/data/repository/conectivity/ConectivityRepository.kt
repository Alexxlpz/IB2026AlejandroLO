package com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityRepository {
    val isOnline: StateFlow<Boolean>
    fun setOnlineMode(isOnline: Boolean)
}
