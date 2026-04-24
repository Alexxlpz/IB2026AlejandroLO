package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.iberdrola.practicas2026.alejandroLO.IberdrolaApplication

/**
 * Factory para instanciar el BillsViewModel con sus dependencias (BillsRepository).
 */
object BillsViewModelFactory {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as IberdrolaApplication)
            val repository = application.container.billsRepository
            val connectivityRepository = application.container.connectivityRepository
            val filterRepository = application.container.filterRepository

            BillsViewModel(
                billsRepository = repository,
                connectivityRepository = connectivityRepository,
                filterRepository = filterRepository
            )
        }
    }
}