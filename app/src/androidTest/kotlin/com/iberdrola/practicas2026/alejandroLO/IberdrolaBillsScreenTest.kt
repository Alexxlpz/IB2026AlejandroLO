package com.iberdrola.practicas2026.alejandroLO

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.screens.IberdrolaBillsScreen
import org.junit.Rule
import org.junit.Test
import java.util.Date

class IberdrolaBillsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun fakeBill(): Bill {
        return Bill(
            typeId = BillTypeEnum.LUZ.ordinal,
            price = 50.0,
            statusId = BillStatusEnum.PAGADA.ordinal,
            date = Date(),
            dueDate = Date()
        )
    }

    @Test
    fun givenLoadingTrue_whenScreenLoaded_thenSkeletonIsDisplayed() {

        // mostrar skeleton (no hay contenido real)
        composeTestRule.setContent {
            IberdrolaBillsScreen(
                bills = emptyList(),
                lastBill = null,
                isLoading = true,
                onclick = {},
                onFilterClick = { }
            )
        }

        // No hay texto de contenido → implica skeleton visible
        composeTestRule
            .onNodeWithText("Histórico de facturas")
            .assertDoesNotExist()
    }

    @Test
    fun givenBillsEmpty_whenLoaded_thenShowEmptyMessage() {

        // lista vacía
        composeTestRule.setContent {
            IberdrolaBillsScreen(
                bills = emptyList(),
                lastBill = null,
                isLoading = false,
                onclick = {},
                onFilterClick = { }
            )
        }

        // mostrar mensaje "no hay facturas"
        composeTestRule
            .onNodeWithText("No hay facturas")
            .assertIsDisplayed()
    }

    @Test
    fun givenBillsWithData_whenLoaded_thenShowLastBill() {

        val bill = fakeBill()

        // mostrar última factura
        composeTestRule.setContent {
            IberdrolaBillsScreen(
                bills = listOf(bill),
                lastBill = bill,
                isLoading = false,
                onclick = {},
                onFilterClick = { }
            )
        }

        composeTestRule
            .onNodeWithText("Última factura")
            .assertIsDisplayed()
    }

    @Test
    fun givenBillsWithData_whenLoaded_thenShowBillList() {

        val bill = fakeBill()

        // mostrar lista de facturas
        composeTestRule.setContent {
            IberdrolaBillsScreen(
                bills = listOf(bill, bill),
                lastBill = null,
                isLoading = false,
                onclick = {},
                onFilterClick = { }
            )
        }

        composeTestRule
            .onNodeWithText("Histórico de facturas")
            .assertIsDisplayed()
    }

    @Test
    fun givenBillsWithData_whenClickOnBill_thenCallbackIsTriggered() {

        val bill = fakeBill()
        var clicked = false

        // click en factura
        composeTestRule.setContent {
            IberdrolaBillsScreen(
                bills = listOf(bill),
                lastBill = null,
                isLoading = false,
                onclick = { clicked = true },
                onFilterClick = { }
            )
        }

        composeTestRule
            .onNodeWithText("Factura Luz")
            .performClick()

        assert(clicked)
    }

    @Test
    fun givenPaidBill_whenDisplayed_thenShowPagadaStatus() {

        val bill = fakeBill()

        // estado pagada
        composeTestRule.setContent {
            IberdrolaBillsScreen(
                bills = listOf(bill),
                lastBill = bill,
                isLoading = false,
                onclick = {},
                onFilterClick = { }
            )
        }

        composeTestRule
            .onNodeWithTag("bill_status_"+BillStatusEnum.PAGADA.title, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun givenUnpaidBill_whenDisplayed_thenShowPendienteStatus() {

        val bill = fakeBill().copy(statusId = BillStatusEnum.PENDIENTE.ordinal)

        // estado pendiente
        composeTestRule.setContent {
            IberdrolaBillsScreen(
                bills = listOf(bill),
                lastBill = bill,
                isLoading = false,
                onclick = {},
                onFilterClick = { }
            )
        }

        composeTestRule
            .onNodeWithTag("bill_status_"+BillStatusEnum.PENDIENTE.title, useUnmergedTree = true)
            .assertIsDisplayed()
    }
}