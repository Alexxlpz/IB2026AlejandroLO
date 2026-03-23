package com.iberdrola.practicas2026.alejandroLO.ui.features.main

import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.BillType

data class MainUiState (
    val options: List<BillType> = emptyList(),
    val selectedOption: BillType = BillType.LUZ
)