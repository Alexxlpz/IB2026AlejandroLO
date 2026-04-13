package com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel

import androidx.lifecycle.ViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

class FilterViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()


    fun updatePriceRange(range: ClosedFloatingPointRange<Float>) {
        _uiState.update { 
            it.copy(priceRange = range)
        }
    }

    fun updateDateFrom(date: Date) {
        if(_uiState.value.selectedDateTo != null
            && _uiState.value.selectedDateTo!! < date){

            _uiState.update {
                it.copy(
                    selectedDateFrom = it.selectedDateTo,
                    selectedDateTo = date
                )
            }

        }else {
            _uiState.update { it.copy(selectedDateFrom = date) }
        }
    }

    fun updateDateTo(date: Date) {
        if(_uiState.value.selectedDateFrom != null
            && _uiState.value.selectedDateFrom!! > date){

            _uiState.update {
                it.copy(
                    selectedDateFrom = date,
                    selectedDateTo = it.selectedDateFrom
                )
            }

        }else {
            _uiState.update { it.copy(selectedDateTo = date) }
        }
    }

    fun clearFilters() {
        _uiState.value = FilterUiState()
    }

    fun addState(state: BillStatusEnum) {
        _uiState.update {
            it.copy(
                selectedStates = it.selectedStates + state
            )
        }
    }

    fun removeState(state: BillStatusEnum) {
        _uiState.update {
            it.copy(
                selectedStates = it.selectedStates - state
            )
        }
    }
}