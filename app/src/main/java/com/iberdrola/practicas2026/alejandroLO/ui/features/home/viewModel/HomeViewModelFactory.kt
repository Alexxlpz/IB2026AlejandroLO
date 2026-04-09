package com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.iberdrola.practicas2026.alejandroLO.IberdrolaApplication
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModel

object HomeViewModelFactory {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as IberdrolaApplication)
            val repository = application.container.directionsRepository
            HomeViewModel(directionRepository = repository)
        }
    }
}