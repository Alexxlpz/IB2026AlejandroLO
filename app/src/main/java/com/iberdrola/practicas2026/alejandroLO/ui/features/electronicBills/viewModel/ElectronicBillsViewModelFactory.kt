package com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.viewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.iberdrola.practicas2026.alejandroLO.IberdrolaApplication

object ElectronicBillsViewModelFactory {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as IberdrolaApplication)
            val repository = application.container.electronicBillsRepository
            val connectivityRepository = application.container.connectivityRepository

            ElectronicBillsViewModel(
                electronicBillsRepository = repository,
                connectivityRepository = connectivityRepository
            )
        }
    }
}