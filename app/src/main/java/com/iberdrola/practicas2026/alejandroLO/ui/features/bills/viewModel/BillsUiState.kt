package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel

import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum

data class BillsUiState (
    val billsList: List<Bill> = emptyList(),
    val selectedOption: BillTypeEnum = BillTypeEnum.LUZ,
    val isLoading: Boolean = false,
    val isOnline: Boolean = false,
    val options: List<BillTypeEnum> = emptyList(),
    val directionId: Int = 0,
    val directionStreet: String = ""
)