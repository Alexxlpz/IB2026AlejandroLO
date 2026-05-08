package com.iberdrola.practicas2026.alejandroLO.ui.features.main.viewModel

import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.enums.FilterType

data class ActiveFilterItem(
    val type: FilterType,
    val label: String
)