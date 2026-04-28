package com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.viewModel

import com.iberdrola.practicas2026.alejandroLO.data.model.ElectronicBill

data class ElectronicBillsUiState (
    val electronicBills: List<ElectronicBill> = emptyList(),
    val isOnline: Boolean = false,
    val errorMessage: String? = null
)