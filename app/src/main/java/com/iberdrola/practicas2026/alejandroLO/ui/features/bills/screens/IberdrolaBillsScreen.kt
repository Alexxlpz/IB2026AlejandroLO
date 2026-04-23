package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.filled.Cancel
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
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.enums.FilterType
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.ActiveFilterItem
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterUiState
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.floor

@Composable
fun IberdrolaBillsScreen(
    bills: List<Bill>,
    lastBill: Bill?,
    isLoading: Boolean,
    onclick: (Bill) -> Unit,
    modifier: Modifier = Modifier,
    refresh: () -> Unit = {},
    error: String? = null,
    locale: Locale = Locale.forLanguageTag("es-ES"),
    onFilterClick: () -> Unit,
    filterUiState: FilterUiState,
    clearFilterField: (ActiveFilterItem) -> Unit,
    enableFilterButton: Boolean,
    filterIsApplied: Boolean
) {
    val scrollState = rememberScrollState()
    val numberFormat = NumberFormat.getCurrencyInstance(locale)

    val refreshingState: PullToRefreshState = rememberPullToRefreshState()

//    Log.d("FilterChipList", "FilterChipList: $filterIsApplied")
//    Log.d("FilterChipList", "FilterChipList: $filterUiState")

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
                        IberdrolaLastBill(
                            lastBill = lastBill ,
                            numberFormat = numberFormat
                        )
                    }
                    IberdrolaBillList(
                        bills = bills,
                        onclick = onclick,
                        numberFormat = numberFormat,
                        onFilterClick = onFilterClick,
                        filterUiState = filterUiState,
                        clearFilterField = clearFilterField,
                        filterIsApplied = filterIsApplied,
                        enableFilterButton = enableFilterButton
                    )
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
    val billColor = BillStatusEnum.entries[lastBill.statusId].color
    val billStatus = BillStatusEnum.entries[lastBill.statusId].status

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

            val formattedPrice = numberFormat.format(lastBill.price)
            val euroSymbol = "€"

            val annotatedPrice = buildAnnotatedString {
                if (formattedPrice.contains(euroSymbol)) {
                    // solo si es € lo ponemos mas chiquitito
                    val pricePart = formattedPrice.replace(euroSymbol, "").trim()
                    append(pricePart)

                    pushStyle(
                        SpanStyle(
                            fontSize = IberdrolaTheme.typography.importe.fontSize * 0.65f,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    append(" $euroSymbol")
                    pop()
                } else {
                    append(formattedPrice)
                }
            }

            Text(
                text = annotatedPrice,
                style = IberdrolaTheme.typography.importe,
                color = IberdrolaTheme.colors.onSurface,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            val dateFormat = SimpleDateFormat("dd MMM. yyyy", Locale.forLanguageTag("es-ES"))
            Text(
                text = "${dateFormat.format(lastBill.startDate)} - ${dateFormat.format(lastBill.endDate)}",
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

            Surface(
                color = billStatus,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = BillStatusEnum.entries[lastBill.statusId].title,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = IberdrolaTheme.typography.etiquetaGrande,
                    color = billColor
                )
            }
        }
    }
}

