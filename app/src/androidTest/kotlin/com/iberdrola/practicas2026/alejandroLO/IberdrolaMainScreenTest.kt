package com.iberdrola.practicas2026.alejandroLO

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens.IberdrolaMainScreen
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import org.junit.Rule
import org.junit.Test

class IberdrolaMainScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // -------------------------
    // Función de ayuda para crear el ViewModel necesario en los tests
    // -------------------------
    @Composable
    private fun createBillsViewModel(): BillsViewModel {
        return viewModel(factory = BillsViewModelFactory.Factory)
    }

    // -------------------------
    // mostrar main_screen
    // -------------------------
    @Test
    fun givenMainScreen_whenLoaded_thenIsDisplayed() {
        composeTestRule.setContent {
            IB2026AlejandroLOTheme {
                val billsVm = createBillsViewModel()
                IberdrolaMainScreen(
                    onBackButtonClick = {},
                    onFilterClick = {},
                    onElectronicBillClick = { _, _ -> },
                    billsViewModel = billsVm
                )
            }
        }

        composeTestRule.onNodeWithTag("main_screen").assertIsDisplayed()
    }

    @Test
    fun givenMainScreen_whenBackButtonClicked_thenCallbackIsTriggered() {
        var backClicked = false

        composeTestRule.setContent {
            IB2026AlejandroLOTheme {
                val billsVm = createBillsViewModel()
                IberdrolaMainScreen(
                    onBackButtonClick = { backClicked = true },
                    onFilterClick = {},
                    onElectronicBillClick = { _, _ -> },
                    billsViewModel = billsVm
                )
            }
        }

        composeTestRule.onNodeWithTag("main_back_button").performClick()
        assert(backClicked)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenMainScreen_whenOptionSelected_thenPagerChanges() {
        composeTestRule.setContent {
            IB2026AlejandroLOTheme {
                val billsVm = createBillsViewModel()
                IberdrolaMainScreen(
                    onBackButtonClick = {},
                    onFilterClick = {},
                    onElectronicBillClick = { _, _ -> },
                    billsViewModel = billsVm
                )
            }
        }

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("bills_screen"), 5000)
        composeTestRule.onNodeWithTag("service_option_Gas").performClick()
        composeTestRule.onNodeWithTag("bills_screen").assertIsDisplayed()
    }

    @Test
    fun givenMainScreen_thenRenderedCorrectly() {
        composeTestRule.setContent {
            IB2026AlejandroLOTheme {
                val billsVm = createBillsViewModel()
                IberdrolaMainScreen(
                    onBackButtonClick = {},
                    onFilterClick = {},
                    onElectronicBillClick = { _, _ -> },
                    billsViewModel = billsVm
                )
            }
        }

        // Verificamos que la pantalla principal se renderiza correctamente
        composeTestRule.onNodeWithTag("main_screen").assertIsDisplayed()
    }
}
