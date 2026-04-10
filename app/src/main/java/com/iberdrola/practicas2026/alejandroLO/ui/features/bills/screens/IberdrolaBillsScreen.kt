package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IberdrolaBillsScreen(
    bills: List<Bill>,
    lastBill: Bill?,
    isLoading: Boolean,
    onclick: (Bill) -> Unit,
    modifier: Modifier = Modifier,
    refresh: () -> Unit = {},
    error: String? = null,
    locale: Locale = Locale.forLanguageTag("es-ES")
) {
    val scrollState = rememberScrollState()
    val numberFormat = NumberFormat.getCurrencyInstance(locale)

    val refreshingState: PullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox( // para refrescar las facturas
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isLoading,
        onRefresh = refresh,
        state = refreshingState,
        contentAlignment = Alignment.TopStart,
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = refreshingState,
                isRefreshing = isLoading,
                modifier = Modifier.align(Alignment.TopCenter),
                containerColor = IberdrolaTheme.colors.surface,
                color = IberdrolaTheme.colors.primary
            )
        }
    ){
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
                    .testTag("bills_screen")
            ) {
                if(error == null) {
                    if (lastBill != null) {
                        IberdrolaLastBill(lastBill = lastBill, numberFormat = numberFormat)
                    }
                    IberdrolaBillList(bills = bills, onclick = onclick, numberFormat = numberFormat)
                }


                error?.let { message ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .testTag("error_message_surface"),
                        color = IberdrolaTheme.colors.errorContainer,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, IberdrolaTheme.colors.onErrorContainer.copy(alpha = 0.2f)),
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = IberdrolaTheme.colors.onErrorContainer,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = message,
                                style = IberdrolaTheme.typography.cuerpoMedio,
                                color = IberdrolaTheme.colors.onErrorContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IberdrolaLastBill(
    lastBill: Bill,
    numberFormat: NumberFormat
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("last_bill_item"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = IberdrolaTheme.colors.surface),
        border = BorderStroke(2.dp, IberdrolaTheme.colors.primary.copy(alpha = 0.6f))
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
                        text = stringResource(R.string.ultima_factura),
                        style = IberdrolaTheme.typography.tituloGrande,
                        color = IberdrolaTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(
                            R.string.factura,
                            BillTypeEnum.entries[lastBill.typeId].title
                        ),
                        style = IberdrolaTheme.typography.cuerpoMedio,
                        color = IberdrolaTheme.colors.onSurfaceVariant
                    )
                }

                val icon = if (lastBill.typeId == 0) Icons.Outlined.Lightbulb else Icons.Outlined.LocalFireDepartment
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("type_${lastBill.typeId}_icon"),
                    tint = IberdrolaTheme.colors.iconLuzGas
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = numberFormat.format(lastBill.price),
                style = IberdrolaTheme.typography.importe,
                color = IberdrolaTheme.colors.onSurface
            )

            val dateFormat = SimpleDateFormat("dd MMM. yyyy", Locale.forLanguageTag("es-ES"))
            Text(
                text = "${dateFormat.format(lastBill.date)} - ${dateFormat.format(lastBill.dueDate)}",
                style = IberdrolaTheme.typography.cuerpoPeque,
                color = IberdrolaTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = IberdrolaTheme.colors.onSurfaceVariant.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val isPaid = lastBill.statusId == BillStatusEnum.PAGADA.ordinal
            Surface(
                color = if (isPaid) IberdrolaTheme.colors.successContainer else IberdrolaTheme.colors.errorContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = BillStatusEnum.entries[lastBill.statusId].title,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = IberdrolaTheme.typography.etiquetaGrande,
                    color = if (isPaid) IberdrolaTheme.colors.onSuccessContainer else IberdrolaTheme.colors.onErrorContainer
                )
            }
        }
    }
}

@Composable
fun IberdrolaBillList(
    bills: List<Bill>,
    onclick: (Bill) -> Unit,
    numberFormat: NumberFormat
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.historico_de_facturas),
                style = IberdrolaTheme.typography.tituloGrande,
                color = IberdrolaTheme.colors.onSurface
            )

            OutlinedButton(
                onClick = { /* TODO: Filtrar */ },
                border = BorderStroke(2.dp, IberdrolaTheme.colors.primary),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = IberdrolaTheme.colors.primary),
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

        val yearFormat = SimpleDateFormat("yyyy", Locale.forLanguageTag("es-ES"))
        var auxyear = ""

        if (bills.isEmpty()) {
            Text(
                text = stringResource(R.string.no_hay_facturas),
                style = IberdrolaTheme.typography.cuerpoGrande,
                color = IberdrolaTheme.colors.onSurfaceVariant
            )
        } else {
            bills.forEach { bill ->
                val currentYear = yearFormat.format(bill.date)
                if (currentYear != auxyear) {
                    auxyear = currentYear
                    Text(
                        text = auxyear,
                        style = IberdrolaTheme.typography.tituloMedio,
                        color = IberdrolaTheme.colors.onSurface,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                IberdrolaBillItem(bill = bill, onclick = onclick, numberFormat = numberFormat)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = IberdrolaTheme.colors.border.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun IberdrolaBillItem(
    bill: Bill,
    onclick: (Bill) -> Unit,
    numberFormat: NumberFormat
) {
    val billStatus = BillStatusEnum.entries[bill.statusId].title
    val isPaid = bill.statusId == BillStatusEnum.PAGADA.ordinal
    val dateFormat = SimpleDateFormat("d 'de' MMMM", Locale.forLanguageTag("es-ES"))
    val type = BillTypeEnum.entries.find { it.ordinal == bill.typeId }?.title ?: ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onclick(bill) }
            .testTag("bill_item"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dateFormat.format(bill.date),
                style = IberdrolaTheme.typography.cuerpoGrande,
                color = IberdrolaTheme.colors.onSurface
            )
            Text(
                text = stringResource(R.string.factura, type),
                style = IberdrolaTheme.typography.cuerpoMedio,
                color = IberdrolaTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = if (isPaid) IberdrolaTheme.colors.successContainer else IberdrolaTheme.colors.errorContainer,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("bill_status_$billStatus")
            ) {
                Text(
                    text = billStatus,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    style = IberdrolaTheme.typography.etiquetaPeque,
                    color = if (isPaid) IberdrolaTheme.colors.onSuccessContainer else IberdrolaTheme.colors.onErrorContainer
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = numberFormat.format(bill.price),
                style = IberdrolaTheme.typography.importeHistorico,
                color = IberdrolaTheme.colors.importeHistorico
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = IberdrolaTheme.colors.onSurfaceVariant,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewIberdrolaBillsScreen() {
    val bill = Bill(
        typeId = BillTypeEnum.LUZ.ordinal,
        price = 20.0,
        statusId = BillStatusEnum.PENDIENTE.ordinal,
        date = Date(),
        dueDate = Date()
    )
    val bills = listOf(bill, bill, bill)
    IB2026AlejandroLOTheme {
        IberdrolaBillsScreen(bills = bills, isLoading = false, onclick = {}, lastBill = bill)
    }
}
