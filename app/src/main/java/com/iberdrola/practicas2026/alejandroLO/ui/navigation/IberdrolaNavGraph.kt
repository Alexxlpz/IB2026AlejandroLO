package com.iberdrola.practicas2026.alejandroLO.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.screens.IberdrolaHomeScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens.IberdrolaMainScreen
import java.util.Locale

@Composable
fun IberdrolaNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: IberdrolaScreens = IberdrolaScreens.HOME,
    innerPadding: PaddingValues
) {
    val locale = Locale.getDefault()
    val TAG = "IberdrolaNavGraph"
    NavHost(
        navController = navController,
        startDestination = startDestination.title
    ) {
        composable(IberdrolaScreens.HOME.title) {
            IberdrolaHomeScreen(onAddressClick = { navController.navigate(IberdrolaScreens.MAIN.title) })
        }
        composable(IberdrolaScreens.MAIN.title) {
            IberdrolaMainScreen(
                locale = locale,
                onBackButtonClick = {
                    Log.d(TAG, "Back button clicked")
                    navController.popBackStack()
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}