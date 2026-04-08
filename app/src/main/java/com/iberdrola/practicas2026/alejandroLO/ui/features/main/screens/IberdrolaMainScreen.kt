package com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
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
import java.util.Locale

// hay que hacer una UI que almacene selectedOption

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IberdrolaMainScreen(
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    locale: Locale = Locale.forLanguageTag("es-ES"),
    billsViewModel: BillsViewModel = viewModel(factory = BillsViewModelFactory.Factory),
    mainViewModel: MainViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        billsViewModel.updateDataBase(mainViewModel.uiState.value.isOnline)
    }

    val mainUiState = mainViewModel.uiState.collectAsState()

    val billsUiState = billsViewModel.uiState.collectAsState()

    var showAlert by remember { mutableStateOf(false) }
    val selectingBill: (Bill) -> Unit = remember {
        {
            showAlert = true
        }
    }

    val pagerState = rememberPagerState(
        initialPage = if (mainUiState.value.selectedOption == BillTypeEnum.LUZ) 0 else 1,
        pageCount = { 2 }
    )

    LaunchedEffect(pagerState.currentPage) {
        val option = if (pagerState.currentPage == 0) BillTypeEnum.LUZ else BillTypeEnum.GAS
        mainViewModel.updateSelectedOption(option)
        billsViewModel.updateSelectedOption(option)
    }

    BackHandler { // quiero que tambien cuente el contador si le das al boton back
        onBackButtonClick()
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
        Column(modifier = modifier
            .fillMaxSize()
            .testTag("main_screen")
        ) {
            Log.d("MainScreen", "is sync enabled: ${mainUiState.value.isOnline}")
            IberdrolaTopBar(
                selectedOption = mainUiState.value.selectedOption,
                options = mainUiState.value.options,
                onOptionSelected = { option ->
                    val page = if (option == BillTypeEnum.LUZ) 0 else 1
                    scope.launch { pagerState.animateScrollToPage(page) }
                },
                onBackButtonClick = onBackButtonClick,
                isSyncEnabled = mainUiState.value.isOnline,
                onSyncToggle = {
                    billsViewModel.updateDataBase(it)
                    mainViewModel.updateIsOnline(it)
                }
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
                    refresh = { billsViewModel.refreshBills() },
                    modifier = Modifier.fillMaxSize(),
                    locale = locale
                )
            }
        }
        if(showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                confirmButton = {
                    TextButton(onClick = { showAlert = false }) {
                        Text("Cerrar")
                    }
                },
                title = { Text("Funcionalidad aún no implementada") },
                text = { Text("Esta funcionalidad aún no está implementada, mantente alerta a futuras actualizaciones") }
            )
        }
    }
}

@Composable
@Preview
fun PreviewIberdrolaMainScreen() {
    IberdrolaMainScreen(
        modifier = Modifier,
        onBackButtonClick = { },
        locale = Locale.forLanguageTag("es-ES")
    )
}