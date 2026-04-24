package com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.repository.filter.FilterRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.enums.FilterType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
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
            combine(
                filterRepository.minPrice,
                filterRepository.maxPrice
            ) { min, max -> min to max }
                .filter { (min, max) -> max >= min }
                .collect { (minRepo, maxRepo) ->
                    _uiState.update { currentState ->
                        val wasAtLimits = currentState.priceRange.start == currentState.minPrice &&
                                currentState.priceRange.endInclusive == currentState.maxPrice

                        val isFirstLoad = currentState.minPrice == Float.MIN_VALUE &&
                                currentState.maxPrice == Float.MAX_VALUE

                        val maxAux = if (minRepo == maxRepo) minRepo + 1 else maxRepo

                        val newRange = if (wasAtLimits || isFirstLoad) {
                            minRepo..maxAux
                        } else {
                            currentState.priceRange.start.coerceIn(minRepo, maxAux)..
                                    currentState.priceRange.endInclusive.coerceIn(minRepo, maxAux)
                        }

                        delay(500) // para que no se vean los cambios al darle al boton submit
                        currentState.copy(
                            minPrice = minRepo,
                            maxPrice = maxAux,
                            priceRange = newRange
                        )
                    }
                }
        }
    }



    fun clearFilters() {
        val maxPrice = _uiState.value.maxPrice
        val minPrice = _uiState.value.minPrice

        _uiState.update {
            it.copy(
                selectedDateFrom = null,
                selectedDateTo = null,
                priceRange = minPrice..maxPrice,
                maxPrice = maxPrice,
                minPrice = minPrice,
                selectedStates = BillStatusEnum.entries
            )
        }
        filterRepository.setFilterCriteria(_uiState.value)
    }

    fun clearFiltersToChangeMode() {
        val maxPrice = Float.MAX_VALUE
        val minPrice = Float.MIN_VALUE

        _uiState.update {
            it.copy(
                selectedDateFrom = null,
                selectedDateTo = null,
                priceRange = minPrice..maxPrice,
                maxPrice = maxPrice,
                minPrice = minPrice,
                selectedStates = BillStatusEnum.entries
            )
        }
    }

    fun sumbmitButtom(
        dateFrom: Date?,
        dateTo: Date?,
        priceRange: ClosedFloatingPointRange<Float>,
        selectedStates: List<BillStatusEnum>
    ){
        var selectedStatesAux = selectedStates
        if(selectedStates.isEmpty()){ // si esta vacio estamos filtrando por todos
            selectedStatesAux = BillStatusEnum.entries
        }

        _uiState.update {
            it.copy(
                selectedDateFrom = dateFrom,
                selectedDateTo = dateTo,
                priceRange = priceRange,
                selectedStates = selectedStatesAux
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
        var futureState = _uiState.value.selectedStates - state

        if(_uiState.value.selectedStates.size == 1
            && _uiState.value.selectedStates.contains(state)){
            futureState = BillStatusEnum.entries
        }

        _uiState.update {
            it.copy(
                selectedStates = futureState
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