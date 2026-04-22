package com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.direction.DirectionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Math.random

class HomeViewModel(
    private val directionRepository: DirectionRepository,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    val TAG = "HomeViewModel"

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

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

    fun refreshDirections() {
        val isOnline = _uiState.value.isOnline
        viewModelScope.launch {
            _uiState.update { it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

            val observator = launch {
                directionRepository.getAllDirections().collect { bills ->
                    _uiState.update {
                        it.copy(
                            directionList = bills
                        )
                    }
                }
            }

            if (isOnline) {
                try {
                    directionRepository.refreshDirectionsOnline()
                    delay(1000) // debido a que se carga demasiado rapido y ves aparecer las direcciones mientras se cargan
                } catch (e: Exception) {
                    Log.e(TAG, "Error al conectar con Mockoon: ${e.message}")
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
            }else {
                directionRepository.insertMockDirectionsFromAssets()
                delay((1000 + (random() * 2000)).toLong()) // delay entre 1 y 3 seg
            }
            _uiState.update { it.copy(isLoading = false) }

            observator.cancel()
        }
    }

    fun updateDirectionsOnline(isOnline: Boolean, clearFilters: () -> Unit){
        connectivityRepository.setOnlineMode(isOnline) // el collect se encarga de cambiar el valor del uiState
        clearFilters()
        refreshDirections()
    }
}