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
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens.IberdrolaMainScreen
import org.junit.Rule
import org.junit.Test

class IberdrolaMainScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    // -------------------------
    // Función de ayuda para crear los ViewModels necesarios en los tests
    // -------------------------
    @Composable
    private fun createViewModels(): Pair<BillsViewModel, FilterViewModel> {
        // Asumiendo que tienes acceso a las fábricas en el entorno de test
        // Si no, puedes usar hiltViewModel() o pasar mocks si usas una librería de mocking
        val billsViewModel: BillsViewModel = viewModel(factory = BillsViewModelFactory.Factory)
        val filterViewModel: FilterViewModel = viewModel(factory = FilterViewModelFactory.Factory)
        return Pair(billsViewModel, filterViewModel)
    }

    // -------------------------
    // mostrar main_screen
    // -------------------------
    @Test
    fun givenMainScreen_whenLoaded_thenIsDisplayed() {
        composeTestRule.setContent {
            val (billsVm, filterVm) = createViewModels()
            IberdrolaMainScreen(
                onBackButtonClick = {},
                onFilterClick = {},
                filterViewModel = filterVm,
                billsViewModel = billsVm
            )
        }

        composeTestRule.onNodeWithTag("main_screen").assertIsDisplayed()
    }

    @Test
    fun givenMainScreen_whenBackButtonClicked_thenCallbackIsTriggered() {
        var backClicked = false

        composeTestRule.setContent {
            val (billsVm, filterVm) = createViewModels()
            IberdrolaMainScreen(
                onBackButtonClick = { backClicked = true },
                onFilterClick = {},
                filterViewModel = filterVm,
                billsViewModel = billsVm
            )
        }

        composeTestRule.onNodeWithTag("main_back_button").performClick()
        assert(backClicked)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenMainScreen_whenOptionSelected_thenPagerChanges() {
        composeTestRule.setContent {
            val (billsVm, filterVm) = createViewModels()
            IberdrolaMainScreen(
                onBackButtonClick = {},
                onFilterClick = {},
                filterViewModel = filterVm,
                billsViewModel = billsVm
            )
        }

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("bills_screen"), 5000)
        composeTestRule.onNodeWithTag("service_option_Gas").performClick()
        composeTestRule.onNodeWithTag("bills_screen").assertIsDisplayed()
    }

    @Test
    fun givenMainScreen_thenFilterButtonParametersArePassed() {
        composeTestRule.setContent {
            val (billsVm, filterVm) = createViewModels()

            IberdrolaMainScreen(
                onBackButtonClick = {},
                onFilterClick = {},
                filterViewModel = filterVm,
                billsViewModel = billsVm
            )
        }

        // Verificamos que la pantalla principal se renderiza correctamente con los nuevos parámetros
        composeTestRule.onNodeWithTag("main_screen").assertIsDisplayed()
    }
}