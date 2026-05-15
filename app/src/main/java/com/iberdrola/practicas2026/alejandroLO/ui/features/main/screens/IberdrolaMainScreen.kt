package com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens

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
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.FilterUiState
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.viewModel.ActiveFilterItem
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IberdrolaMainScreen(
    onBackButtonClick: () -> Unit,
    onFilterClick: () -> Unit,
    onElectronicBillClick: (String, Int) -> Unit,
    modifier: Modifier = Modifier,
    locale: Locale = Locale.forLanguageTag("es-ES"),
    billsViewModel: BillsViewModel = viewModel(factory = BillsViewModelFactory.Factory),
    onChangeBillType: (String) -> Unit,
    onButtonClick: (String) -> Unit
) {

    LaunchedEffect(Unit) {
        billsViewModel.refreshBills()
    }

    val billsUiState by billsViewModel.billsUiState.collectAsState()
    val filterUiState by billsViewModel.filterUiState.collectAsState()
    val isGasEnabled by billsViewModel.isGasEnabled.collectAsState()

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
        onChangeBillType = onChangeBillType,
        onButtonClick = onButtonClick,
        isGasEnabled = isGasEnabled,
        onPageScrollInitialized = { billsViewModel.onPageScrollInitialized(it) },
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
    onChangeBillType: (String) -> Unit,
    onButtonClick: (String) -> Unit,
    isGasEnabled: Boolean,
    onPageScrollInitialized: (Int) -> Unit,
    modifier: Modifier = Modifier,
    locale: Locale = Locale.forLanguageTag("es-ES")
) {
    val filterIsApplied = remember(filterUiState) {
        filterUiState.selectedDateFrom != null ||
                filterUiState.selectedDateTo != null ||
                filterUiState.priceRange != filterUiState.minPrice..filterUiState.maxPrice ||
                filterUiState.selectedStates.size != BillStatusEnum.entries.size
    }

    val billsToShow = if (filterIsApplied) billsUiState.filteredBillList else billsUiState.billsList

    val enableFilterButton = filterIsApplied || billsUiState.billsList.isNotEmpty()

    var showAlert by remember { mutableStateOf(false) }
    val selectingBill: (Bill) -> Unit = remember {
        {
            showAlert = true
        }
    }



    val pagerState = if(isGasEnabled) {
        rememberPagerState(
            initialPage = if (billsUiState.selectedOption == BillTypeEnum.LUZ) 0 else 1,
            pageCount = { 2 }
        )
    }else {
        rememberPagerState(
            initialPage = BillTypeEnum.LUZ.ordinal,
            pageCount = { 1 }
        )
    }


    LaunchedEffect(pagerState.currentPage) {
        val option = if (pagerState.currentPage == 0) BillTypeEnum.LUZ else BillTypeEnum.GAS
        onOptionSelected(option)
    }

    BackHandler {
        onBackButtonClick()
    }

    val scope: CoroutineScope = rememberCoroutineScope()


    Box(Modifier.background(color = IberdrolaTheme.colors.surface)) {
        Column(modifier = modifier
            .fillMaxSize()
            .testTag("main_screen")
        ) {
            IberdrolaTopBar(
                selectedOption = billsUiState.selectedOption,
                streetName = billsUiState.directionStreet,
                options = billsUiState.options,
                onOptionSelected = { option ->
                    onChangeBillType(option.title)
                    val page = if (option == BillTypeEnum.LUZ) 0 else 1
                    scope.launch { pagerState.animateScrollToPage(page) }
                },
                onBackButtonClick = onBackButtonClick,
                isGasEnabled = isGasEnabled
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->


                val filteredBills = billsToShow.filter {
                    it.typeId == page
                }

                val lastBill = billsUiState.billsList.filter {
                    it.typeId == page
                }.maxByOrNull { it.emissionDate.time }

                IberdrolaBillsScreen(
                    bills = filteredBills,
                    lastBill = lastBill,
                    isLoading = billsUiState.isLoading,
                    onclick = {
                        onButtonClick("factura ${it.id} clickada")
                        selectingBill(it)
                    },
                    refresh = onRefresh,
                    modifier = Modifier.fillMaxSize(),
                    error = billsUiState.errorMessage,
                    locale = locale,
                    onFilterClick = onFilterClick,
                    filterUiState = filterUiState,
                    clearFilterField = onClearFilterField,
                    filterIsApplied = filterIsApplied,
                    enableFilterButton = enableFilterButton,
                    onElectronicBillClick = {
                        onButtonClick("boton_electronicBills")
                        onElectronicBillClick(billsUiState.directionStreet, billsUiState.directionId)
                    },
                    isActivePage = page == pagerState.currentPage,
                    initialScrollDone = billsUiState.scrollInitializedPages.contains(page),
                    onScrollInitialized = { onPageScrollInitialized(page) }
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
                text = { Text(stringResource(R.string.factura_no_disponible)) },
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
            onBackButtonClick = {},
            onFilterClick = {},
            onOptionSelected = {},
            onRefresh = {},
            onClearFilterField = {},
            onElectronicBillClick = { _, _ -> },
            onChangeBillType = {},
            onButtonClick = {},
            isGasEnabled = true,
            onPageScrollInitialized = {},
            modifier = Modifier,
            locale = Locale.forLanguageTag("es-ES")
        )
    }
}
