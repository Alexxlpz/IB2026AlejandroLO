package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Math.random

class BillsViewModel(
    private val billsRepository: BillsRepository,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BillsUiState())
    val uiState: StateFlow<BillsUiState> = _uiState.asStateFlow()

    val TAG: String = "BillsViewModel"

    init {
        load_conectivity()
        load_options()
        refreshBills()
    }

    fun load_conectivity() {
        viewModelScope.launch {
            connectivityRepository.isOnline.collect { status ->
                _uiState.update { it.copy(isOnline = status) }
            }
        }
    }
    fun load_options(){
        _uiState.update { it.copy(options = BillTypeEnum.entries.toTypedArray().toList()) }
    }

    fun refreshBills() {
        val isOnline = _uiState.value.isOnline
        val directionId = _uiState.value.directionId
        viewModelScope.launch {
            _uiState.update { it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

            val observator = launch {// solo coger aquellas que coincidan con la calle
                billsRepository.getAllBillsByDirectionId(directionId).collect { bills ->
                    _uiState.update {
                        it.copy(
                            billsList = bills
                        )
                    }
                }
            }

            if (isOnline) {
                try {
                    billsRepository.refreshBillsOnline()
                    delay(1000) // debido a que se carga demasiado rapido y ves aparecer las bills mientras se cargan
                } catch (e: Exception) {
                    Log.e(TAG, "Error al conectar con Mockoon: ${e.message}")
                    _uiState.update { it.copy(errorMessage = "Error al conectar con Mockoon: ${e.message}") }
                }
            }else {
                billsRepository.insertMockBillsFromAssets()
                delay((1000 + (random() * 2000)).toLong()) // delay entre 1 y 3 seg
            }
            _uiState.update { it.copy(isLoading = false) }

            observator.cancel()
        }
    }

    fun updateSelectedOption(option: BillTypeEnum) {
        Log.d(TAG, "BILLS -> updateSelectedOption: $option")
        _uiState.update {
            it.copy(
                selectedOption = option
            )
        }
    }

    fun updateDataBase(isOnline: Boolean) {
        connectivityRepository.setOnlineMode(isOnline)
        refreshBills()
    }

    fun updateDirection(directionId: Int, directionStreet: String){
        _uiState.update {
            it.copy(
                directionId = directionId,
                directionStreet = directionStreet
            )
        }
    }
}