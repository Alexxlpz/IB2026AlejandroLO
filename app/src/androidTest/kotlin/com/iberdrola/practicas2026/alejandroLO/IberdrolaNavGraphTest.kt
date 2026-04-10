package com.iberdrola.practicas2026.alejandroLO

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
import com.iberdrola.practicas2026.alejandroLO.ui.navigation.IberdrolaNavGraph
import com.iberdrola.practicas2026.alejandroLO.ui.navigation.IberdrolaScreens
import org.junit.Rule
import org.junit.Test

class IberdrolaNavGraphTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setupNavController(): TestNavHostController {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        return TestNavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenHomeScreen_whenAddressClicked_thenNavigateToMainScreen() {
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

        assert(navController.currentDestination?.route == IberdrolaScreens.MAIN.title)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenMainScreen_whenBackPressed_thenNavigateBackToHome() {
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

        assert(navController.currentDestination?.route == IberdrolaScreens.HOME.title)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenHomeScreen_whenLoaded_thenIsDisplayed() {
        val navController = setupNavController()

        composeTestRule.setContent {
            IberdrolaNavGraph(
                navController = navController,
                innerPadding = PaddingValues()
            )
        }

        // Esperamos a que el componente de carga desaparezca y se muestre la home
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("home_screen"), 5000)
        
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenMainScreen_whenNavigated_thenIsDisplayed() {
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

        composeTestRule.onNodeWithTag("main_screen").assertIsDisplayed()
    }
}
