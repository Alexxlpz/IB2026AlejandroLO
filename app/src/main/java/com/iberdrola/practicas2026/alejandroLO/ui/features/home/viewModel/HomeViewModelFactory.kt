package com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.iberdrola.practicas2026.alejandroLO.IberdrolaApplication

object HomeViewModelFactory {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as IberdrolaApplication)
            val repository = application.container.directionsRepository
            val connectivityRepository = application.container.connectivityRepository

            HomeViewModel(
                directionRepository = repository,
                connectivityRepository = connectivityRepository
            )
        }
    }
}