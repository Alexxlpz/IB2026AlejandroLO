package com.iberdrola.practicas2026.alejandroLO

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.screens.IberdrolaBillsScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.FilterUiState
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import org.junit.Rule
import org.junit.Test
import java.util.Date

class IberdrolaBillsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // Mock del estado que ahora se gestiona en el BillsViewModel
    private val fakeFilterUiState = mutableStateOf(FilterUiState())

    private fun fakeBill(status: BillStatusEnum = BillStatusEnum.PAGADA): Bill {
        return Bill(
            id = 1,
            directionId = 1,
            typeId = BillTypeEnum.LUZ.ordinal,
            price = 50.0,
            statusId = status.ordinal,
            emissionDate = Date(),
            startDate = Date(),
            endDate = Date()
        )
    }

    @Test
    fun givenLoadingTrue_whenScreenLoaded_thenSkeletonIsDisplayed() {
        composeTestRule.setContent {
            IB2026AlejandroLOTheme {
                IberdrolaBillsScreen(
                    bills = emptyList(),
                    lastBill = null,
                    isLoading = true,
                    onclick = {},
                    onFilterClick = {},
                    filterUiState = fakeFilterUiState.value,
                    clearFilterField = {},
                    enableFilterButton = true,
                    filterIsApplied = false,
                    onElectronicBillClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("bills_skeleton").assertIsDisplayed()
        composeTestRule.onNodeWithText("Histórico de facturas").assertDoesNotExist()
    }

    @Test
    fun givenBillsEmpty_whenLoaded_thenShowEmptyMessage() {
        composeTestRule.setContent {
            IB2026AlejandroLOTheme {
                IberdrolaBillsScreen(
                    bills = emptyList(),
                    lastBill = null,
                    isLoading = false,
                    onclick = {},
                    onFilterClick = {},
                    filterUiState = fakeFilterUiState.value,
                    clearFilterField = {},
                    enableFilterButton = true,
                    filterIsApplied = false,
                    onElectronicBillClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("No hay facturas").assertIsDisplayed()
    }

    @Test
    fun givenBillsWithData_whenLoaded_thenShowLastBill() {
        val bill = fakeBill()
        composeTestRule.setContent {
            IB2026AlejandroLOTheme {
                IberdrolaBillsScreen(
                    bills = listOf(bill),
                    lastBill = bill,
                    isLoading = false,
                    onclick = {},
                    onFilterClick = {},
                    filterUiState = fakeFilterUiState.value,
                    clearFilterField = {},
                    enableFilterButton = true,
                    filterIsApplied = false,
                    onElectronicBillClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Última factura").assertIsDisplayed()
    }

    @Test
    fun givenPaidBill_whenDisplayed_thenShowPagadaStatus() {
        val bill = fakeBill(BillStatusEnum.PAGADA)

        composeTestRule.setContent {
            IB2026AlejandroLOTheme {
                IberdrolaBillsScreen(
                    bills = listOf(bill),
                    lastBill = bill,
                    isLoading = false,
                    onclick = {},
                    onFilterClick = {},
                    filterUiState = fakeFilterUiState.value,
                    clearFilterField = {},
                    enableFilterButton = true,
                    filterIsApplied = false,
                    onElectronicBillClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("bill_status_${BillStatusEnum.PAGADA.title}", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun givenUnpaidBill_whenDisplayed_thenShowPendienteStatus() {
        val bill = fakeBill(BillStatusEnum.PENDIENTE)

        composeTestRule.setContent {
            IB2026AlejandroLOTheme {
                IberdrolaBillsScreen(
                    bills = listOf(bill),
                    lastBill = bill,
                    isLoading = false,
                    onclick = {},
                    onFilterClick = {},
                    filterUiState = fakeFilterUiState.value,
                    clearFilterField = {},
                    enableFilterButton = true,
                    filterIsApplied = false,
                    onElectronicBillClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("bill_status_${BillStatusEnum.PENDIENTE.title}", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun whenClickingOnBill_thenCallbackIsTriggered() {
        val bill = fakeBill()
        var clicked = false

        composeTestRule.setContent {
            IB2026AlejandroLOTheme {
                IberdrolaBillsScreen(
                    bills = listOf(bill),
                    lastBill = null,
                    isLoading = false,
                    onclick = { clicked = true },
                    onFilterClick = {},
                    filterUiState = fakeFilterUiState.value,
                    clearFilterField = {},
                    enableFilterButton = true,
                    filterIsApplied = false,
                    onElectronicBillClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("bill_item").performClick()
        assert(clicked)
    }
}