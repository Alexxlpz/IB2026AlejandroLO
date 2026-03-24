package com.iberdrola.practicas2026.alejandroLO.ui.features.main

import android.util.Log
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.BillType
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// TODO: necesitamos actualizar las facturas al seleccionar en la main la opcion
// necesitamos que main contenga el BillViewmodel para actualizar???
class MainViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    val TAG: String = "MainViewModel"
    init {
        load_options()
    }

    fun load_options(){
        _uiState.update { it.copy(options = BillType.entries.toTypedArray().toList()) }
    }
    fun updateSelectedOption(option: BillType) {
        Log.d(TAG, "MAIN -> updateSelectedOption: $option")

        _uiState.update {
            it.copy(selectedOption = option)
        }
    }
}