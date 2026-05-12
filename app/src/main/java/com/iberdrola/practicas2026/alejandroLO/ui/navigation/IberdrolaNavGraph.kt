package com.iberdrola.practicas2026.alejandroLO.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.screens.IberdrolaHomeScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel.HomeViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel.HomeViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens.IberdrolaMainScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun IberdrolaNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: IberdrolaScreens = IberdrolaScreens.HOME,
    innerPadding: PaddingValues,
    locale: Locale
) {
    val TAG = "IberdrolaNavGraph"

    val billsViewModel: BillsViewModel = viewModel(factory = BillsViewModelFactory.Factory)
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory.Factory)
    val electronicBillsViewModel: ElectronicBillsViewModel = viewModel(factory = ElectronicBillsViewModelFactory.Factory)
    val electronicBillsUiState = electronicBillsViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        currentRoute?.let { route ->
            val screenName = IberdrolaScreens.entries
                .firstOrNull { it.title == route }?.name
                ?: route
            homeViewModel.logScreenView(screenName)
        }
    }


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
        if (cont == 0) {
            homeViewModel.logOpenFeedbackSheet(true)
        }
        Log.d(TAG, "decrementarCont: cont = $cont")
    }

    val backStackHandler: (IberdrolaScreens, IberdrolaScreens) -> Unit = { pantallaAct, pantallaDest ->
        if (navController.currentBackStackEntry?.destination?.route == pantallaAct.title) {
            homeViewModel.logButtonClick("volver", pantallaAct.title)
            if(pantallaAct == IberdrolaScreens.MAIN) {
                decrementarCont()
                // no puedo ponerback porque si te da tiempo a pulsar varias
                // veces antes de que cambie de pantalla llegamos a la base de
                // la pila de navController
            }

            navController.navigate(pantallaDest.title) {
                popUpTo(pantallaDest.title) {
                    inclusive = true
                }
            }
        }
    }

    var selectedStreet by remember { mutableStateOf("") }
    val updateSelectedStreet: (String) -> Unit = {
        Log.d(TAG, "updateSelectedStreet: $it")
        selectedStreet = it
    }

    var fromVerification by remember { mutableStateOf(false) }
    val updateFromVerification: (Boolean) -> Unit = {
        fromVerification = it
    }

    var electronicBills: ElectronicBill? by remember { mutableStateOf(ElectronicBill()) }
    val updateElectronicBills: (Int) -> Unit = { directionId ->
        Log.d(TAG, "updateElectronicBills, directionid: $directionId")
        electronicBills = electronicBillsUiState.value.electronicBills.firstOrNull{ it.directionId == directionId }
    }
    val refreshElectronicBill:(String, BillTypeEnum) -> Unit = { email, type ->
        electronicBills = if(type == BillTypeEnum.LUZ){
            electronicBills?.copy(electricityBillEmail = email)
        }else {
            electronicBills?.copy(gasBillEmail = email)
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

    var newEmail by remember { mutableStateOf<String?>(null) }
    val updateNewEmail: (String?) -> Unit = {
        Log.d(TAG, "changeNewEmail: $it")
        newEmail = it
    }

    val onCloseClick: (IberdrolaScreens) -> Unit = { pantallaAct ->
        scope.launch {
            backStackHandler(
                pantallaAct,
                IberdrolaScreens.ELECTRONIC_BILLS
            )
            billsViewModel.reviewIsGasEnabled()
            delay(300)
            updateNewEmail(null)
            //electronicBillsViewModel.resetCounter()
        }
    }

    var isModificacion by remember { mutableStateOf(false) }
    val updateIsModificacion: (Boolean) -> Unit = {
        Log.d(TAG, "updateIsModificacion: $it")
        isModificacion = it
    }

    val isGasEnabled by billsViewModel.isGasEnabled.collectAsState()

    NavHost(
        navController = navController,
        startDestination = startDestination.title
    ) {
        composable(IberdrolaScreens.HOME.title) {
            IberdrolaHomeScreen(
                onAddressClick = { id, street ->
                    homeViewModel.logSelectDirection(street)
                    billsViewModel.updateDirection(
                        directionId = id,
                        directionStreet = street
                    )
                    navController.navigate(IberdrolaScreens.MAIN.title)
                },
                setCont = setCont,
                mostrarSheet = mostrarSheet,
                homeViewModel = homeViewModel,
                changeMode = {
                    // para reiniciar el filtro antes de cambiar de modo
                    homeViewModel.logChangeMode(it)
                    billsViewModel.clearFilters(it)
                }
            )
        }
        composable(IberdrolaScreens.MAIN.title) {
            IberdrolaMainScreen(
                locale = locale,
                onBackButtonClick = {
                    Log.d(TAG, "Back button clicked")
                    backStackHandler(
                        IberdrolaScreens.MAIN,
                        IberdrolaScreens.HOME
                    )
                    scope.launch {
                        delay(500) // para que no se vea la limpieza del filtro
                        billsViewModel.clearFilters() // para reiniciar el filtro al cambiar de calle
                    }
                },
                modifier = Modifier.padding(innerPadding),
                billsViewModel = billsViewModel,
                onFilterClick = {
                    homeViewModel.logButtonClick("filtrar", IberdrolaScreens.MAIN.title)
                    navController.navigate(IberdrolaScreens.FILTER.title)
                },
                onElectronicBillClick = { street, streetId ->
                    billsViewModel.reviewIsGasEnabled()
                    onElectronicBillClick(street, streetId)
                },
                onChangeBillType = { homeViewModel.logChangeBillType(it) },
                onButtonClick = {
                    homeViewModel.logButtonClick(it, IberdrolaScreens.MAIN.title)
                }
            )
        }
        composable(IberdrolaScreens.FILTER.title) {
            IberdrolaFilterScreen(
                onBack = {
                    backStackHandler(
                        IberdrolaScreens.FILTER,
                        IberdrolaScreens.MAIN
                    )
                },
                onFilter = { homeViewModel.logButtonClick("aplicarFiltro", IberdrolaScreens.FILTER.title) },
                onClearFilter = { homeViewModel.logButtonClick("limpiarFiltro", IberdrolaScreens.FILTER.title) },
                billsViewModel = billsViewModel,
                locale = locale
            )
        }
        composable(IberdrolaScreens.ELECTRONIC_BILLS.title) {
            IberdrolaElectronicBillsScreen(
                onBackClick = {
                    backStackHandler(
                        IberdrolaScreens.ELECTRONIC_BILLS,
                        IberdrolaScreens.MAIN
                    )
                },
                onContratoClick = { isModificacion, type ->
                    homeViewModel.logButtonClick(type, IberdrolaScreens.ELECTRONIC_BILLS.title)
                    if (isModificacion) {
                        navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_MODIFY.title)
                    } else {
                        navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_FILL.title)
                    }
                },
                updateSelectedTypeBill = updateSelectedTypeBill,
                electronicBillError = electronicBills == null,
                isGasEnabled = isGasEnabled
            )
        }
        composable(IberdrolaScreens.ELECTRONIC_BILLS_MODIFY.title) {
            val emailToDisplay = if (typeSelected == BillTypeEnum.LUZ) {
                electronicBills?.electricityBillEmail ?: "esteEmailNoExiste@noExiste.com"
            } else {
                electronicBills?.gasBillEmail ?: "esteEmailNoExiste@noExiste.com"
            }
            IberdrolaModifyElectronicBillsScreen(
                onBackClick = {
                    homeViewModel.logButtonClick("volver", IberdrolaScreens.ELECTRONIC_BILLS_MODIFY.title)
                    onCloseClick(IberdrolaScreens.ELECTRONIC_BILLS_MODIFY)
                },
                onEditEmailClick = {
                    homeViewModel.logButtonClick("ver_email_de_factura_activa", IberdrolaScreens.ELECTRONIC_BILLS_MODIFY.title)
                    navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_MODIFING_EMAIL.title)
                    updateNewEmail(emailToDisplay)
                },
                selectedStreet = selectedStreet,
                email = emailToDisplay,
                type = typeSelected
            )
        }
        composable(IberdrolaScreens.ELECTRONIC_BILLS_MODIFING_EMAIL.title) {
            IberdrolaModifyEmailElectronicBillScreen(
                onCloseClick = {
                    homeViewModel.logButtonClick("cerrar", IberdrolaScreens.ELECTRONIC_BILLS_MODIFING_EMAIL.title)
                    onCloseClick(IberdrolaScreens.ELECTRONIC_BILLS_MODIFING_EMAIL)
                },
                onBackClick = {
                    updateFromVerification(false)
                    backStackHandler(
                        IberdrolaScreens.ELECTRONIC_BILLS_MODIFING_EMAIL,
                        IberdrolaScreens.ELECTRONIC_BILLS_MODIFY
                    )
                    updateNewEmail(null)
                },
                onNextClick = { newEmail ->
                    homeViewModel.logButtonClick("modificar_email", IberdrolaScreens.ELECTRONIC_BILLS_MODIFING_EMAIL.title)
                    navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_VERIFICATION.title)
                    updateNewEmail(newEmail)
                    updateIsModificacion(true)
                    updateFromVerification(true)
                },
                email = if (newEmail != null) {
                    newEmail!!
                } else if (typeSelected == BillTypeEnum.LUZ) {
                    electronicBills?.electricityBillEmail!!
                } else if (typeSelected == BillTypeEnum.GAS) {
                    electronicBills?.gasBillEmail!!
                } else {
                    "esteEmailNoExiste@noExiste.com"
                },
                fromVerification = fromVerification
            )
        }
        composable(IberdrolaScreens.ELECTRONIC_BILLS_FILL.title) {
            IberdrolaFillElectronicBillsScreen(
                onBackClick = {
                    homeViewModel.logButtonClick("volver", IberdrolaScreens.ELECTRONIC_BILLS_FILL.title)
                    updateFromVerification(false)
                    onCloseClick(IberdrolaScreens.ELECTRONIC_BILLS_FILL)
                },
                onCloseClick = {
                    homeViewModel.logButtonClick("cerrar", IberdrolaScreens.ELECTRONIC_BILLS_FILL.title)
                    onCloseClick(IberdrolaScreens.ELECTRONIC_BILLS_FILL)
                },
                onNextClick = { newEmail ->
                    homeViewModel.logButtonClick("rellenar_email", IberdrolaScreens.ELECTRONIC_BILLS_FILL.title)
                    navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_VERIFICATION.title)
                    updateNewEmail(newEmail)
                    updateIsModificacion(false)
                    updateFromVerification(true)
                },
                email = newEmail,
                fromVerification = fromVerification
            )
        }
        composable(IberdrolaScreens.ELECTRONIC_BILLS_VERIFICATION.title) {
            IberdrolaVerificationEmailElectronicBillsScreen(
                onCloseClick = {
                    homeViewModel.logButtonClick("cerrar", IberdrolaScreens.ELECTRONIC_BILLS_VERIFICATION.title)
                    updateFromVerification(false)
                    onCloseClick(IberdrolaScreens.ELECTRONIC_BILLS_VERIFICATION)
                },
                onBackClick = {
                    homeViewModel.logButtonClick("volver", IberdrolaScreens.ELECTRONIC_BILLS_VERIFICATION.title)

                    if (isModificacion) {
                        backStackHandler(
                            IberdrolaScreens.ELECTRONIC_BILLS_VERIFICATION,
                            IberdrolaScreens.ELECTRONIC_BILLS_MODIFING_EMAIL
                        )
                    } else {
                        backStackHandler(
                            IberdrolaScreens.ELECTRONIC_BILLS_VERIFICATION,
                            IberdrolaScreens.ELECTRONIC_BILLS_FILL
                        )
                    }
                },
                onNextClick = {
                    updateFromVerification(false)
                    if (isModificacion) {
                        electronicBillsViewModel.updateElectronicBillEmail(
                            email = newEmail!!,
                            type = typeSelected,
                            electronicBill = electronicBills!!
                        )
                        // simula la recarga sin esperar a room
                        refreshElectronicBill(newEmail!!, typeSelected)
                    }

                    homeViewModel.logElectronicBillEmailUpdated(
                        contractType = typeSelected.name,
                        isModification = isModificacion
                    )
                    navController.navigate(IberdrolaScreens.ELECTRONIC_BILLS_THANKS.title)
                },
                electronicBillsUiState = electronicBillsViewModel.uiState.collectAsState().value,
                updateCounter = {

                    electronicBillsViewModel.updateCounterWithAnalyticsPost(
                        {
                            homeViewModel.logVerificationAttempt(
                                typeSelected.name,
                                it
                            )
                        }
                    )
                },
                reviewCoolDown = { electronicBillsViewModel.reviewCooldown() },
                resetTimerUpdate = { electronicBillsViewModel.resetTimerUpdate() }
            )
        }
        composable(IberdrolaScreens.ELECTRONIC_BILLS_THANKS.title) {
            IberdrolaThanksScreen(
                email = newEmail?:"",
                isModificacion = isModificacion,
                onAcceptClick = {
                    homeViewModel.logButtonClick("aceptar", IberdrolaScreens.ELECTRONIC_BILLS_THANKS.title)
                    onCloseClick(IberdrolaScreens.ELECTRONIC_BILLS_THANKS)
                },
                onCloseClick = {
                    homeViewModel.logButtonClick("cerrar", IberdrolaScreens.ELECTRONIC_BILLS_THANKS.title)
                    onCloseClick(IberdrolaScreens.ELECTRONIC_BILLS_THANKS)
                }
            )
        }
    }
}