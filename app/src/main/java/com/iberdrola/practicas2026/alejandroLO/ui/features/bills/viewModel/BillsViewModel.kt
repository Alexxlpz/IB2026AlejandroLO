package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.enums.FilterType
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.viewModel.ActiveFilterItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Math.random
import java.util.Date
import kotlin.math.ceil
import kotlin.math.floor
import kotlinx.coroutines.flow.debounce

class BillsViewModel(
    private val billsRepository: BillsRepository,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    private val _billsUiState = MutableStateFlow(BillsUiState())
    val billsUiState: StateFlow<BillsUiState> = _billsUiState.asStateFlow()

    private val _filterUiState = MutableStateFlow(FilterUiState())
    val filterUiState: StateFlow<FilterUiState> = _filterUiState.asStateFlow()
    private var billsJob: Job? = null
    val TAG: String = "BillsViewModel"

    init {
        load_conectivity()
        load_options()
        refreshBills()
    }


    fun load_conectivity() {
        viewModelScope.launch {
            connectivityRepository.isOnline.collect { status ->
                _billsUiState.update { it.copy(isOnline = status) }
            }
        }
    }
    fun load_options(){
        _billsUiState.update { it.copy(options = BillTypeEnum.entries.toTypedArray().toList()) }
    }

    @OptIn(FlowPreview::class)
    fun refreshBills() {
        billsJob?.cancel()
        val isOnline = _billsUiState.value.isOnline
        val directionId = _billsUiState.value.directionId
        viewModelScope.launch {
            _billsUiState.update { it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

            billsJob = launch {// solo coger aquellas que coincidan con la calle
                billsRepository.getAllBillsByDirectionId(directionId)
                    .debounce(600)
                    .collect { bills ->
                    _billsUiState.update {
                        it.copy(
                            billsList = bills
                        )
                    }
                    if (bills.isNotEmpty()) {
                        withContext(Dispatchers.Default) {

                            // para redondear hacia arriba
                            val maxPrice = ceil(bills.maxOf { it.price }).toFloat()
                            // para redondear hacia abajo
                            val minPrice = floor(bills.minOf { it.price }).toFloat()

                            val maxDate = bills.maxOf { it.emissionDate }
                            val minDate = bills.minOf { it.emissionDate }

                            setDateLimits(minDate, maxDate)
                            setPriceLimits(minPrice, maxPrice)
                            filterCriteriaApply()
                        }
                    }
                }
            }

            if (isOnline) {
                try {
                    Log.d(TAG, "BILLS -> refreshBills form street: "+_billsUiState.value.directionStreet)
                    billsRepository.refreshBillsOnline()
                    // ahora el delay lo simulamos desde mockoon
                    delay(500) // debido a que se carga demasiado rapido y ves aparecer las bills mientras se cargan
                } catch (e: Exception) {
                    Log.e(TAG, "Error al conectar con Mockoon: ${e.message}")
                    _billsUiState.update { it.copy(errorMessage = "Error al conectar con Mockoon: ${e.message}") }
                }
            }else {
                billsRepository.insertMockBillsFromAssets()
                delay((1000 + (random() * 2000)).toLong()) // delay entre 1 y 3 seg
            }
            delay(700)
            _billsUiState.update { it.copy(isLoading = false) }
        }
    }

    fun setDateLimits(minDate: Date, maxDate: Date){
        _filterUiState.update { currentState ->
            currentState.copy(
                minDate = minDate,
                maxDate = maxDate
            )
        }
    }

    fun setPriceLimits(minPrice: Float, maxPrice: Float) {
        _filterUiState.update { currentState ->
            if (minPrice > maxPrice) return@update currentState

            val wasAtLimits = currentState.priceRange.start == currentState.minPrice &&
                    currentState.priceRange.endInclusive == currentState.maxPrice

            val isFirstLoad = currentState.minPrice == Float.MIN_VALUE

            val safeMax = if (minPrice == maxPrice) maxPrice + 1f else maxPrice

            val newRange = if (wasAtLimits || isFirstLoad) {
                minPrice..safeMax
            } else currentState.priceRange //else {
//                currentState.priceRange.start.coerceIn(minPrice, safeMax)..
//                        currentState.priceRange.endInclusive.coerceIn(minPrice, safeMax)
//            }

            currentState.copy(
                minPrice = minPrice,
                maxPrice = safeMax,
                priceRange = newRange
            )
        }
    }

    fun updateSelectedOption(option: BillTypeEnum) {
        Log.d(TAG, "BILLS -> updateSelectedOption: $option")
        _billsUiState.update {
            it.copy(
                selectedOption = option
            )
        }
    }

    fun updateDirection(directionId: Int, directionStreet: String){
        _billsUiState.update {
            it.copy(
                directionId = directionId,
                directionStreet = directionStreet
            )
        }
    }

    fun filterCriteriaApply(){
        viewModelScope.launch(Dispatchers.Default) {

            Log.d(TAG, "BILLS -> filterCriteria price: ${_filterUiState.value.priceRange}")
            Log.d(TAG, "BILLS -> filterCriteria max-min: ${_filterUiState.value.maxPrice}-${_filterUiState.value.minPrice}")
            Log.d(TAG, "BILLS -> filterCriteria dateFrom: ${_filterUiState.value.selectedDateFrom}")
            Log.d(TAG, "BILLS -> filterCriteria dateTo: ${_filterUiState.value.selectedDateTo}")
            Log.d(TAG, "BILLS -> filterCriteria states: ${_filterUiState.value.selectedStates}")


            val filteredBills = filterBillsLocally(_billsUiState.value.billsList, _filterUiState.value)

            Log.d(TAG, "BILLS -> filterCriteriaApply: ${filteredBills.size}")

            _billsUiState.update {
                it.copy(
                    filteredBillList = filteredBills
                )
            }
        }
    }

    private fun filterBillsLocally(bills: List<Bill>, criteria: FilterUiState): List<Bill> {
        return bills.filter { bill ->
            val priceIn = bill.price in criteria.priceRange
            val statusMatch = criteria.selectedStates.isEmpty() ||
                    criteria.selectedStates.contains(BillStatusEnum.entries[bill.statusId])
            val dateFromMatch = criteria.selectedDateFrom?.let { !bill.emissionDate.before(it) } ?: true
            val dateToMatch = criteria.selectedDateTo?.let { !bill.emissionDate.after(it) } ?: true

            priceIn && statusMatch && dateFromMatch && dateToMatch
        }
    }

    fun clearFilters(initialValueIsOnline: Boolean){
        viewModelScope.launch {
            connectivityRepository.isOnline.first { it != initialValueIsOnline }
            _filterUiState.update {
                it.copy(
                    selectedDateFrom = null,
                    selectedDateTo = null,
                    priceRange = Float.MIN_VALUE..Float.MAX_VALUE,
                    maxPrice = Float.MAX_VALUE,
                    minPrice = Float.MIN_VALUE,
                    selectedStates = BillStatusEnum.entries
                )
            }
        }
    }


    // Lo que antes habia en filter
    fun clearFilters() {
        val maxPrice = _filterUiState.value.maxPrice
        val minPrice = _filterUiState.value.minPrice

        _filterUiState.update {
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

        _filterUiState.update {
            it.copy(
                selectedDateFrom = dateFrom,
                selectedDateTo = dateTo,
                priceRange = priceRange,
                selectedStates = selectedStatesAux
            )
        }
    }

    fun onClearDate(dateField: Int) {
        when (dateField) {
            0 -> _filterUiState.update { it.copy(selectedDateFrom = null) }
            1 -> _filterUiState.update { it.copy(selectedDateTo = null) }
        }
        if(dateField == 0){
            Log.d(TAG, "onClearDate(0): ${_filterUiState.value.selectedDateFrom}")
        }else {
            Log.d(TAG, "onClearDate(1): ${_filterUiState.value.selectedDateTo}")
        }
    }

    fun onClearState(state: BillStatusEnum) {
        var futureState = _filterUiState.value.selectedStates - state

        if(_filterUiState.value.selectedStates.size == 1
            && _filterUiState.value.selectedStates.contains(state)){
            futureState = BillStatusEnum.entries
        }

        _filterUiState.update {
            it.copy(
                selectedStates = futureState
            )
        }
        Log.d(TAG, "onClearState: ${_filterUiState.value.selectedStates}")
    }

    fun onClearPriceRange() {
        _filterUiState.update {
            it.copy(
                priceRange = it.minPrice..it.maxPrice
            )
        }
        Log.d(TAG, "onClearPriceRange: ${_filterUiState.value.priceRange}")
    }

    fun clearFilterField(activeFilterItem: ActiveFilterItem){
        when(activeFilterItem.type){
            FilterType.DATE_FROM -> onClearDate(0)
            FilterType.DATE_TO -> onClearDate(1)
            FilterType.PRICE_RANGE -> onClearPriceRange()
            FilterType.STATUS -> onClearState(BillStatusEnum.entries.find{ it.title == activeFilterItem.label }!!)
        }
    }

}