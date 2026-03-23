package com.iberdrola.practicas2026.alejandroLO.ui.features.bills

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import java.text.SimpleDateFormat
import java.util.Locale


// daremos la lista de bill ordenadas por fecha o pasamos por parametro la ultima factura=
@Composable
fun IberdrolaBillsScreen(
    bills: List<Bill>,
    lastBill: Bill,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        IberdrolaLastBill(lastBill = lastBill)
        IberdrolaBillList(bills = bills)
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
        border = BorderStroke(1.dp,
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
                        text = "Factura ${lastBill.type}", // Ej: "Factura Luz"
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                // Icono de bombilla (Luz)
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFF004D3F) // Verde oscuro
                )
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
                color = Color(0xFFEEEEEE)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Badge de Estado (Pendiente de pago)
            val isPaid = lastBill.status == BillStatus.PAGADA.title // Lógica de estado

            Surface(
                color = if (isPaid) Color(0xFFD4EBD0) else Color(0xFFF2B3BA),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = lastBill.status.toString(),
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
fun IberdrolaBillList(bills: List<Bill>) {
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

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (bills.isNotEmpty()) yearFormat.format(bills.first().date) else "ERROR",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de facturas
            // Usamos Column si los datos son pocos, o LazyColumn si es una lista larga.
            // Aquí lo haré dentro de la Column principal para simplificar el scroll.
            bills.forEach { bill ->
                IberdrolaBillItem(bill = bill)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0)
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun IberdrolaBillItem(bill: Bill) {
    val isPaid = bill.status == BillStatus.PAGADA.title
    val dateFormat = SimpleDateFormat("d 'de' MMMM", Locale("es", "ES"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* TODO: Ver detalle */ },
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
                text = "Factura ${bill.type.lowercase().replaceFirstChar { it.uppercase() }}",
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

@Composable
@Preview
fun PreviewIberdrolaBillsScreen() {
    val bill = Bill(
        type = BillType.LUZ.title,
        price = 100.0,
        status = BillStatus.PENDIENTE.title,
        date = Date(),
        dueDate = Date()
    )
    val bills = listOf(bill, bill, bill)

    IberdrolaBillsScreen(bills = bills, lastBill = bill)
}