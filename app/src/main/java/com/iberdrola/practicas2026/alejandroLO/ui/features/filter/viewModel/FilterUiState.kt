package com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel

import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import java.util.Date

data class FilterUiState (
    val selectedDateFrom: Date? = null,
    val selectedDateTo: Date? = null,
    val priceRange: ClosedFloatingPointRange<Float> = Float.MIN_VALUE..Float.MAX_VALUE,
    val selectedStates: List<BillStatusEnum> = BillStatusEnum.entries.toList(),
    val maxPrice: Float = Float.MAX_VALUE,
    val minPrice: Float = Float.MIN_VALUE
)