package com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.model.ElectronicBill
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill.ElectronicBillsRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ElectronicBillsViewModel(
    private val electronicBillsRepository: ElectronicBillsRepository,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ElectronicBillsUiState())
    val uiState: StateFlow<ElectronicBillsUiState> = _uiState.asStateFlow()


    val  DAY_IN_MILLIS = 60 * 60 * 24 * 1000

    private var billsJob: Job? = null

    init {
        viewModelScope.launch {
            load_conectivity()
            delay(1000)
            refreshElectronicBills()
        }
    }

    fun load_conectivity() {
        viewModelScope.launch {
            connectivityRepository.isOnline.collect { status ->
                _uiState.update { it.copy(isOnline = status) }
            }
        }
    }

    fun updateElectronicBillEmail(
        email: String,
        type: BillTypeEnum,
        electronicBill: ElectronicBill
    ){
        viewModelScope.launch {
            val electronicBillAux  = if(type == BillTypeEnum.LUZ){
                    electronicBill.copy(electricityBillEmail = email)
                }else {
                    electronicBill.copy(gasBillEmail = email)
                }
            _uiState.update { currentState ->
                currentState.copy(
                    electronicBills = currentState.electronicBills.map {
                        if (it.id == electronicBillAux.id) {
                            electronicBillAux
                        } else {
                            it
                        }
                    }
                )
            }
            electronicBillsRepository.update(electronicBillAux)
        }
    }

    fun refreshElectronicBills() {
        billsJob?.cancel()
        val isOnline = _uiState.value.isOnline
        viewModelScope.launch {
            _uiState.update { it.copy(
                errorMessage = null
            )
            }

            billsJob = launch {
                electronicBillsRepository.getAllElectronicBills().collect { electronicBills ->
                    _uiState.update {
                        it.copy(
                            electronicBills = electronicBills
                        )
                    }
                }
            }

            if (isOnline) {
                try {
                    electronicBillsRepository.refreshElectronicBillsOnline()
                } catch (e: Exception) {
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
            }else {
                electronicBillsRepository.insertMockElectronicBillsFromAssets()
            }
        }
    }

    fun updateCounterWithAnalyticsPost(post2Analytics: (Int) -> Unit) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    counter = currentState.counter - 1
                )
            }
            post2Analytics(_uiState.value.counter)
        }
    }

    fun resetTimerUpdate() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    resetTimer = System.currentTimeMillis() + DAY_IN_MILLIS
                )
            }
        }
    }

    fun reviewCooldown(){
        if(uiState.value.resetTimer != null &&
            System.currentTimeMillis() >= uiState.value.resetTimer!!){
            viewModelScope.launch {
                _uiState.update { currentState ->
                    currentState.copy(
                        resetTimer = null,
                        counter = 3
                    )
                }
            }
        }
    }
}