package com.iberdrola.practicas2026.alejandroLO.data.repository.filter

import android.util.Log
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OfflineFilterRepository() : FilterRepository {

    val TAG = "OfflineFilterRepository"
    private val _filterCriteria = MutableStateFlow(FilterUiState())
    override val filterCriteria = _filterCriteria.asStateFlow()

    private val _maxPrice = MutableStateFlow(Float.MAX_VALUE)
    override val maxPrice = _maxPrice.asStateFlow()

    private val _minPrice = MutableStateFlow(Float.MIN_VALUE)
    override val minPrice = _minPrice.asStateFlow()


    override fun setFilterCriteria(filterCriteria: FilterUiState) {
        if (_filterCriteria.value != filterCriteria) {
            Log.d(TAG, "setFilterCriteria: Actualizando criterios")
            _filterCriteria.value = filterCriteria
        }
    }

    override fun setMaxPrice(maxPrice: Float) {
        if (_maxPrice.value != maxPrice) {
            Log.d(TAG, "setMaxPrice: $maxPrice")
            _maxPrice.value = maxPrice
        }
    }

    override fun setMinPrice(minPrice: Float) {
        if (_minPrice.value != minPrice) {
            Log.d(TAG, "setMinPrice: $minPrice")
            _minPrice.value = minPrice
        }
    }
    private fun syncCriteriaWithLimits() {
        _filterCriteria.update { current ->

            val newMin = _minPrice.value
            val newMax = _maxPrice.value

            val isUsingFullRange =
                current.priceRange.start == current.minPrice &&
                        current.priceRange.endInclusive == current.maxPrice

            val newRange = if (isUsingFullRange ||
                (current.priceRange.start == 0f && current.priceRange.endInclusive == 0f)
            ) {
                newMin..newMax
            } else {
                current.priceRange.start.coerceIn(newMin, newMax)..
                        current.priceRange.endInclusive.coerceIn(newMin, newMax)
            }

            current.copy(
                minPrice = newMin,
                maxPrice = newMax,
                priceRange = newRange
            )
        }
    }
}