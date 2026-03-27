package com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens

import android.app.AlertDialog
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaTopBar
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.screens.IberdrolaBillsScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.viewModel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// hay que hacer una UI que almacene selectedOption

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IberdrolaMainScreen(modifier: Modifier = Modifier) {
    val billsViewModel: BillsViewModel = viewModel(factory = BillsViewModelFactory.Factory)
    val mainViewModel: MainViewModel = viewModel()
    val mainUiState = mainViewModel.uiState.collectAsState()

    val billsUiState = billsViewModel.uiState.collectAsState()

    var showAlert by remember { mutableStateOf<Boolean>(false) }
    val selectingBill: (Bill) -> Unit = remember {
        {
            showAlert = true
        }
    }
    val closeAlert: () -> Unit = remember {
        {
            showAlert = false
        }
    }
    val alertDialog = alertDialogOnBillClick(LocalContext.current, closeAlert)


    val pagerState = rememberPagerState(
        initialPage = if (mainUiState.value.selectedOption == BillTypeEnum.LUZ) 0 else 1,
        pageCount = { 2 }
    )

    LaunchedEffect(pagerState.currentPage) {
        val option = if (pagerState.currentPage == 0) BillTypeEnum.LUZ else BillTypeEnum.GAS
        mainViewModel.updateSelectedOption(option)
        billsViewModel.updateSelectedOption(option)
    }

    val scope: CoroutineScope = rememberCoroutineScope()


//    val bill = Bill(
//        type = BillType.LUZ.title,
//        price = 100.0,
//        status = BillStatus.PENDIENTE.title,
//        date = Date(),
//        dueDate = Date()
//    )
//    val bills = listOf(bill, bill, bill, bill, bill, bill, bill, bill, bill, bill)

    Box() {
        Column(modifier = modifier.fillMaxSize()) {

            IberdrolaTopBar(
                selectedOption = mainUiState.value.selectedOption,
                options = mainUiState.value.options,
                onOptionSelected = { option ->
                    val page = if (option == BillTypeEnum.LUZ) 0 else 1
                    scope.launch { pagerState.animateScrollToPage(page) }
                },
                isSyncEnabled = billsUiState.value.isOnline,
                onSyncToggle = { billsViewModel.updateDataBase(it) }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->

                val filteredBills = billsUiState.value.billsList.filter {
                    it.typeId == page // 0 = Luz, 1 = Gas
                }

                val lastBill = filteredBills.maxByOrNull { it.date.time }

                IberdrolaBillsScreen(
                    bills = filteredBills,
                    lastBill = lastBill,
                    isLoading = billsUiState.value.isLoading,
                    onclick = { selectingBill(it) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        if(showAlert) {
            alertDialog.show()
        }
    }
}

private fun alertDialogOnBillClick(context: Context, closeAlert: () -> Unit): AlertDialog{
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder
        .setMessage("Esta funcionalidad aun no esta implementada, " +
                "mantente alerta a futuras actualizaciones")
        .setTitle("Funcionalidad aun no implementada")
        .setNegativeButton("Cerrar") { _, _ ->
            closeAlert()
        }

   return builder.create()

}

@Composable
@Preview
fun PreviewIberdrolaMainScreen() {
    IberdrolaMainScreen(modifier = Modifier)
}