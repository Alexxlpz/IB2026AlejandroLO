package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.filter.FilterRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                        // para redondear hacia arriba
                        val maxPrice = ceil(bills.maxOf { it.price }).toFloat()
                        // para redondear hacia abajo
                        val minPrice = floor(bills.minOf { it.price }).toFloat()

                        filterRepository.setMaxPrice(maxPrice)
                        filterRepository.setMinPrice(minPrice)

                        filterCriteriaApply()
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
        filterCriteriaApply() // antes de terminar filtramos las facturas
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
        viewModelScope.launch {

            Log.d(TAG, "BILLS -> filterCriteria price: ${filterCriteria.value.priceRange}")
            Log.d(TAG, "BILLS -> filterCriteria dateFrom: ${filterCriteria.value.selectedDateFrom}")
            Log.d(TAG, "BILLS -> filterCriteria dateTo: ${filterCriteria.value.selectedDateTo}")
            Log.d(TAG, "BILLS -> filterCriteria states: ${filterCriteria.value.selectedStates}")


            val filteredBills = _uiState.value.billsList.filter { bill ->
                bill.price in filterCriteria.value.priceRange &&
                filterCriteria.value.selectedStates.contains(BillStatusEnum.entries[bill.statusId]) &&
                filterCriteria.value.selectedDateFrom?.before(bill.date) ?: true &&
                filterCriteria.value.selectedDateTo?.after(bill.date) ?: true
            }

            Log.d(TAG, "BILLS -> filterCriteriaApply: ${filteredBills.size}")

            _uiState.update {
                it.copy(
                    billsList = filteredBills
                )
            }
        }
    }
}