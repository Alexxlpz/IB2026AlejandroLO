package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaBar
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    onBack: () -> Unit = {},
    onApply: () -> Unit = {},
    onClear: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            IberdrolaBar(
                onBackButtonClick = onBack
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(24.dp),
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
                    modifier = Modifier.clickable { onClear() }
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
                DatePickerField(value = "Desde", modifier = Modifier.weight(1f))
                Spacer(Modifier.width(24.dp))
                DatePickerField(value = "Hasta", modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(40.dp))

            // Sección: Por un importe
            SectionTitle("Por un importe")
            PriceRangeSelector()

            Spacer(Modifier.height(40.dp))

            // Sección: Por estado
            SectionTitle("Por estado")
            val states = listOf("Pagadas", "Pendientes de Pago", "En trámite de cobro", "Anuladas", "Cuota Fija")
            var selectedStates by remember { mutableStateOf(setOf("Pagadas")) }

            states.forEach { state ->
                FilterCheckboxItem(
                    label = state,
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
fun DatePickerField(value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = Color.LightGray,
                        start = androidx.compose.ui.geometry.Offset(0f, size.height),
                        end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .padding(bottom = 8.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = value, style = IberdrolaTheme.typography.cuerpoMedio, color = Color.LightGray)
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
fun PriceRangeSelector() {
    var rangeValues by remember { mutableStateOf(15f..151f) }
    val minLimit = 15f
    val maxLimit = 151f

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Etiqueta del rango (Pill)
        Surface(
            color = Color(0xFFE5EEE9),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = "${rangeValues.start.toInt()} € - ${rangeValues.endInclusive.toInt()} €",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = IberdrolaTheme.typography.etiquetaPeque.copy(fontWeight = FontWeight.ExtraBold),
                color = Color.DarkGray
            )
        }

        RangeSlider(
            value = rangeValues,
            onValueChange = { rangeValues = it },
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
                    thumbTrackGapSize = 0.dp, // Elimina la separación bola-barra
                    colors = SliderDefaults.colors(
                        activeTrackColor = IberdrolaTheme.colors.primary,
                        inactiveTrackColor = Color.LightGray.copy(alpha = 0.3f)
                    )
                )
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
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
        FilterScreen()
    }
}
