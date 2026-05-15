package com.iberdrola.practicas2026.alejandroLO

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsUiState
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.FilterUiState
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens.IberdrolaMainScreenContent
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import org.junit.Rule
import org.junit.Test
import java.util.Locale

class IberdrolaMainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun defaultContent(
        billsUiState: BillsUiState = BillsUiState(
            directionStreet = "Calle de prueba",
            options = BillTypeEnum.entries.toList()
        ),
        filterUiState: FilterUiState = FilterUiState(),
        isGasEnabled: Boolean = true,
        onBackButtonClick: () -> Unit = {},
        onFilterClick: () -> Unit = {},
        onChangeBillType: (String) -> Unit = {},
        onButtonClick: (String) -> Unit = {}
    ) {
        composeTestRule.setContent {
            IB2026AlejandroLOTheme {
                IberdrolaMainScreenContent(
                    billsUiState = billsUiState,
                    filterUiState = filterUiState,
                    onBackButtonClick = onBackButtonClick,
                    onFilterClick = onFilterClick,
                    onOptionSelected = {},
                    onRefresh = {},
                    onClearFilterField = {},
                    onElectronicBillClick = { _, _ -> },
                    onChangeBillType = onChangeBillType,
                    onButtonClick = onButtonClick,
                    isGasEnabled = isGasEnabled,
                    onPageScrollInitialized = {},
                    locale = Locale.forLanguageTag("es-ES")
                )
            }
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenMainScreen_whenLoaded_thenIsDisplayed() {
        defaultContent()

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("main_screen"), 5000)
        composeTestRule.onNodeWithTag("main_screen").assertIsDisplayed()
    }

    @Test
    fun givenMainScreen_whenBackButtonClicked_thenCallbackIsTriggered() {
        var backClicked = false

        defaultContent(onBackButtonClick = { backClicked = true })

        composeTestRule.onNodeWithTag("main_back_button").performClick()
        assert(backClicked)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenMainScreen_whenGasOptionSelected_thenPagerChanges() {
        defaultContent(isGasEnabled = true)

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("bills_screen"), 5000)
        composeTestRule.onNodeWithTag("service_option_Gas").performClick()
        composeTestRule.onNodeWithTag("bills_screen").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenMainScreen_whenGasDisabled_thenOnlyLuzPageExists() {
        defaultContent(isGasEnabled = false)

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("bills_screen"), 5000)
        composeTestRule.onNodeWithTag("bills_screen").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenMainScreen_whenLoading_thenSkeletonIsDisplayed() {
        defaultContent(
            billsUiState = BillsUiState(
                directionStreet = "Calle de prueba",
                options = BillTypeEnum.entries.toList(),
                isLoading = true
            )
        )

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("bills_skeleton"), 5000)
        composeTestRule.onNodeWithTag("bills_skeleton").assertIsDisplayed()
    }
}