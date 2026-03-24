package com.iberdrola.practicas2026.alejandroLO.ui.features.bills

import androidx.annotation.NonNull
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import java.util.Date

data class BillsUiState (
    val billsList: List<Bill?> = emptyList(),
    val lastBill: Bill? = null,
    val selectedOption: BillType = BillType.LUZ,
    val isLoading: Boolean = false,
    val isLocal: Boolean = true,
    val hasBills: Boolean = billsList.isNotEmpty()
)