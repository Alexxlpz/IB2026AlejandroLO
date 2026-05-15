package com.iberdrola.practicas2026.alejandroLO

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.iberdrola.practicas2026.alejandroLO.fakes.FakeAnalyticsRepository
import com.iberdrola.practicas2026.alejandroLO.fakes.FakeConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.fakes.FakeDirectionRepository
import com.iberdrola.practicas2026.alejandroLO.fakes.FakeElectronicBillsRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.screens.IberdrolaHomeScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel.HomeViewModel
import org.junit.Rule
import org.junit.Test

class IberdrolaHomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun createHomeViewModel(): HomeViewModel {
        return HomeViewModel(
            directionRepository = FakeDirectionRepository(),
            connectivityRepository = FakeConnectivityRepository(),
            electronicBillsRepository = FakeElectronicBillsRepository(),
            analyticsRepository = FakeAnalyticsRepository()
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun homeScreen_isDisplayed() {
        composeTestRule.setContent {
            IberdrolaHomeScreen(
                onAddressClick = { _, _ -> },
                setCont = {},
                mostrarSheet = false,
                homeViewModel = createHomeViewModel(),
                changeMode = {}
            )
        }

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("home_screen"), 5000)
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun whenToggleSwitch_thenModeIsChanged() {
        val viewModel = createHomeViewModel()
        composeTestRule.setContent {
            IberdrolaHomeScreen(
                onAddressClick = { _, _ -> },
                setCont = {},
                mostrarSheet = false,
                homeViewModel = viewModel,
                changeMode = {}
            )
        }

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("home_screen"), 5000)

        composeTestRule.onNodeWithTag("home_switch").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_switch").performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allSuministroItems_areClickable() {
        var clicked = false

        composeTestRule.setContent {
            IberdrolaHomeScreen(
                onAddressClick = { _, _ -> clicked = true },
                setCont = {},
                mostrarSheet = false,
                homeViewModel = createHomeViewModel(),
                changeMode = {}
            )
        }

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("home_screen"), 5000)

        composeTestRule
            .onAllNodesWithTag("home_address_item")[0]
            .performClick()

        assert(clicked)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun bottomSheet_isVisible_whenMostrarSheetTrue() {
        composeTestRule.setContent {
            IberdrolaHomeScreen(
                onAddressClick = { _, _ -> },
                setCont = {},
                mostrarSheet = true,
                homeViewModel = createHomeViewModel(),
                changeMode = {}
            )
        }

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("home_screen"), 5000)
        composeTestRule.onNodeWithTag("bottom_sheet").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenHomeScreen_whenIsDisplayed_thenHeaderAndFooterAreDisplayed() {
        composeTestRule.setContent {
            IberdrolaHomeScreen(
                onAddressClick = { _, _ -> },
                setCont = {},
                mostrarSheet = false,
                homeViewModel = createHomeViewModel(),
                changeMode = {}
            )
        }

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("home_screen"), 5000)
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_header").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_footer").assertIsDisplayed()
    }
}