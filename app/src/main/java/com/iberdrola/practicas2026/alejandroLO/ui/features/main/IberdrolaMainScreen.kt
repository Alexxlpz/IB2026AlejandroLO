package com.iberdrola.practicas2026.alejandroLO.ui.features.main

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaTopBar
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.BillsViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.BillsViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.IberdrolaBillsScreen
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import androidx.compose.animation.core.*
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.defaultShimmerTheme

// hay que hacer una UI que almacene selectedOption

@Composable
fun IberdrolaMainScreen(modifier: Modifier = Modifier) {
    val billsViewModel: BillsViewModel = viewModel(factory = BillsViewModelFactory.Factory)
    val mainViewModel: MainViewModel = viewModel()
    val mainUiState = mainViewModel.uiState.collectAsState()

    val billsUiState = billsViewModel.uiState.collectAsState()

//    val bill = Bill(
//        type = BillType.LUZ.title,
//        price = 100.0,
//        status = BillStatus.PENDIENTE.title,
//        date = Date(),
//        dueDate = Date()
//    )
//    val bills = listOf(bill, bill, bill, bill, bill, bill, bill, bill, bill, bill)

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
            isSyncEnabled = billsUiState.value.isOnline,
            onSyncToggle = { billsViewModel.updateDataBase(it) }
        )

        IberdrolaBillsScreen(
            bills = billsUiState.value.billsList,
            lastBill = billsUiState.value.lastBill,
            isLoading = billsUiState.value.isLoading,
            modifier = Modifier.weight(1f)
        )

    }
}

@Composable
@Preview
fun PreviewIberdrolaMainScreen() {
    IberdrolaMainScreen(modifier = Modifier)
}