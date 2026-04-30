package com.iberdrola.practicas2026.alejandroLO.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.alejandroLO.data.model.ElectronicBill
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens.IberdrolaElectronicBillsScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens.IberdrolaFillElectronicBillsScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens.IberdrolaModifyElectronicBillsScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens.IberdrolaModifyEmailElectronicBillScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens.IberdrolaThanksScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens.IberdrolaVerificationEmailElectronicBillsScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.viewModel.ElectronicBillsViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.viewModel.ElectronicBillsViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.screens.IberdrolaFilterScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.screens.IberdrolaHomeScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel.HomeViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel.HomeViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens.IberdrolaMainScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun IberdrolaNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: IberdrolaScreens = IberdrolaScreens.HOME,
    innerPadding: PaddingValues
) {
    val locale = LocalLocale.current.platformLocale
    val TAG = "IberdrolaNavGraph"

    val billsViewModel: BillsViewModel = viewModel(factory = BillsViewModelFactory.Factory)
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory.Factory)
    val filterViewModel: FilterViewModel = viewModel(factory = FilterViewModelFactory.Factory)
    val electronicBillsViewModel: ElectronicBillsViewModel = viewModel(factory = ElectronicBillsViewModelFactory.Factory)
    val electronicBillsUiState = electronicBillsViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

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

    var selectedStreet by remember { mutableStateOf("") }
    val updateSelectedStreet: (String) -> Unit = {
        Log.d(TAG, "updateSelectedStreet: $it")
        selectedStreet = it
    }

    var electronicBills by remember { mutableStateOf(ElectronicBill()) }
    val updateElectronicBills: (Int) -> Unit = { directionId ->
        Log.d(TAG, "updateElectronicBills, directionid: $directionId")
        electronicBills = electronicBillsUiState.value.electronicBills.first { it.directionId == directionId }
    }
    val refreshElectronicBill:(String, BillTypeEnum) -> Unit = { email, type ->
        electronicBills = if(type == BillTypeEnum.LUZ){
            electronicBills.copy(electricityBillEmail = email)
        }else {
            electronicBills.copy(gasBillEmail = email)
        }
    }

    var typeSelected by remember { mutableStateOf(BillTypeEnum.LUZ) }
    val updateSelectedTypeBill: (BillTypeEnum) -> Unit = {
        Log.d(TAG, "updateSelectedTypeBill: $it")
        typeSelected = it
    }

    val onElectronicBillClick: (String, Int) -> Unit = { street, directionId ->
        navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS.title)
        updateSelectedStreet(street)
        updateElectronicBills(directionId)
    }

    var newEmail: String? = null
    val updateNewEmail: (String?) -> Unit = {
        Log.d(TAG, "changeNewEmail: $it")
        newEmail = it
    }

    val onCloseClick: () -> Unit = {
        scope.launch {
            navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS.title)
            delay(1000)
            updateNewEmail(null)
            electronicBillsViewModel.resetCounter()
        }
    }

    var isModificacion by remember { mutableStateOf(false) }
    val updateIsModificacion: (Boolean) -> Unit = {
        Log.d(TAG, "updateIsModificacion: $it")
        isModificacion = it
    }


    NavHost(
        navController = navController,
        startDestination = startDestination.title
    ) {
        composable(IberdrolaScreens.HOME.title) {
            IberdrolaHomeScreen(
                onAddressClick = { id, street ->
                    billsViewModel.updateDirection(
                        directionId = id,
                        directionStreet = street
                    )
                    navController.navigate(IberdrolaScreens.MAIN.title)
                },
                setCont = setCont,
                mostrarSheet = mostrarSheet,
                homeViewModel = homeViewModel,
                clearFilters = {
                    // para reiniciar el filtro antes de cambiar de modo
                    filterViewModel.clearFiltersToChangeMode()
                    billsViewModel.clearFilters(it)
                }
            )
        }
        composable(IberdrolaScreens.MAIN.title) {
            IberdrolaMainScreen(
                locale = locale,
                onBackButtonClick = {
                    Log.d(TAG, "Back button clicked")
                    if (navController.currentBackStackEntry?.destination?.route == IberdrolaScreens.MAIN.title) {
                        decrementarCont()
                        navController.navigate(IberdrolaScreens.HOME.title) {
                            popUpTo(IberdrolaScreens.HOME.title) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
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
                filterViewModel = filterViewModel,
                onElectronicBillClick = onElectronicBillClick
            )
        }
        composable(IberdrolaScreens.FILTER.title) {
            IberdrolaFilterScreen(
                onBack = { navController.navigate(IberdrolaScreens.MAIN.title) },
                filterViewModel = filterViewModel,
                locale = locale
            )
        }
        composable(IberdrolaScreens.ELECTRONIC_BILLS.title) {
            IberdrolaElectronicBillsScreen(
                onBackClick = { navController.navigate(IberdrolaScreens.MAIN.title) },
                onContratoClick = {
                    if (it) {
                        navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_MODIFY.title)
                    } else {
                        navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_FILL.title)
                    }
                },
                updateSelectedTypeBill = updateSelectedTypeBill
            )
        }
        composable(IberdrolaScreens.ELECTRONIC_BILLS_MODIFY.title) {
            IberdrolaModifyElectronicBillsScreen(
                onBackClick = onCloseClick, // todo hacer un backhandler global que gestione el crash de que se vacie la pila y que controle el contador
                onEditEmailClick = { navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_MODIFING_EMAIL.title) },
                selectedStreet = selectedStreet,
                email = if (typeSelected == BillTypeEnum.LUZ) electronicBills.electricityBillEmail!! else electronicBills.gasBillEmail!!
            )
        }
        composable(IberdrolaScreens.ELECTRONIC_BILLS_MODIFING_EMAIL.title) {
            IberdrolaModifyEmailElectronicBillScreen(
                onCloseClick = onCloseClick,
                onBackClick = {
                    navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_MODIFY.title)
                    updateNewEmail(null)
                },
                onNextClick = { newEmail ->
                    navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_VERIFICATION.title)
                    updateNewEmail(newEmail)
                    updateIsModificacion(true)
                },
                email = if (newEmail != null) {
                    newEmail!!
                } else if (typeSelected == BillTypeEnum.LUZ) {
                    electronicBills.electricityBillEmail!!
                } else {
                    electronicBills.gasBillEmail!!
                }
            )
        }
        composable(IberdrolaScreens.ELECTRONIC_BILLS_FILL.title) {
            IberdrolaFillElectronicBillsScreen(
                onBackClick = { navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS.title) },
                onCloseClick = onCloseClick,
                onNextClick = { newEmail ->
                    navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_VERIFICATION.title)
                    updateNewEmail(newEmail)
                    updateIsModificacion(false)
                },
                email = newEmail
            )
        }
        composable(IberdrolaScreens.ELECTRONIC_BILLS_VERIFICATION.title) {
            IberdrolaVerificationEmailElectronicBillsScreen(
                onCloseClick = onCloseClick,
                onBackClick = {
                    if (isModificacion) {
                        navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_MODIFING_EMAIL.title)
                    } else {
                        navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_FILL.title)
                    }
                },
                onNextClick = {
                    navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_THANKS.title)
                    if (isModificacion) {
                        electronicBillsViewModel.updateElectronicBillEmail(
                            email = newEmail!!,
                            type = typeSelected,
                            electronicBill = electronicBills
                        )
                        // simula la recarga sin esperar a room
                        refreshElectronicBill(newEmail!!, typeSelected)
                    }

                    navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_THANKS.title)
                },
                electronicBillsUiState = electronicBillsViewModel.uiState.collectAsState().value,
                updateCounter = { electronicBillsViewModel.updateCounter() }
            )
        }
        composable(IberdrolaScreens.ELECTRONIC_BILLS_THANKS.title) {
            IberdrolaThanksScreen(
                email = newEmail?:"",
                isModificacion = isModificacion,
                onAcceptClick = onCloseClick,
                onCloseClick = onCloseClick
            )
        }
    }
}