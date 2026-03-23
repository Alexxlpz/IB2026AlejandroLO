package com.iberdrola.practicas2026.alejandroLO.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun IberdrolaNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = IberdrolaScreens.EJEMPLO.title
) {
}