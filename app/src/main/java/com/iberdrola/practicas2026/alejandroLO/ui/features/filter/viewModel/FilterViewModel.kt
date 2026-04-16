package com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.repository.filter.FilterRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.enums.FilterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class FilterViewModel(
    private val filterRepository: FilterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    val TAG = "FilterViewModel"

    init {
        loadPriceRange()
    }

    fun loadPriceRange() {
        viewModelScope.launch {
            kotlinx.coroutines.flow.combine(
                filterRepository.minPrice,
                filterRepository.maxPrice
            ) { minPrice, maxPrice -> Pair(minPrice, maxPrice) }
                .filter { (minPrice, maxPrice) -> maxPrice > minPrice }
                // toma el primer valor válido y cancela la suscripción automáticamente
                .first()
                .let { (minPrice, maxPrice) ->

                _uiState.update { currentState ->
                    currentState.copy(
                        priceRange = minPrice..maxPrice,
                        minPrice = minPrice,
                        maxPrice = maxPrice
                    )
                }
            }
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
        filterRepository.setFilterCriteria(_uiState.value)
    }

    fun sumbmitButtom(
        dateFrom: Date?,
        dateTo: Date?,
        priceRange: ClosedFloatingPointRange<Float>,
        selectedStates: List<BillStatusEnum>
    ){
        _uiState.update {
            it.copy(
                selectedDateFrom = dateFrom,
                selectedDateTo = dateTo,
                priceRange = priceRange,
                selectedStates = selectedStates
            )
        }
        filterRepository.setFilterCriteria(_uiState.value)
    }

    fun onClearDate(dateField: Int) {
        when (dateField) {
            0 -> _uiState.update { it.copy(selectedDateFrom = null) }
            1 -> _uiState.update { it.copy(selectedDateTo = null) }
        }
        if(dateField == 0){
            Log.d(TAG, "onClearDate(0): ${_uiState.value.selectedDateFrom}")
        }else {
            Log.d(TAG, "onClearDate(1): ${_uiState.value.selectedDateTo}")
        }
    }

    fun onClearState(state: BillStatusEnum) {
        _uiState.update {
            it.copy(
                selectedStates = it.selectedStates - state
            )
        }
        Log.d(TAG, "onClearState: ${_uiState.value.selectedStates}")
    }

    fun onClearPriceRange() {
        _uiState.update {
            it.copy(
                priceRange = it.minPrice..it.maxPrice
            )
        }
        Log.d(TAG, "onClearPriceRange: ${_uiState.value.priceRange}")
    }

    fun clearFilterField(activeFilterItem: ActiveFilterItem){
        when(activeFilterItem.type){
            FilterType.DATE_FROM -> onClearDate(0)
            FilterType.DATE_TO -> onClearDate(1)
            FilterType.PRICE_RANGE -> onClearPriceRange()
            FilterType.STATUS -> onClearState(BillStatusEnum.entries.find{ it.title == activeFilterItem.label }!!)
        }
        filterRepository.setFilterCriteria(_uiState.value)
    }
}