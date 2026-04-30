package com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.model.ElectronicBill
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill.ElectronicBillsRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ElectronicBillsViewModel(
    private val electronicBillsRepository: ElectronicBillsRepository,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    val TAG = "ElectronicBillsViewModel"

    private val _uiState = MutableStateFlow(ElectronicBillsUiState())
    val uiState: StateFlow<ElectronicBillsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            load_conectivity()
            delay(1000)
            refreshElectronicBills()
        }
    }
    // REFRESCAMOS LAS ELECTRONICBILLS CUANDO CARGAMOS LAS CALLES
    fun load_conectivity() {
        viewModelScope.launch {
            connectivityRepository.isOnline.collect { status ->
                _uiState.update { it.copy(isOnline = status) }
            }
        }
    }

    fun updateElectronicBillEmail(
        email: String,
        type: BillTypeEnum,
        electronicBill: ElectronicBill
    ){
        viewModelScope.launch {
            val electronicBillAux  = if(type == BillTypeEnum.LUZ){
                    electronicBill.copy(electricityBillEmail = email)
                }else {
                    electronicBill.copy(gasBillEmail = email)
                }
            _uiState.update { currentState ->
                currentState.copy(
                    electronicBills = currentState.electronicBills.map {
                        if (it.id == electronicBillAux.id) {
                            electronicBillAux
                        } else {
                            it
                        }
                    }
                )
            }
            Log.d(TAG, "aux: $electronicBillAux, updateElectronicBillEmail: ${_uiState.value.electronicBills}")
            electronicBillsRepository.update(electronicBillAux)
        }
    }

    fun refreshElectronicBills() {
        val isOnline = _uiState.value.isOnline
        viewModelScope.launch {
            _uiState.update { it.copy(
                errorMessage = null
            )
            }

            launch {
                electronicBillsRepository.getAllElectronicBills().collect { electronicBills ->
                    _uiState.update {
                        it.copy(
                            electronicBills = electronicBills
                        )
                    }
                }
            }

            if (isOnline) {
                try {
                    electronicBillsRepository.refreshElectronicBillsOnline()
                } catch (e: Exception) {
                    Log.e(TAG, "Error al conectar con Mockoon: ${e.message}")
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
            }else {
                electronicBillsRepository.insertMockElectronicBillsFromAssets()
            }
            Log.d(TAG, "ELECBILL tras refreshElectronicBills -> ${uiState.value.electronicBills}")
        }
    }

    fun updateCounter() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    counter = currentState.counter - 1
                )
            }
        }
    }

    fun resetCounter() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    counter = 3
                )
            }
        }
    }
}