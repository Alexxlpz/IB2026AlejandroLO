package com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel

import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import java.util.Date

data class FilterUiState (
    val selectedDateFrom: Date? = null,
    val selectedDateTo: Date? = null,
    val priceRange: ClosedFloatingPointRange<Float> = 15f..151f,
    val selectedStates: List<BillStatusEnum> = emptyList()
)