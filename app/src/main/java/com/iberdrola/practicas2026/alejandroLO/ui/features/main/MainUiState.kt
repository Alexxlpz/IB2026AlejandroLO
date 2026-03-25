package com.iberdrola.practicas2026.alejandroLO.ui.features.main

import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.BillTypeEnum

data class MainUiState (
    val options: List<BillTypeEnum> = emptyList(),
    val selectedOption: BillTypeEnum = BillTypeEnum.LUZ
)