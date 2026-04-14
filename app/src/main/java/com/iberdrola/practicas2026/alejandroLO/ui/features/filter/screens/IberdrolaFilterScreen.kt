package com.iberdrola.practicas2026.alejandroLO.ui.features.filter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaBar
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IberdrolaFilterScreen(
    onBack: () -> Unit = {},
    onApply: () -> Unit = {},
    onClear: () -> Unit = {},
    filterViewModel: FilterViewModel = viewModel()
) {
    val filterUiState by filterViewModel.uiState.collectAsState()

    val selectedDateFrom = filterUiState.selectedDateFrom
    val selectedDateTo = filterUiState.selectedDateTo
    val priceRange = filterUiState.priceRange
    val selectedStates = filterUiState.selectedStates

    var showDatePickerFrom by remember { mutableStateOf(false) }
    var showDatePickerTo by remember { mutableStateOf(false) }

    val setDatePickerFrom: (Boolean) -> Unit = {
        showDatePickerFrom = it
    }

    val setDatePickerTo: (Boolean) -> Unit = {
        showDatePickerTo = it
    }

    if (showDatePickerFrom) {
        IberdrolaDatePickerDialog(
            onDateSelected = { filterViewModel.updateDateFrom(it) },
            onDismiss = { setDatePickerFrom(false) }
        )
    }

    if (showDatePickerTo) {
        IberdrolaDatePickerDialog(
            onDateSelected = { filterViewModel.updateDateTo(it) },
            onDismiss = { setDatePickerTo(false) }
        )
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .padding(WindowInsets.statusBars.asPaddingValues())
            ){
                IberdrolaBar(
                    onBackButtonClick = onBack
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .windowInsetsPadding(WindowInsets.navigationBars)                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onApply,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IberdrolaTheme.colors.primary),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Aplicar filtros", style = IberdrolaTheme.typography.tituloMedio, color = Color.White)
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    "Borrar filtros",
                    color = IberdrolaTheme.colors.primary,
                    style = IberdrolaTheme.typography.cuerpoMedio.copy(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { 
                        filterViewModel.clearFilters()
                        onClear()
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Filtrar",
                style = IberdrolaTheme.typography.tituloGrande
            )

            Spacer(Modifier.height(24.dp))

            // Sección: Por fecha
            SectionTitle("Por fecha")
            Row(modifier = Modifier.fillMaxWidth()) {
                DatePickerField(
                    label = "Desde",
                    value = selectedDateFrom,
                    modifier = Modifier.weight(1f),
                    onClick = { setDatePickerFrom(true) }
                )
                Spacer(Modifier.width(24.dp))
                DatePickerField(
                    label = "Hasta",
                    value = selectedDateTo,
                    modifier = Modifier.weight(1f),
                    onClick = { setDatePickerTo(true) }
                )
            }

            Spacer(Modifier.height(40.dp))

            // Sección: Por un importe
            SectionTitle("Por un importe")
            PriceRangeSelector(
                range = priceRange,
                onRangeChange = { filterViewModel.updatePriceRange(it) }
            )

            Spacer(Modifier.height(40.dp))

            // Sección: Por estado
            SectionTitle("Por estado")

            BillStatusEnum.entries.forEach { state ->
                FilterCheckboxItem(
                    label = state.title,
                    isSelected = selectedStates.contains(state),
                    onClick = {
                        if (selectedStates.contains(state)) {
                            filterViewModel.removeState(state)
                        } else {
                            filterViewModel.addState(state)
                        }
                    }
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = IberdrolaTheme.typography.tituloPeque,
        fontWeight = FontWeight.Bold,
        color = IberdrolaTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun DatePickerField(label: String, value: Date?, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Column(modifier = modifier.clickable { onClick() }) {
        Text(
            text = "* $label",
            style = IberdrolaTheme.typography.etiquetaPeque,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .padding(bottom = 8.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val dateFormat = SimpleDateFormat("dd MMM. yyyy", Locale.forLanguageTag("es-ES"))

            if(value == null){
                Text(
                    text = label,
                    style = IberdrolaTheme.typography.cuerpoMedio,
                    color = Color.LightGray
                )
            }else {
                Text(
                    text = dateFormat.format(value),
                    style = IberdrolaTheme.typography.cuerpoMedio,
                    color = Color.Black
                )
            }
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IberdrolaDatePickerDialog(
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    val date = Date(it)
                    onDateSelected(date)
                }
                onDismiss()
            }) {
                Text("OK", color = IberdrolaTheme.colors.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = IberdrolaTheme.colors.onSurfaceVariant)
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRangeSelector(
    range: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    val minLimit = 15f
    val maxLimit = 151f

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = Color(0xFFE5EEE9),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = "${range.start.toInt()} € - ${range.endInclusive.toInt()} €",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = IberdrolaTheme.typography.etiquetaPeque.copy(fontWeight = FontWeight.ExtraBold),
                color = Color.DarkGray
            )
        }

        RangeSlider(
            value = range,
            onValueChange = { onRangeChange(it) },
            valueRange = minLimit..maxLimit,
            modifier = Modifier.fillMaxWidth(),
            startThumb = {
                Surface(
                    modifier = Modifier.size(20.dp),
                    shape = CircleShape,
                    color = IberdrolaTheme.colors.primary
                ) {}
            },
            endThumb = {
                Surface(
                    modifier = Modifier.size(20.dp),
                    shape = CircleShape,
                    color = IberdrolaTheme.colors.primary
                ) {}
            },
            track = { rangeSliderState ->
                SliderDefaults.Track(
                    rangeSliderState = rangeSliderState,
                    modifier = Modifier.height(6.dp),
                    thumbTrackGapSize = 0.dp,
                    colors = SliderDefaults.colors(
                        activeTrackColor = IberdrolaTheme.colors.primary,
                        inactiveTrackColor = Color.LightGray.copy(alpha = 0.3f)
                    )
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${minLimit.toInt()} €", style = IberdrolaTheme.typography.etiquetaPeque, color = Color.Gray)
            Text("${maxLimit.toInt()} €", style = IberdrolaTheme.typography.etiquetaPeque, color = Color.Gray)
        }
    }
}

@Composable
fun FilterCheckboxItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .border(
                    width = 2.dp,
                    color = IberdrolaTheme.colors.primary,
                    shape = RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = IberdrolaTheme.colors.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        Text(
            text = label,
            style = IberdrolaTheme.typography.cuerpoMedio,
            color = Color.Black
        )
    }
}

@Preview
@Composable
fun FilterScreenPreview() {
    IB2026AlejandroLOTheme {
        IberdrolaFilterScreen(
            onBack = { },
            onApply = { },
            onClear = { },
            filterViewModel = viewModel()
        )
    }
}
