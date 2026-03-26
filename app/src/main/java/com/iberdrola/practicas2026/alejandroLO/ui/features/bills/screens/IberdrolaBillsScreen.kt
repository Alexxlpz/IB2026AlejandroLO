package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import java.util.Date
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.GasMeter
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.iberdrola.practicas2026.alejandroLO.data.model.BillStatus
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import java.text.SimpleDateFormat
import java.util.Locale


// daremos la lista de bill ordenadas por fecha o pasamos por parametro la ultima factura=
@Composable
fun IberdrolaBillsScreen(
    bills: List<Bill>,
    lastBill: Bill?,
    isLoading: Boolean,
    onclick: (Bill) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()


    Box(modifier = modifier.fillMaxWidth()) {

        if (isLoading) {
            val shimmer = rememberShimmer(
                shimmerBounds = ShimmerBounds.Window,
                theme = shimmerTheme()
            )
            SkeletonScreen(modifier = modifier.shimmer(shimmer))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                if (lastBill != null) {
                    IberdrolaLastBill(lastBill = lastBill)
                }
                IberdrolaBillList(bills = bills, onclick = onclick)
            }
        }
    }

}

@Composable
fun IberdrolaLastBill(lastBill: Bill) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp,
            Color(0xFF00833E).copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Última factura",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "Factura ${BillTypeEnum.entries[lastBill.typeId].title}", // Ej: "Factura Luz"
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                if(lastBill.typeId == 0) {
                    // Icono de bombilla (Luz)
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFF004D3F) // Verde oscuro
                    )
                }else if(lastBill.typeId == 1) {
                    // Icono de bombilla (Gas)
                    Icon(
                        imageVector = Icons.Outlined.LocalFireDepartment,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFF004D3F) // Verde oscuro
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Importe
            Text(
                text = "${lastBill.price} €", // Ej: "20,00 €"
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )

            // Rango de fechas
            val dateFormat = SimpleDateFormat("dd MMM. yyyy", Locale("es", "ES"))
            Text(
                text = "${dateFormat.format(lastBill.date)} - ${dateFormat.format(lastBill.dueDate)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Línea divisoria gris claro
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFF9B9696)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Badge de Estado (Pendiente de pago)
            val isPaid = lastBill.statusId == BillStatusEnum.PAGADA.ordinal // Lógica de estado

            Surface(
                color = if (isPaid) Color(0xFFD4EBD0) else Color(0xFFF2B3BA),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = BillStatusEnum.entries[lastBill.statusId].title,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isPaid) Color(0xFF00833E) else Color(0xFFC00000)
                )
            }
        }
    }
}

@Composable
fun IberdrolaBillList(
    bills: List<Bill>,
    onclick: (Bill) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Cabecera del Histórico
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Histórico de facturas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Botón Filtrar
            OutlinedButton(
                onClick = { /* TODO: Filtrar */ },
                border = BorderStroke(1.dp, Color(0xFF00833E)),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00833E)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Filtrar")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val yearFormat = SimpleDateFormat("yyyy", Locale("es", "ES"))
        var auxyear: String = ""

        Column(modifier = Modifier.fillMaxWidth()) {

            /////////////////////////
            /// Liata de facturas ///
            /////////////////////////

            if(bills.isEmpty()){
                Text("No hay facturas")
            }else {
                bills.forEach { bill ->
                    if (yearFormat.format(bill.date) != auxyear){
                        auxyear = yearFormat.format(bill.date)
                        Text(
                            text = auxyear,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    IberdrolaBillItem(bill = bill, onclick = onclick)
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = Color(0xFFE0E0E0)
                    )

                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun IberdrolaBillItem(
    bill: Bill,
    onclick: (Bill) -> Unit
) {
    val isPaid = bill.statusId == BillStatusEnum.PAGADA.ordinal
    val dateFormat = SimpleDateFormat("d 'de' MMMM", Locale("es", "ES"))
    val type: String = BillTypeEnum.entries.find { it.ordinal == bill.typeId }?.title ?: ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onclick(bill) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Información de la factura (Izquierda)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dateFormat.format(bill.date),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Factura ${type.lowercase().replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Badge de estado
            Surface(
                color = if (isPaid) Color(0xFFD4EBD0) else Color(0xFFF2B3BA),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (isPaid) "Pagada" else "Pendiente de Pago",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isPaid) Color(0xFF00833E) else Color(0xFFC00000)
                )
            }
        }

        // Precio e Icono (Derecha)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = String.format("%.2f €", bill.price),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertOnBillClick(showDialog: Boolean, selectedBill: Bill?, leaveAlert: () -> Unit){
    if(showDialog && selectedBill != null){
        BasicAlertDialog(
            onDismissRequest = { leaveAlert() }
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "La factura aún no está disponible",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Estamos procesando los datos de su factura. Inténtelo más tarde.")

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(onClick = { leaveAlert() }) {
                        Text("Aceptar", color = Color(0xFF00833E))
                    }
                }
            }
        }
        Log.d("AlertOnBillClick", "estamos en el alert")
    }
}

@Composable
@Preview
fun PreviewIberdrolaBillsScreen() {
    val bill = Bill(
        typeId = BillTypeEnum.LUZ.ordinal,
        price = 100.0,
        statusId = BillStatusEnum.PENDIENTE.ordinal,
        date = Date(),
        dueDate = Date()
    )
    val bills = listOf(bill, bill, bill)

    IberdrolaBillsScreen(bills = bills, isLoading = false, onclick = {}, lastBill = bill)
}