@Composable
fun IberdrolaBillList(
    bills: List<Bill>,
    onclick: (Bill) -> Unit,
    numberFormat: NumberFormat,
    onFilterClick: () -> Unit,
    filterUiState: FilterUiState,
    clearFilterField: (ActiveFilterItem) -> Unit,
    filterIsApplied: Boolean,
    enableFilterButton: Boolean
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
                onClick = onFilterClick,
                border = if(!enableFilterButton){
                    BorderStroke(2.dp, IberdrolaTheme.colors.disableFontColor)
                }else {
                    BorderStroke(2.dp, IberdrolaTheme.colors.primary)
                },
                shape = RoundedCornerShape(20.dp),
                enabled = enableFilterButton,
                colors = if(filterIsApplied){
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = IberdrolaTheme.colors.primary,
                        contentColor = IberdrolaTheme.colors.surfaceVariant,
                    )
                } else {
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = IberdrolaTheme.colors.surfaceVariant,
                        contentColor = IberdrolaTheme.colors.primary,
                        disabledContainerColor = IberdrolaTheme.colors.onSurfaceVariant.copy(alpha = 0.12f),
                        disabledContentColor = IberdrolaTheme.colors.disableFontColor.copy(alpha = 0.5f)
                    )
                },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                if(filterIsApplied){
                    Text("Filtrar º")
                }else {
                    Text("Filtrar")
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        FilterChipList(
            filterUiState = filterUiState,
            clearFilterField = clearFilterField
        )

        Spacer(modifier = Modifier.height(5.dp))

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
                val currentYear = yearFormat.format(bill.emissionDate)
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
                    thickness = 2.dp,
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
    val billStatusTitle = BillStatusEnum.entries[bill.statusId].title
    val billColor = BillStatusEnum.entries[bill.statusId].color
    val billStatus = BillStatusEnum.entries[bill.statusId].status
    val dateFormat = SimpleDateFormat("d 'de' MMMM", Locale.forLanguageTag("es-ES"))
    val type = BillTypeEnum.entries.find { it.ordinal == bill.typeId }?.title ?: ""

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) IberdrolaTheme.colors.onSurface.copy(alpha = 0.08f) else Color.Transparent,
        label = "billItemFocus"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = IberdrolaTheme.colors.onSurface),
                onClick = { onclick(bill) }
            )
            .padding(vertical = 8.dp)
            .testTag("bill_item"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dateFormat.format(bill.emissionDate),
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
                color = billStatus,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("bill_status_$billStatusTitle")
            ) {
                Text(
                    text = billStatusTitle,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    style = IberdrolaTheme.typography.etiquetaPeque,
                    color = billColor
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
fun FilterChipList(
    filterUiState: FilterUiState,
    clearFilterField: (ActiveFilterItem) -> Unit
){
    val dateFormat = SimpleDateFormat("dd MMM. yyyy", Locale.forLanguageTag("es-ES"))
    val activeFilters = remember(filterUiState) {
        mutableListOf<ActiveFilterItem>().apply {
            if (filterUiState.selectedDateFrom != null) add(ActiveFilterItem(FilterType.DATE_FROM, "Desde: ${dateFormat.format(filterUiState.selectedDateFrom)}"))
            if (filterUiState.selectedDateTo != null) add(ActiveFilterItem(FilterType.DATE_TO, "Hasta: ${dateFormat.format(filterUiState.selectedDateTo)}"))
            if (filterUiState.priceRange != filterUiState.minPrice..filterUiState.maxPrice){
                // para redondear hacia abajo
                val maxPrice = floor(filterUiState.priceRange.endInclusive)
                // para redondear hacia abajo
                val minPrice = floor(filterUiState.priceRange.start)
                add(ActiveFilterItem(FilterType.PRICE_RANGE, "Precio: $minPrice-$maxPrice"))
            }
            if (filterUiState.selectedStates != BillStatusEnum.entries && filterUiState.selectedStates.isNotEmpty()){
                BillStatusEnum.entries.forEach {
                    if(filterUiState.selectedStates.contains(it)){
                        add(ActiveFilterItem(FilterType.STATUS, it.title))
                    }
                }
            }
        }
    }

    if (activeFilters.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            activeFilters.forEach { activeFilterItem ->
                FilterChip(
                    text = activeFilterItem.label,
                    onRemove = { clearFilterField(activeFilterItem) }
                )
            }
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    onRemove: () -> Unit
) {
    Surface(
        modifier = Modifier.padding(end = 10.dp),
        color = IberdrolaTheme.colors.successContainer, // Verde muy suave de Iberdrola
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, IberdrolaTheme.colors.primary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = IberdrolaTheme.typography.cuerpoPeque,
                color = IberdrolaTheme.colors.primary
            )
            Spacer(Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = "Quitar filtro",
                modifier = Modifier
                    .size(18.dp)
                    .clickable { onRemove() },
                tint = IberdrolaTheme.colors.primary
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
        emissionDate = Date()
    )
    val bills = listOf(bill, bill, bill)
    IB2026AlejandroLOTheme {
        IberdrolaBillsScreen(
            bills = bills,
            isLoading = false,
            onclick = {},
            lastBill = bill,
            locale = Locale.forLanguageTag("es-ES"),
            onFilterClick = {},
            filterUiState = FilterUiState(),
            clearFilterField = {},
            enableFilterButton = true,
            filterIsApplied = false
        )
    }
}