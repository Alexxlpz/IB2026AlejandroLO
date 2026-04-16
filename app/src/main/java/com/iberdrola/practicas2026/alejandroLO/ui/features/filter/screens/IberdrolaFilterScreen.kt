package com.iberdrola.practicas2026.alejandroLO.ui.features.filter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.util.Date
import java.util.Locale
import kotlin.collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IberdrolaFilterScreen(
    onBack: () -> Unit = {},
    filterViewModel: FilterViewModel = viewModel()
) {

    val filterUiState = filterViewModel.uiState.collectAsState().value
    val statesToShow = if(filterUiState.selectedStates.containsAll(BillStatusEnum.entries)){
        emptyList()
    }else {
        filterUiState.selectedStates
    }

    var selectedDateFrom by remember(filterUiState.selectedDateFrom) {
        mutableStateOf(filterUiState.selectedDateFrom)
    }
    var selectedDateTo by remember(filterUiState.selectedDateTo) {
        mutableStateOf(filterUiState.selectedDateTo)
    }
    var priceRange by remember(filterUiState.priceRange) {
        mutableStateOf(filterUiState.priceRange)
    }
    var selectedStates by remember(statesToShow) {
        mutableStateOf(statesToShow)
    }

    var showDatePickerFrom by remember { mutableStateOf(false) }
    var showDatePickerTo by remember { mutableStateOf(false) }

    val updateDateFrom: (Date) -> Unit = {
        if(selectedDateTo != null
            && selectedDateTo!! < it){

            selectedDateFrom = selectedDateTo
            selectedDateTo = it

        }else {
            selectedDateFrom = it
        }
    }


    val updateDateTo: (date: Date) -> Unit = {
        if(selectedDateFrom != null
            && selectedDateFrom!! > it){

            selectedDateFrom = it
            selectedDateTo = selectedDateFrom

        }else {
            selectedDateTo = it
        }
    }

    val onClearDate: (Int) -> Unit = {
        when (it) {
            0 -> selectedDateFrom = null
            1 -> selectedDateTo = null
        }
    }

    val setDatePickerFrom: (Boolean) -> Unit = {
        showDatePickerFrom = it
    }

    val setDatePickerTo: (Boolean) -> Unit = {
        showDatePickerTo = it
    }

    if (showDatePickerFrom) {
        IberdrolaDatePickerDialog(
            onDateSelected = { updateDateFrom(it) },
            onDismiss = { setDatePickerFrom(false) }
        )
    }

    if (showDatePickerTo) {
        IberdrolaDatePickerDialog(
            onDateSelected = { updateDateTo(it) },
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
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        filterViewModel.sumbmitButtom(
                            dateFrom = selectedDateFrom,
                            dateTo = selectedDateTo,
                            priceRange = priceRange,
                            selectedStates = selectedStates
                        )
                        onBack()
                    },
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
                        selectedDateFrom = null
                        selectedDateTo = null
                        priceRange = filterUiState.minPrice..filterUiState.maxPrice
                        selectedStates = BillStatusEnum.entries
                        filterViewModel.clearFilters()
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
                    onClick = { setDatePickerFrom(true) },
                    onClearDate = { onClearDate(0) }
                )
                Spacer(Modifier.width(24.dp))
                DatePickerField(
                    label = "Hasta",
                    value = selectedDateTo,
                    modifier = Modifier.weight(1f),
                    onClick = { setDatePickerTo(true) },
                    onClearDate = { onClearDate(1) }
                )
            }

            Spacer(Modifier.height(40.dp))

            // Sección: Por un importe
            SectionTitle("Por un importe")
            PriceRangeSelector(
                range = priceRange,
                maxPrice = filterUiState.maxPrice,
                minPrice = filterUiState.minPrice,
                onRangeChange = { priceRange = it }
            )

            Spacer(Modifier.height(40.dp))

            // Sección: Por estado
            SectionTitle("Por estado")

            BillStatusEnum.entries.forEach { state ->
                FilterCheckboxItem(
                    label = state.title,
                    isSelected = selectedStates.contains(state),
                    onClick = {
                        selectedStates = if (selectedStates.contains(state)) {
                            selectedStates - state
                        } else {
                            selectedStates + state
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
fun DatePickerField(
    label: String,
    value: Date?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onClearDate: () -> Unit
) {
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
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }else {
                Text(
                    text = dateFormat.format(value),
                    style = IberdrolaTheme.typography.cuerpoMedio,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onClearDate() }
                )
            }
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
                Text("CANCELAR", color = IberdrolaTheme.colors.primary)
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
    maxPrice: Float,
    minPrice: Float,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {

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
            valueRange = minPrice..maxPrice,
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
            Text("$minPrice €", style = IberdrolaTheme.typography.etiquetaPeque, color = Color.Gray)
            Text("$maxPrice €", style = IberdrolaTheme.typography.etiquetaPeque, color = Color.Gray)
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
            filterViewModel = viewModel()
        )
    }
}
