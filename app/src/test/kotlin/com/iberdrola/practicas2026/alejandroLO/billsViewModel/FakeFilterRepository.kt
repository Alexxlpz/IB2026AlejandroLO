package com.iberdrola.practicas2026.alejandroLO.billsViewModel

import com.iberdrola.practicas2026.alejandroLO.data.repository.filter.FilterRepository
import kotlinx.coroutines.flow.asStateFlow

class FakeFilterRepository : FilterRepository {
    private val _filterCriteria = kotlinx.coroutines.flow.MutableStateFlow(com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterUiState())
    override val filterCriteria = _filterCriteria.asStateFlow()

    private val _maxPrice = kotlinx.coroutines.flow.MutableStateFlow(0f)
    override val maxPrice = _maxPrice.asStateFlow()

    private val _minPrice = kotlinx.coroutines.flow.MutableStateFlow(0f)
    override val minPrice = _minPrice.asStateFlow()

    override fun setFilterCriteria(filterCriteria: com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterUiState) {
        _filterCriteria.value = filterCriteria
    }

    override fun setMaxPrice(maxPrice: Float) {
        _maxPrice.value = maxPrice
    }

    override fun setMinPrice(minPrice: Float) {
        _minPrice.value = minPrice
    }

    override fun clearFilter() {
        _filterCriteria.value = com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterUiState()
    }
}