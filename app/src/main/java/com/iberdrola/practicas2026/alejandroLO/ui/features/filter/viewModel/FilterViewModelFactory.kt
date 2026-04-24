package com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.iberdrola.practicas2026.alejandroLO.IberdrolaApplication

object FilterViewModelFactory {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as IberdrolaApplication)
            val filterRepository = application.container.filterRepository

            FilterViewModel(
                filterRepository = filterRepository
            )
        }
    }
}