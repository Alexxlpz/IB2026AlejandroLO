package com.iberdrola.practicas2026.alejandroLO.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.screens.IberdrolaFilterScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.screens.IberdrolaHomeScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel.HomeViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel.HomeViewModelFactory
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

    var cont by remember { mutableIntStateOf(1) }
    val setCont: (Int) -> Unit = { num ->
        Log.d(TAG, "aumentarCont: cont = $num")
        cont = num
    }
    val mostrarSheet by remember {
        derivedStateOf {
            Log.d(TAG, "Calculando mostrarSheet. cont es: $cont")
            cont == 0
        }
    }
    val decrementarCont: () -> Unit = {
        if (cont > 0) cont--
        Log.d(TAG, "decrementarCont: cont = $cont")
    }

    val billsViewModel: BillsViewModel = viewModel(factory = BillsViewModelFactory.Factory)
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory.Factory)
    val filterViewModel: FilterViewModel = viewModel(factory = FilterViewModelFactory.Factory)


    NavHost(
        navController = navController,
        startDestination = startDestination.title
    ) {
        composable(IberdrolaScreens.HOME.title) {
            IberdrolaHomeScreen(
                onAddressClick = { id, street ->
                    billsViewModel.updateDirection (
                        directionId = id,
                        directionStreet = street
                    )
                    navController.navigate(IberdrolaScreens.MAIN.title)
                },
                setCont = setCont,
                mostrarSheet = mostrarSheet,
                homeViewModel = homeViewModel
            )
        }
        composable(IberdrolaScreens.MAIN.title) {
            IberdrolaMainScreen(
                locale = locale,
                onBackButtonClick = {
                    Log.d(TAG, "Back button clicked")
                    if (navController.currentBackStackEntry?.destination?.route == IberdrolaScreens.MAIN.title) {
                        decrementarCont()
                        navController.navigate(IberdrolaScreens.HOME.title)
                        // no puedo ponerback porque si te da tiempo a pulsar varias
                        // veces antes de que cambie de pantalla llegamos a la base de
                        // la pila de navController
                    }
                },
                modifier = Modifier.padding(innerPadding),
                billsViewModel = billsViewModel,
                onFilterClick = {
                    navController.navigate(IberdrolaScreens.FILTER.title)
                },
                filterViewModel = filterViewModel
            )
        }
        composable(IberdrolaScreens.FILTER.title) {
            IberdrolaFilterScreen(
                onBack = { navController.navigate(IberdrolaScreens.MAIN.title) },
                filterViewModel = filterViewModel
            )
        }
    }
}