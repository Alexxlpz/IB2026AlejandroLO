package com.iberdrola.practicas2026.alejandroLO.ui.features.main.viewModel

import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum

data class MainUiState (
    val options: List<BillTypeEnum> = emptyList(),
    val selectedOption: BillTypeEnum = BillTypeEnum.LUZ,
    val isOnline: Boolean = false
)