package com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens

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
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterViewModel
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
    onFilterClick: () -> Unit,
    filterViewModel: FilterViewModel
) {

    LaunchedEffect(Unit) {
        billsViewModel.refreshBills()
    }

    val billsUiState = billsViewModel.uiState.collectAsState()
    val filterUiState = filterViewModel.uiState.collectAsState()


    var showAlert by remember { mutableStateOf(false) }
    val selectingBill: (Bill) -> Unit = remember {
        {
            showAlert = true
        }
    }

    val pagerState = rememberPagerState(
        initialPage = if (billsUiState.value.selectedOption == BillTypeEnum.LUZ) 0 else 1,
        pageCount = { 2 }
    )

    LaunchedEffect(pagerState.currentPage) {
        val option = if (pagerState.currentPage == 0) BillTypeEnum.LUZ else BillTypeEnum.GAS
        billsViewModel.updateSelectedOption(option)
    }

    BackHandler { // quiero que tambien cuente el contador si le das al boton back
        onBackButtonClick()
    }

    val scope: CoroutineScope = rememberCoroutineScope()


    Box() {
        Column(modifier = modifier
            .fillMaxSize()
            .testTag("main_screen")
        ) {
//            Log.d("MainScreen", "is sync enabled: ${billsUiState.value.isOnline}")
            IberdrolaTopBar(
                selectedOption = billsUiState.value.selectedOption,
                streetName = billsUiState.value.directionStreet,
                options = billsUiState.value.options,
                onOptionSelected = { option ->
                    val page = if (option == BillTypeEnum.LUZ) 0 else 1
                    scope.launch { pagerState.animateScrollToPage(page) }
                },
                onBackButtonClick = onBackButtonClick
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
                    error = billsUiState.value.errorMessage,
                    locale = locale,
                    onFilterClick = onFilterClick,
                    filterUiState = filterUiState.value,
                    clearFilterField = { filterViewModel.clearFilterField(it) }
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
        locale = Locale.forLanguageTag("es-ES"),
        onFilterClick = {},
        filterViewModel = viewModel()
    )
}