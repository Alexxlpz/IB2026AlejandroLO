package com.iberdrola.practicas2026.alejandroLO.ui.features.main

import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.BillType
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        load_options()
    }

    fun load_options(){
        _uiState.update { it.copy(options = BillType.entries.toTypedArray().toList()) }
    }
}