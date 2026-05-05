package com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaTopBar
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.screens.IberdrolaBillsScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsUiState
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.viewModel.ActiveFilterItem
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.FilterUiState
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.abs

// hay que hacer una UI que almacene selectedOption

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IberdrolaMainScreen(
    onBackButtonClick: () -> Unit,
    onFilterClick: () -> Unit,
    onElectronicBillClick: (String, Int) -> Unit,
    modifier: Modifier = Modifier,
    locale: Locale = Locale.forLanguageTag("es-ES"),
    billsViewModel: BillsViewModel = viewModel(factory = BillsViewModelFactory.Factory),
) {

    LaunchedEffect(Unit) {
        billsViewModel.refreshBills()
    }

    val billsUiState by billsViewModel.billsUiState.collectAsState()
    val filterUiState by billsViewModel.filterUiState.collectAsState()

    IberdrolaMainScreenContent(
        billsUiState = billsUiState,
        filterUiState = filterUiState,
        onBackButtonClick = onBackButtonClick,
        onFilterClick = onFilterClick,
        onOptionSelected = { billsViewModel.updateSelectedOption(it) },
        onRefresh = { billsViewModel.refreshBills() },
        onClearFilterField = {
            billsViewModel.clearFilterField(it)
            billsViewModel.refreshBills()
        },
        onElectronicBillClick = onElectronicBillClick,
        modifier = modifier,
        locale = locale
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IberdrolaMainScreenContent(
    billsUiState: BillsUiState,
    filterUiState: FilterUiState,
    onBackButtonClick: () -> Unit,
    onFilterClick: () -> Unit,
    onOptionSelected: (BillTypeEnum) -> Unit,
    onRefresh: () -> Unit,
    onClearFilterField: (ActiveFilterItem) -> Unit,
    onElectronicBillClick: (String, Int) -> Unit,
    modifier: Modifier = Modifier,
    locale: Locale = Locale.forLanguageTag("es-ES")
) {
    // usamos state para forzar a la pantalla a calcular el valor de filterUiState, asi detecta el cambio al borrar el chip
    val filterIsApplied = remember(filterUiState) {
        filterUiState.selectedDateFrom != null ||
                filterUiState.selectedDateTo != null ||
                filterUiState.priceRange != filterUiState.minPrice..filterUiState.maxPrice ||
                filterUiState.selectedStates.size != BillStatusEnum.entries.size
                // con comparar el tamaño basta, nos da igual porque este filtrando, solo si lo
                // está haciendo o no
    }

    val billsToShow = if (filterIsApplied) billsUiState.filteredBillList else billsUiState.billsList


    if (filterIsApplied) {
        Log.d("FilterDebug", "Applied because -> Date: ${filterUiState.selectedDateFrom != null}, " +
                "Status: ${filterUiState.selectedStates.size != BillStatusEnum.entries.size}, " +
                "Price: ${abs(filterUiState.priceRange.start - filterUiState.minPrice) > 0.01f}")
    }

    // está deshabilitado si no hay filtros y no hay facturas
    val enableFilterButton = filterIsApplied || billsUiState.billsList.isNotEmpty()

    var showAlert by remember { mutableStateOf(false) }
    val selectingBill: (Bill) -> Unit = remember {
        {
            showAlert = true
        }
    }

    val pagerState = rememberPagerState(
        initialPage = if (billsUiState.selectedOption == BillTypeEnum.LUZ) 0 else 1,
        pageCount = { 2 }
    )

    LaunchedEffect(pagerState.currentPage) {
        val option = if (pagerState.currentPage == 0) BillTypeEnum.LUZ else BillTypeEnum.GAS
        onOptionSelected(option)
    }

    BackHandler { // quiero que también cuente el contador si le das al botón back
        onBackButtonClick()
    }

    val scope: CoroutineScope = rememberCoroutineScope()


    Box(Modifier.background(color = IberdrolaTheme.colors.surface)) {
        Column(modifier = modifier
            .fillMaxSize()
            .testTag("main_screen")
        ) {
//            Log.d("MainScreen", "is sync enabled: ${billsUiState.value.isOnline}")
            IberdrolaTopBar(
                selectedOption = billsUiState.selectedOption,
                streetName = billsUiState.directionStreet,
                options = billsUiState.options,
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


                val filteredBills = billsToShow.filter {
                    it.typeId == page // 0 = Luz, 1 = Gas
                }

                val lastBill = billsUiState.billsList.maxByOrNull { it.emissionDate.time }

                IberdrolaBillsScreen(
                    bills = filteredBills,
                    lastBill = lastBill,
                    isLoading = billsUiState.isLoading,
                    onclick = { selectingBill(it) },
                    refresh = onRefresh,
                    modifier = Modifier.fillMaxSize(),
                    error = billsUiState.errorMessage,
                    locale = locale,
                    onFilterClick = onFilterClick,
                    filterUiState = filterUiState,
                    clearFilterField = onClearFilterField,
                    filterIsApplied = filterIsApplied,
                    enableFilterButton = enableFilterButton,
                    onElectronicBillClick = { onElectronicBillClick(billsUiState.directionStreet, billsUiState.directionId) },
                )
            }
        }
        if(showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                confirmButton = {
                    TextButton(onClick = { showAlert = false }) {
                        Text(stringResource(R.string.cerrar))
                    }
                },
                title = { Text(stringResource(R.string.factura_no_disponible)) },
                containerColor = IberdrolaTheme.colors.surface
            )
        }
    }
}


@Composable
@Preview
fun PreviewIberdrolaMainScreen() {
    IB2026AlejandroLOTheme {
        IberdrolaMainScreenContent(
            billsUiState = BillsUiState(
                directionStreet = "Calle de la energía",
                options = BillTypeEnum.entries.toList()
            ),
            filterUiState = FilterUiState(),
            onBackButtonClick = { },
            onFilterClick = {},
            onOptionSelected = {},
            onRefresh = {},
            onClearFilterField = {},
            onElectronicBillClick = { _, _ -> },
            modifier = Modifier,
            locale = Locale.forLanguageTag("es-ES")
        )
    }
}
