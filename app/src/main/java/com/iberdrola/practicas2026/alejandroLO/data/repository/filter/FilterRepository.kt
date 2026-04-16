package com.iberdrola.practicas2026.alejandroLO.data.repository.filter

import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterUiState
import kotlinx.coroutines.flow.StateFlow

interface FilterRepository {
    val filterCriteria: StateFlow<FilterUiState>
    val maxPrice: StateFlow<Float>
    val minPrice: StateFlow<Float>

    fun setFilterCriteria(filterCriteria: FilterUiState)
    fun setMaxPrice(maxPrice: Float)
    fun setMinPrice(minPrice: Float)

}