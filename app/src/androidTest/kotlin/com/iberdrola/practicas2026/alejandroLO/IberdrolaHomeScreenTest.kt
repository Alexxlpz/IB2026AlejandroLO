package com.iberdrola.practicas2026.alejandroLO

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.iberdrola.practicas2026.alejandroLO.fakes.FakeConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.fakes.FakeDirectionRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.screens.IberdrolaHomeScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel.HomeViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.navigation.IberdrolaNavGraph
import org.junit.Rule
import org.junit.Test

class IberdrolaHomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setupNavController(): TestNavHostController {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return TestNavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
        }
    }

    private fun createHomeViewModel(): HomeViewModel {
        return HomeViewModel(
            directionRepository = FakeDirectionRepository(),
            connectivityRepository = FakeConnectivityRepository()
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenCounterFlow_whenNavigateBackWithAtras_thenBottomSheetIsVisible() {
        val navController = setupNavController()

        composeTestRule.setContent {
            IberdrolaNavGraph(
                navController = navController,
                innerPadding = PaddingValues()
            )
        }

        // Esperamos a que el componente de carga desaparezca y se muestre la home
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("home_screen"), 5000)

        composeTestRule
            .onAllNodesWithTag("home_address_item")[0]
            .performClick()
        composeTestRule.onNodeWithTag("main_back_button").performClick()

        composeTestRule.onNodeWithTag("bottom_sheet").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun homeScreen_isDisplayed() {
        composeTestRule.setContent {
            IberdrolaHomeScreen(
                onAddressClick = { _, _ -> },
                setCont = {},
                mostrarSheet = false,
                homeViewModel = createHomeViewModel()
            )
        }

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("home_screen"), 5000)
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
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
                homeViewModel = createHomeViewModel()
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
                setCont = {  },
                mostrarSheet = true,
                homeViewModel = createHomeViewModel()
            )
        }

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("home_screen"), 5000)
        composeTestRule.onNodeWithTag("bottom_sheet").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenHomeScreen_whenIsDisplayed_thenHeaderIsDisplayed() {
        composeTestRule.setContent {
            IberdrolaHomeScreen(
                onAddressClick = { _, _ -> },
                setCont = {},
                mostrarSheet = false,
                homeViewModel = createHomeViewModel()
            )
        }

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("home_screen"), 5000)
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_header").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_footer").assertIsDisplayed()
    }
}
