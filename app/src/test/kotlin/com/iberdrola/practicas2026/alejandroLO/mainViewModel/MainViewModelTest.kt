package com.iberdrola.practicas2026.alejandroLO.mainViewModel

import com.iberdrola.practicas2026.alejandroLO.MainDispatcherRule
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.viewModel.MainViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel()
    }

    @Test
    fun givenInit_whenInit_thenUiStateContainsAllTypes() { // hay LUZ y GAS al inicio del viewModel
        // Arrange
        // Act
        viewModel.load_options()

        // Assert
        val actualOptions = viewModel.uiState.value.options
        assertTrue(actualOptions.contains(BillTypeEnum.LUZ))
        assertTrue(actualOptions.contains(BillTypeEnum.GAS))

    }

    @Test
    fun givenSelectedOption_whenUpdateSelectedOption_thenUiStateIsUpdated() = runTest {
        // Arrange
        val initialOption = viewModel.uiState.value.selectedOption

        // Act
        viewModel.updateSelectedOption(BillTypeEnum.GAS)
        val updatedOption = viewModel.uiState.value.selectedOption

        // Assert
        assertFalse(initialOption == updatedOption)
        assertTrue(updatedOption == BillTypeEnum.GAS)
    }
}