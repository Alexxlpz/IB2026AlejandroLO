package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.remoteConfig.RemoteConfigRepository
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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Math.random
import java.util.Date
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

class BillsViewModel(
    private val billsRepository: BillsRepository,
    private val connectivityRepository: ConnectivityRepository,
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    private val _billsUiState = MutableStateFlow(BillsUiState())
    val billsUiState: StateFlow<BillsUiState> = _billsUiState.asStateFlow()

    private val _filterUiState = MutableStateFlow(FilterUiState())
    val filterUiState: StateFlow<FilterUiState> = _filterUiState.asStateFlow()

    private val _isGasEnabled = MutableStateFlow(
        remoteConfigRepository.isGasContractsEnabled()
    )
    val isGasEnabled: StateFlow<Boolean> = _isGasEnabled.asStateFlow()

    private var billsJob: Job? = null
    val TAG: String = "BillsViewModel"

    init {
        load_conectivity()
        loadRemoteConfig()
        load_options()
        refreshBills()
    }

    fun loadRemoteConfig() {
        viewModelScope.launch {
            remoteConfigRepository.fetchAndActivate()
            _isGasEnabled.value = remoteConfigRepository.isGasContractsEnabled()
        }
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

                            var billAux = bills

                            suspendReviewIsGasEnabled()
                            if(!_isGasEnabled.value){
                                billAux = billAux.filter { it.typeId != BillTypeEnum.GAS.ordinal }
                                _billsUiState.update {
                                    it.copy(
                                        billsList = billAux
                                    )
                                }
                            }

                            // para redondear hacia arriba
                            val maxPrice = ceil(billAux.maxOf { it.price }).toFloat()
                            // para redondear hacia abajo
                            val minPrice = floor(billAux.minOf { it.price }).toFloat()

                            val maxDate = billAux.maxOf { it.emissionDate }
                            val minDate = billAux.minOf { it.emissionDate }

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

            var newDateFrom: Date? = if(minDate == maxDate) {
                null
            } else if(currentState.selectedDateFrom != null) {
                if(currentState.selectedDateFrom !in minDate..maxDate) {
                    minDate
                }else {
                    currentState.selectedDateFrom
                }
            }else {
                null
            }
            var newDateTo: Date? = if(minDate == maxDate) {
                null
            } else if(currentState.selectedDateTo != null) {
                if(currentState.selectedDateTo !in minDate..maxDate) {
                    maxDate
                }else {
                    currentState.selectedDateTo
                }
            }else {
                null
            }

            if(newDateTo == newDateFrom) {
                newDateTo = null
                newDateFrom = null
            }

            currentState.copy(
                minDate = minDate,
                maxDate = maxDate,
                selectedDateFrom = newDateFrom,
                selectedDateTo = newDateTo
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

            val newRange = if (wasAtLimits || isFirstLoad || abs(minPrice - maxPrice) < 1f) {
                minPrice..safeMax
            } else {
                currentState.priceRange.start.coerceIn(minPrice, safeMax)..
                        currentState.priceRange.endInclusive.coerceIn(minPrice, safeMax)
            }

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
        _billsUiState.update { it.copy(scrollInitializedPages = emptySet()) }
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
        _billsUiState.update { it.copy(scrollInitializedPages = emptySet()) }
        when(activeFilterItem.type){
            FilterType.DATE_FROM -> onClearDate(0)
            FilterType.DATE_TO -> onClearDate(1)
            FilterType.PRICE_RANGE -> onClearPriceRange()
            FilterType.STATUS -> onClearState(BillStatusEnum.entries.find{ it.title == activeFilterItem.label }!!)
        }
    }

    suspend fun suspendReviewIsGasEnabled() {
        remoteConfigRepository.fetchAndActivate()
        _isGasEnabled.value = remoteConfigRepository.isGasContractsEnabled()
    }

    fun reviewIsGasEnabled(){
        viewModelScope.launch {
            suspendReviewIsGasEnabled()
        }
    }

    fun onReturnFromFilter() {
        _billsUiState.update { it.copy(isReturnFromFilter = true) }
    }

    fun onPageScrollInitialized(page: Int) {
        _billsUiState.update { it.copy(scrollInitializedPages = it.scrollInitializedPages + page) }
    }
}