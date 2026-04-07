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
import org.junit.Rule
import org.junit.Test

class IberdrolaHomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setupNavController(): TestNavHostController {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        return TestNavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
        }
    }

    @Test
    fun givenCounterFlow_whenNavigateBackWithAtras_thenBottomSheetIsVisible() {
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

        composeTestRule.onNodeWithTag("bottom_sheet").assertIsDisplayed()
    }

//    @Test
//    fun givenCounterFlow_whenNavigateBackWithBackButton_thenBottomSheetIsVisible() {
//        val navController = setupNavController()
//        val dispatcherOwner = TestDispatcherOwner()
//
//        // avanzamos el lifecycle para que el navController funcione
//        dispatcherOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
//        dispatcherOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
//
//        composeTestRule.setContent {
//            CompositionLocalProvider(
//                LocalOnBackPressedDispatcherOwner provides dispatcherOwner,
//                LocalLifecycleOwner provides dispatcherOwner
//            ) {
//                IberdrolaNavGraph(
//                    navController = navController,
//                    innerPadding = PaddingValues()
//                )
//            }
//        }
//
//        composeTestRule
//            .onAllNodesWithTag("home_address_item")[0]
//            .performClick()
//
//        composeTestRule.runOnUiThread {
//            dispatcherOwner.backDispatcher.onBackPressed()
//        }
//
//        composeTestRule.onNodeWithTag("bottom_sheet").assertIsDisplayed()
//    }
}