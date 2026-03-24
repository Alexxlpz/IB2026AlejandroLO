package com.iberdrola.practicas2026.alejandroLO.ui.features.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.BillStatus
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.BillType
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaTopBar
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.BillsViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.BillsViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.IberdrolaBillsScreen
import java.util.Date

// hay que hacer una UI que almacene selectedOption

@Composable
fun IberdrolaMainScreen(modifier: Modifier = Modifier) {
    val billsViewModel: BillsViewModel = viewModel(factory = BillsViewModelFactory.Factory)
    val mainViewModel: MainViewModel = viewModel()
    val mainUiState = mainViewModel.uiState.collectAsState()

    val billsUiState = billsViewModel.uiState.collectAsState()

    val bill = Bill(
        type = BillType.LUZ.title,
        price = 100.0,
        status = BillStatus.PENDIENTE.title,
        date = Date(),
        dueDate = Date()
    )
    val bills = listOf(bill, bill, bill, bill, bill, bill, bill, bill, bill, bill)

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        IberdrolaTopBar(
            selectedOption = mainUiState.value.selectedOption,
            options = mainUiState.value.options,
            onOptionSelected = {
                mainViewModel.updateSelectedOption(it)
                billsViewModel.updateSelectedOption(it)
            },
            isSyncEnabled = billsUiState.value.isLocal,
            onSyncToggle = { billsViewModel.updateDataBase(it) }
        )

        IberdrolaBillsScreen(
            bills = bills,
            lastBill = bill,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
@Preview
fun PreviewIberdrolaMainScreen() {
    IberdrolaMainScreen(modifier = Modifier)
}