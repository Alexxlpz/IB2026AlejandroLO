package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.filter.FilterRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Math.random
import kotlin.math.ceil
import kotlin.math.floor

class BillsViewModel(
    private val billsRepository: BillsRepository,
    private val connectivityRepository: ConnectivityRepository,
    private val filterRepository: FilterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BillsUiState())
    val uiState: StateFlow<BillsUiState> = _uiState.asStateFlow()

    val filterCriteria = filterRepository.filterCriteria

    val TAG: String = "BillsViewModel"

    init {
        load_conectivity()
        load_options()
        refreshBills()
        listenToFilterCriteria()
    }

    fun listenToFilterCriteria() {
        viewModelScope.launch {
            filterRepository.filterCriteria.collect {
                refreshBills()
            }
        }
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
                    if (bills.isNotEmpty()) {
                        launch(Dispatchers.IO) {
                            try {
                                val rawMax = billsRepository.getMaxPrice()
                                val rawMin = billsRepository.getMinPrice()

                                // para redondear hacia arriba
                                val maxPrice = ceil(bills.maxOf { rawMax })
                                // para redondear hacia abajo
                                val minPrice = floor(bills.minOf { rawMin })

                                filterRepository.setMaxPrice(maxPrice)
                                filterRepository.setMinPrice(minPrice)

                                filterCriteriaApply()
                            } catch (e: Exception) {
                                Log.e(TAG, "Error calculando rangos de filtro: ${e.message}")
                            }
                        }
                    }
                }
            }

            if (isOnline) {
                try {
                    billsRepository.refreshBillsOnline()
                    // ahora el delay lo simulamos desde mockoon
                    delay(500) // debido a que se carga demasiado rapido y ves aparecer las bills mientras se cargan
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

    fun filterCriteriaApply(){
        viewModelScope.launch(Dispatchers.Default) {

            Log.d(TAG, "BILLS -> filterCriteria price: ${filterCriteria.value.priceRange}")
            Log.d(TAG, "BILLS -> filterCriteria max-min: ${filterCriteria.value.maxPrice}-${filterCriteria.value.minPrice}")
            Log.d(TAG, "BILLS -> filterCriteria dateFrom: ${filterCriteria.value.selectedDateFrom}")
            Log.d(TAG, "BILLS -> filterCriteria dateTo: ${filterCriteria.value.selectedDateTo}")
            Log.d(TAG, "BILLS -> filterCriteria states: ${filterCriteria.value.selectedStates}")


            val filteredBills = filterBillsLocally(_uiState.value.billsList, filterCriteria.value)

            Log.d(TAG, "BILLS -> filterCriteriaApply: ${filteredBills.size}")

            _uiState.update {
                it.copy(
                    billsList = filteredBills
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
            delay(300) //tiempo para darle tiempo a room de pasar el flow
            filterRepository.clearFilter()
        }
    }
}