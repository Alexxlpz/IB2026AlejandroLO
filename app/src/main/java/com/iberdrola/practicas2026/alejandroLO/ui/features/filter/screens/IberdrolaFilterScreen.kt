package com.iberdrola.practicas2026.alejandroLO.ui.features.filter.screens

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaBar
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme
import java.text.SimpleDateFormat
import java.util.Calendar.getInstance
import java.util.Date
import java.util.Locale

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
        selectedDateFrom = it
    }


    val updateDateTo: (date: Date) -> Unit = {
        selectedDateTo = it
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
            onDismiss = { setDatePickerFrom(false) },
            minDate = null,
            maxDate = selectedDateTo,
            actual = selectedDateFrom?: selectedDateTo?: Date()
        )
    }

    if (showDatePickerTo) {
        IberdrolaDatePickerDialog(
            onDateSelected = { updateDateTo(it) },
            onDismiss = { setDatePickerTo(false) },
            minDate = selectedDateFrom,
            maxDate = null,
            actual = selectedDateTo?: selectedDateFrom?: Date()
        )
    }

    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .background(IberdrolaTheme.colors.surface)
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
                    .background(IberdrolaTheme.colors.surface)
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
                    Text(stringResource(R.string.aplicar_filtros), style = IberdrolaTheme.typography.tituloMedio, color = Color.White)
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    stringResource(R.string.borrar_filtros),
                    color = IberdrolaTheme.colors.primary,
                    style = IberdrolaTheme.typography.cuerpoMedio.copy(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = ripple(
                                color = IberdrolaTheme.colors.onSurface.copy(alpha = 0.12f)
                            ),
                            onClick = {
                                selectedDateFrom = null
                                selectedDateTo = null
                                priceRange = filterUiState.minPrice..filterUiState.maxPrice
                                selectedStates = emptyList()
                                filterViewModel.clearFilters()
                            }
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(IberdrolaTheme.colors.surface)
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
                onRangeChange = { newRange ->
                    val minGap = 1f

                    if (newRange.endInclusive - newRange.start >= minGap) {
                        priceRange = newRange
                    } else {
                        if (newRange.start != priceRange.start) {

                            priceRange = (newRange.endInclusive - minGap)..newRange.endInclusive

                        } else if (newRange.endInclusive != priceRange.endInclusive) {

                            priceRange = newRange.start..(newRange.start + minGap)

                        }
                    }
                }
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
        val transition = updateTransition(targetState = value != null, label = "LabelTransition")

        val labelOffsetY by transition.animateDp(label = "LabelOffset") { isSelected ->
            if (isSelected) 0.dp else 26.dp
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "* $label",
                style = IberdrolaTheme.typography.etiquetaPeque,
                color = if (value == null) Color.Gray else IberdrolaTheme.colors.primary,
                modifier = Modifier
                    .graphicsLayer {
                        translationY = labelOffsetY.toPx()
                        scaleX = 1.2f
                        scaleY = 1.2f
                        transformOrigin = TransformOrigin(0f, 0f)
                    }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp)
                    .drawBehind {
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val dateFormat = SimpleDateFormat("dd MMM. yyyy", Locale.forLanguageTag("es-ES"))

                if (value == null) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = dateFormat.format(value),
                        style = IberdrolaTheme.typography.cuerpoMedio,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Borrar fecha",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onClearDate() }
                    )
                }
            }
        }
    }
}

@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IberdrolaDatePickerDialog(
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit,
    minDate: Date? = null,
    maxDate: Date? = null,
    actual: Date
) {
    val selectableDates = remember(minDate, maxDate) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // normalizaoms el minimo y el maximo SI NO ES NULO a utc para una mejor
                // comparacion con la fecha seleccionable
                val minTime = minDate?.let {
                    val cal = getInstance(java.util.TimeZone.getTimeZone("UTC"))
                    cal.time = it
                    cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                    cal.set(java.util.Calendar.MINUTE, 0)
                    cal.set(java.util.Calendar.SECOND, 0)
                    cal.set(java.util.Calendar.MILLISECOND, 0)
                    cal.timeInMillis
                }
                val maxTime = maxDate?.let {
                    val cal = getInstance(java.util.TimeZone.getTimeZone("UTC"))
                    cal.time = it
                    cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                    cal.set(java.util.Calendar.MINUTE, 0)
                    cal.set(java.util.Calendar.SECOND, 0)
                    cal.set(java.util.Calendar.MILLISECOND, 0)
                    cal.timeInMillis
                }

                val dateIsAfterMin = minTime?.let { utcTimeMillis >= it } ?: true
                val dateIsBeforeMax = maxTime?.let { utcTimeMillis <= it } ?: true

                return dateIsAfterMin && dateIsBeforeMax
            }

            override fun isSelectableYear(year: Int): Boolean {
                val cal = getInstance()
                val minYear = minDate?.let {
                    cal.time = it
                    cal.get(java.util.Calendar.YEAR)
                } ?: 0
                val maxYear = maxDate?.let {
                    cal.time = it
                    cal.get(java.util.Calendar.YEAR)
                } ?: Int.MAX_VALUE

                return year in minYear..maxYear
            }
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = actual.time,
        selectableDates = selectableDates,
        initialDisplayMode = DisplayMode.Picker
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    onDateSelected(Date(it))
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
    val enabled = (maxPrice != minPrice + 1f)

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
            enabled = enabled,

            startThumb = {
                Surface(
                    modifier = Modifier.size(20.dp),
                    shape = CircleShape,
                    color = if (enabled){
                        IberdrolaTheme.colors.primary
                    }else {
                        IberdrolaTheme.colors.disableFontColor
                    }
                ) {}
            },
            endThumb = {
                Surface(
                    modifier = Modifier.size(20.dp),
                    shape = CircleShape,
                    color = if (enabled){
                        IberdrolaTheme.colors.primary
                    }else {
                        IberdrolaTheme.colors.disableFontColor
                    }
                ) {}
            },
            track = { rangeSliderState ->
                SliderDefaults.Track(
                    rangeSliderState = rangeSliderState,
                    modifier = Modifier.height(6.dp),
                    thumbTrackGapSize = 0.dp,
                    drawStopIndicator = null,
                    enabled = enabled,
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
