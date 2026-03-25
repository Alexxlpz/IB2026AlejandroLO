package com.iberdrola.practicas2026.alejandroLO.ui.features.bills

import com.iberdrola.practicas2026.alejandroLO.data.model.Bill

data class BillsUiState (
    val billsList: List<Bill> = emptyList(),
    val lastBill: Bill? = null,
    val selectedOption: BillTypeEnum = BillTypeEnum.LUZ,
    val isLoading: Boolean = false,
    val isOnline: Boolean = false,
    val hasBills: Boolean = billsList.isNotEmpty()
)