package com.iberdrola.practicas2026.alejandroLO

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
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

    @Test
    fun givenHomeScreen_whenAddressClicked_thenNavigateToMainScreen() {
        val navController = setupNavController()

        composeTestRule.setContent {
            IberdrolaNavGraph(
                navController = navController,
                innerPadding = PaddingValues()
            )
        }

        composeTestRule
            .onAllNodesWithTag("home_address_item")[0]
            .performClick()

        assert(navController.currentDestination?.route == IberdrolaScreens.MAIN.title)
    }

    @Test
    fun givenMainScreen_whenBackPressed_thenNavigateBackToHome() {
        val navController = setupNavController()

        composeTestRule.setContent {
            IberdrolaNavGraph(
                navController = navController,
                innerPadding = PaddingValues()
            )
        }

        composeTestRule
            .onAllNodesWithTag("home_address_item")[0]
            .performClick()
        composeTestRule.onNodeWithTag("main_back_button").performClick()

        assert(navController.currentDestination?.route == IberdrolaScreens.HOME.title)
    }

    @Test
    fun givenHomeScreen_whenLoaded_thenIsDisplayed() {
        val navController = setupNavController()

        composeTestRule.setContent {
            IberdrolaNavGraph(
                navController = navController,
                innerPadding = PaddingValues()
            )
        }

        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
    }

    @Test
    fun givenMainScreen_whenNavigated_thenIsDisplayed() {
        val navController = setupNavController()

        composeTestRule.setContent {
            IberdrolaNavGraph(
                navController = navController,
                innerPadding = PaddingValues()
            )
        }

        composeTestRule
            .onAllNodesWithTag("home_address_item")[0]
            .performClick()

        composeTestRule.onNodeWithTag("main_screen").assertIsDisplayed()
    }
}