package com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.repository.analyticsRepository.AnalyticsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.direction.DirectionRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.electronicBill.ElectronicBillsRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Math.random

class HomeViewModel(
    private val directionRepository: DirectionRepository,
    private val electronicBillsRepository: ElectronicBillsRepository,
    private val connectivityRepository: ConnectivityRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {

    val TAG = "HomeViewModel"

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var directionJob: Job? = null

    init {
        load_conectivity()
        refreshDirections()
    }

    fun load_conectivity() {
        viewModelScope.launch {
            connectivityRepository.isOnline.collect { status ->
                _uiState.update { it.copy(isOnline = status) }
            }
        }
    }

    @OptIn(FlowPreview::class)
    fun refreshDirections() {
        directionJob?.cancel()
        val isOnline = _uiState.value.isOnline
        viewModelScope.launch {
            _uiState.update { it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

            directionJob = launch {
                directionRepository.getAllDirections()
                    .debounce(300)
                    .onCompletion {
                        try {
                            electronicBillsRepository.refreshElectronicBillsOnline()
                        } catch (e: Exception) {
                            Log.e(TAG, "Error silencioso en onCompletion: ${e.message}")
                        }
                    }
                    .collect { bills ->
                    _uiState.update {
                        it.copy(
                            directionList = bills
                        )
                    }
                }
            }

            try{
                if (isOnline) {
                    try {
                        directionRepository.refreshDirectionsOnline()
                        delay(1000) // debido a que se carga demasiado rapido y ves aparecer las direcciones mientras se cargan
                        electronicBillsRepository.refreshElectronicBillsOnline()
                        delay(100)
                    } catch (e: Exception) {
                        if (e.message != "Error en refreshBillsOnline") {
                            Log.e(TAG, "Error al conectar con Mockoon: ${e.message}")
                            _uiState.update { it.copy(errorMessage = e.message) }
                        } else {
                            Log.w(TAG, "Error de facturas electrónicas ignorado en Home")
                        }
                    }
                }else {
                    directionRepository.insertMockDirectionsFromAssets()
                    delay((1000 + (random() * 2000)).toLong()) // delay entre 1 y 3 seg
                    electronicBillsRepository.insertMockElectronicBillsFromAssets()
                    delay(100)
                }
            } catch (e: Exception) {
                if(e.message != "Error en refreshBillsOnline") {
                    Log.e(TAG, "Error en el refresco: ${e.message}")
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
            } finally {
                delay(500)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updateDirectionsOnline(isOnline: Boolean, clearFilters: (Boolean) -> Unit){
        clearFilters(isOnline) // espera a que el valor de isOnline cambie y limpia los filtros
        connectivityRepository.setOnlineMode(isOnline) // el collect se encarga de cambiar el valor del uiState
        refreshDirections()
    }

    // ------------- analytics -------------
    // -- funcionalidades reservadas para --
    // -- indicar el flujo al analytics   --
    // -------------------------------------

    fun logScreenView(screen: String) {
        analyticsRepository.logScreenView(screen)
    }

    fun logButtonClick(buttonName: String, screen: String) {
        analyticsRepository.logButtonClick(buttonName, screen)
    }

    fun logElectronicBillEmailUpdated(contractType: String, isModification: Boolean) {
        analyticsRepository.logElectronicBillEmailUpdated(contractType, isModification)
    }

    fun logVerificationAttempt(contractType: String, attemptNumber: Int) {
        analyticsRepository.logVerificationAttempt(contractType, attemptNumber)
    }

    fun logChangeMode(isOnline: Boolean) {
        analyticsRepository.logChangeMode(isOnline)
    }

    fun logSelectDirection(street: String) {
        analyticsRepository.logSelectDirection(street)
    }

    fun logOpenFeedbackSheet(mostrarFeedback: Boolean) {
        analyticsRepository.logOpenFeedbackSheet(mostrarFeedback)
    }

    fun logChangeBillType(type: String) {
        analyticsRepository.logChangeBillType(type)
    }
}
