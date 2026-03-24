package com.iberdrola.practicas2026.alejandroLO.ui.features.bills

import android.util.Log
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Math.random

class BillsViewModel(
    private val billsRepository: BillsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BillsUiState())
    val uiState: StateFlow<BillsUiState> = _uiState.asStateFlow()

    val TAG: String = "BillsViewModel"

    init {
        refreshBills()
    }

    private fun refreshBills() {
        val isLocal = _uiState.value.isOnline
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            if (isLocal) {
                billsRepository.insertMockBillsFromAssets()
                delay((1000 + (random() * 2000)).toLong()) // delay entre 1 y 3 seg
            }else {
                try {
                    billsRepository.refreshBillsOnline()
                } catch (e: Exception) {
                    Log.e(TAG, "Error al conectar con Mockoon: ${e.message}")
                }
            }

            billsRepository.getBillsByType(_uiState.value.selectedOption.name).collect { bills ->
                _uiState.update {
                    it.copy(billsList = bills,
                        lastBill = bills.lastOrNull(),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateSelectedOption(option: BillType) {
        Log.d(TAG, "BILLS -> updateSelectedOption: $option")
        _uiState.update {
            it.copy(
                selectedOption = option,
                billsList = emptyList()
            )
        }
        refreshBills()
    }

    fun updateDataBase(isOnline: Boolean) {
        _uiState.update { it.copy(isOnline = isOnline) }
        refreshBills()
    }
}