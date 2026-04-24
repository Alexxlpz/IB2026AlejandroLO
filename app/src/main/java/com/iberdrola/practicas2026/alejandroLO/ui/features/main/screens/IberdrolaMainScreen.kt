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
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterViewModel
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

    // usamos state para forzar a la pantalla a calcular el valor de filterUiState, asi detecta el cambio al borrar el chip
    val state = filterUiState.value
    val filterIsApplied = remember(state) {
        state.selectedDateFrom != null ||
                state.selectedDateTo != null ||
                state.priceRange != state.minPrice..state.maxPrice ||
                state.selectedStates.size != BillStatusEnum.entries.size
                // con comparar el tamaño basta, nos da igual porque este filtrando, solo si lo
                // está haciendo o no
    }

    if (filterIsApplied) {
        Log.d("FilterDebug", "Applied because -> Date: ${state.selectedDateFrom != null}, " +
                "Status: ${state.selectedStates.size != BillStatusEnum.entries.size}, " +
                "Price: ${abs(state.priceRange.start - state.minPrice) > 0.01f}")
    }

    // está deshabilitado si no hay filtros y no hay facturas
    val enableFilterButton = filterIsApplied || billsUiState.value.billsList.isNotEmpty()

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


    Box(Modifier.background(color = IberdrolaTheme.colors.surface)) {
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

                val lastBill = filteredBills.maxByOrNull { it.emissionDate.time }

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
                    clearFilterField = { filterViewModel.clearFilterField(it) },
                    filterIsApplied = filterIsApplied,
                    enableFilterButton = enableFilterButton
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
                title = { Text(" la factura aún no está disponible") },
                containerColor = IberdrolaTheme.colors.surface
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