package com.iberdrola.practicas2026.alejandroLO

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens.IberdrolaMainScreen
import org.junit.Rule
import org.junit.Test

class IberdrolaMainScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // -------------------------
    // mostrar main_screen
    // -------------------------
    @Test
    fun givenMainScreen_whenLoaded_thenIsDisplayed() {
        composeTestRule.setContent {
            IberdrolaMainScreen(
                onBackButtonClick = {}
            )
        }

        composeTestRule.onNodeWithTag("main_screen").assertIsDisplayed()
    }

    // -------------------------
    // click en botón back del topbar
    // -------------------------
    @Test
    fun givenMainScreen_whenBackButtonClicked_thenCallbackIsTriggered() {
        var backClicked = false

        composeTestRule.setContent {
            IberdrolaMainScreen(
                onBackButtonClick = { backClicked = true }
            )
        }

        composeTestRule.onNodeWithTag("main_back_button").performClick()

        assert(backClicked)
    }

//        // -------------------------
//        // pulsar botón físico back
//        // -------------------------
//    @Test
//    fun givenMainScreen_whenBackHandlerTriggered_thenCallbackIsTriggered() {
//        var backClicked = false
//
//        composeTestRule.setContent {
//            IberdrolaMainScreen(
//                onBackButtonClick = { backClicked = true }
//            )
//        }
//
//        composeTestRule.activityRule.scenario.onActivity {
//            it.onBackPressedDispatcher.onBackPressed()
//        }
//
//        assert(backClicked)
//    }

    // -------------------------
    // cambiar entre tabs (LUZ / GAS)
    // -------------------------
    @Test
    fun givenMainScreen_whenOptionSelected_thenPagerChanges() {
        composeTestRule.setContent {
            IberdrolaMainScreen(
                onBackButtonClick = {}
            )
        }

        while (!composeTestRule.onNodeWithTag("bills_screen").isDisplayed()) {}

        composeTestRule.onNodeWithTag("service_option_Gas").performClick()

        if(composeTestRule.onNodeWithTag("last_bill_item").isDisplayed()){
            composeTestRule.onNodeWithTag("type_1_icon").assertIsDisplayed()

        }
    }

//    // -------------------------
//    // mostrar alert dialog al pulsar factura
//    // -------------------------
//    @Test
//    fun givenMainScreen_whenBillClicked_thenAlertIsShown() {
//        composeTestRule.setContent {
//            IberdrolaMainScreen(
//                onBackButtonClick = {}
//            )
//        }
//
//        while (!composeTestRule.onNodeWithTag("bills_screen").isDisplayed()) {}
//
//        composeTestRule
//            .onAllNodesWithTag("bill_item")[0]
//            .performClick()
//
//        composeTestRule.onNodeWithTag("alert_dialog").assertIsDisplayed()
//    }
}