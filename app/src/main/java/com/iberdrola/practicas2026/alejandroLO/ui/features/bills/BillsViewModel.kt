package com.iberdrola.practicas2026.alejandroLO.ui.features.bills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.repository.Bill.BillsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BillsViewModel(
    private val billsRepository: BillsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BillsUiState())
    val uiState: StateFlow<BillsUiState> = _uiState.asStateFlow()

    init {
        refreshBills()
    }

    private fun refreshBills() {
        viewModelScope.launch {
            billsRepository.getBillsByType(_uiState.value.selectedOption.name).collect { bills ->
                _uiState.update { it.copy(billsList = bills) }
            }
        }
    }

    fun updateSelectedOption(option: BillType) {
        _uiState.update { it.copy(selectedOption = option) }
        refreshBills()
    }
}