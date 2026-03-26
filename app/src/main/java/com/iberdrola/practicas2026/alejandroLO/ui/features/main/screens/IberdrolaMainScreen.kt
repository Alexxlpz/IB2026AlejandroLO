package com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaTopBar
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.screens.IberdrolaBillsScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModelFactory
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.viewModel.MainViewModel

// hay que hacer una UI que almacene selectedOption

@Composable
fun IberdrolaMainScreen(modifier: Modifier = Modifier) {
    val billsViewModel: BillsViewModel = viewModel(factory = BillsViewModelFactory.Factory)
    val mainViewModel: MainViewModel = viewModel()
    val mainUiState = mainViewModel.uiState.collectAsState()

    val billsUiState = billsViewModel.uiState.collectAsState()

    var selectedBill by remember { mutableStateOf<Bill?>(null) }
    val selectingBill: (Bill) -> Unit = remember {
        {
            selectedBill = it
        }
    }

//    val bill = Bill(
//        type = BillType.LUZ.title,
//        price = 100.0,
//        status = BillStatus.PENDIENTE.title,
//        date = Date(),
//        dueDate = Date()
//    )
//    val bills = listOf(bill, bill, bill, bill, bill, bill, bill, bill, bill, bill)

    Box() {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            IberdrolaTopBar(
                selectedOption = mainUiState.value.selectedOption,
                options = mainUiState.value.options,
                onOptionSelected = {
                    mainViewModel.updateSelectedOption(it)
                    billsViewModel.updateSelectedOption(it)
                },
                isSyncEnabled = billsUiState.value.isOnline,
                onSyncToggle = { billsViewModel.updateDataBase(it) }
            )

            IberdrolaBillsScreen(
                bills = billsUiState.value.billsList,
                lastBill = billsUiState.value.lastBill,
                isLoading = billsUiState.value.isLoading,
                onclick = { selectingBill(it) },
                modifier = Modifier.weight(1f)
            )

        }
//        AlertOnBillClick(
//            selectedBill = selectedBill,
//            leaveAlert = { selectedBill = null }
//        )
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AlertOnBillClick(selectedBill: Bill?, leaveAlert: () -> Unit){
//    if(selectedBill != null){
//        AlertDialog(
//            onDismissRequest = { leaveAlert() },
//            confirmButton = {
//                OutlinedButton(onClick = { leaveAlert() }) {
//                    Text("Aceptar", color = Color(0xFF00833E))
//                }
//            },
//            title = {
//                Text(
//                    text = "La factura aún no está disponible",
//                    fontWeight = FontWeight.Bold
//                )
//            },
//            text = {
//                Text("Estamos procesando los datos de su factura. Inténtelo más tarde.")
//            }
//        )
//        Log.d("AlertOnBillClick", "estamos en el alert")
//    }
//}

@Composable
@Preview
fun PreviewIberdrolaMainScreen() {
    IberdrolaMainScreen(modifier = Modifier)
}