package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel

import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum

data class BillsUiState (
    val billsList: List<Bill> = emptyList(),
    val selectedOption: BillTypeEnum = BillTypeEnum.LUZ,
    val isLoading: Boolean = false,
    val isOnline: Boolean = false,
    val hasBills: Boolean = billsList.isNotEmpty()
)