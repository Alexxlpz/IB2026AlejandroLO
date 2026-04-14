package com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.repository.filter.FilterRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class FilterViewModel(
    private val filterRepository: FilterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    init {
        loadPriceRange()
    }

    fun loadPriceRange() {
        viewModelScope.launch {
            kotlinx.coroutines.flow.combine(
                filterRepository.minPrice,
                filterRepository.maxPrice
            ) { minPrice, maxPrice ->

                _uiState.update { currentState ->
                    currentState.copy(
                        priceRange = minPrice..maxPrice,
                        minPrice = minPrice,
                        maxPrice = maxPrice
                    )
                }
            }.collect()
        }
    }

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
        val maxPrice = filterRepository.maxPrice.value
        val minPrice = filterRepository.minPrice.value
        _uiState.value = FilterUiState(
            priceRange = minPrice..maxPrice,
            maxPrice = maxPrice,
            minPrice = minPrice
        )
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

    fun sumbmitButtom(){
        filterRepository.setFilterCriteria(_uiState.value)
    }
}