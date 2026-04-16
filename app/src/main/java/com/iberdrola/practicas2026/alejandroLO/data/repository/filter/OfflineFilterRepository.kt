package com.iberdrola.practicas2026.alejandroLO.data.repository.filter

import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OfflineFilterRepository() : FilterRepository {

    private val _filterCriteria = MutableStateFlow(FilterUiState())
    override val filterCriteria = _filterCriteria.asStateFlow()

    private val _maxPrice = MutableStateFlow(0f)
    override val maxPrice = _maxPrice.asStateFlow()

    private val _minPrice = MutableStateFlow(999f)
    override val minPrice = _minPrice.asStateFlow()

    override fun setFilterCriteria(filterCriteria: FilterUiState) {
        _filterCriteria.value = filterCriteria
    }

    override fun setMaxPrice(maxPrice: Float) {
        _maxPrice.value = maxPrice
    }

    override fun setMinPrice(minPrice: Float) {
        _minPrice.value = minPrice
    }
}