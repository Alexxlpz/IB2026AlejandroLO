package com.iberdrola.practicas2026.alejandroLO.ui.features.home.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaFeedbackDialog
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel.HomeUiState
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IberdrolaHomeScreen(
    onAddressClick: () -> Unit,
    setCont: (Int) -> Unit,
    mostrarSheet: Boolean = false
) {
    val TAG = "IberdrolaHomeScreen"
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val listaSuministros = listOf(
        HomeUiState("C/ PALMA - ARTA KM 49, 5", "4ºA - PINTO (MADRID)"),
        HomeUiState("AV. DE LA ALBUFERA 12", "1ºC - MADRID"),
        HomeUiState("PASEO DE LA CASTELLANA 250", "PLANTA 12 - MADRID")
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .background(IberdrolaTheme.colors.background)
        .testTag("home_screen")
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            IberdrolaHomeHeader()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.selecciona_un_punto_de_suministro),
                style = IberdrolaTheme.typography.tituloMedio,
                color = IberdrolaTheme.colors.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listaSuministros) { suministro ->
                    SuministroItem(suministro, onAddressClick)
                }
            }
            IberdrolaHomeFoot()
        }

        if (mostrarSheet) {
            Box(){

                IberdrolaFeedbackDialog(
                    sheetState = sheetState,
                    onDismiss = {
                        Log.d(TAG, "IberdrolaHomeScreen: dismiss alert")
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                setCont(1)
                            }
                        }
                    },
                    onAskLater = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            setCont(3)
                        }
                    },
                    onRatingSelected = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            setCont(10)
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun IberdrolaHomeHeader() {
    Surface(
        color = IberdrolaTheme.colors.primary,
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("home_header")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 20.dp, end = 20.dp, bottom = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = IberdrolaTheme.colors.background.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = IberdrolaTheme.colors.background,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = stringResource(R.string.hola) + " Alejandro",
                        style = IberdrolaTheme.typography.tituloGrande, // O un headline si prefieres
                        color = IberdrolaTheme.colors.background
                    )
                    Text(
                        text = stringResource(R.string.gestiona_tus_contratos_y_facturas),
                        style = IberdrolaTheme.typography.cuerpoMedio,
                        color = IberdrolaTheme.colors.background.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun SuministroItem(suministro: HomeUiState, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() }
            .testTag("home_address_item"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = IberdrolaTheme.colors.surface),
        border = BorderStroke(1.5.dp, IberdrolaTheme.colors.border)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = IberdrolaTheme.colors.primaryLight,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = IberdrolaTheme.colors.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = suministro.direccion,
                        style = IberdrolaTheme.typography.cuerpoGrande,
                        color = IberdrolaTheme.colors.onSurface
                    )
                    Text(
                        text = suministro.ciudad,
                        style = IberdrolaTheme.typography.cuerpoMedio,
                        color = IberdrolaTheme.colors.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = IberdrolaTheme.colors.primary
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(IberdrolaTheme.colors.primary)
            )
        }
    }
}

@Composable
fun IberdrolaHomeFoot(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(IberdrolaTheme.colors.surfaceVariant)
            .padding(24.dp)
            .testTag("home_footer"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(IberdrolaTheme.colors.primaryLight, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.informacion),
                color = IberdrolaTheme.colors.primary,
                style = IberdrolaTheme.typography.tituloGrande
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.iberdrola_clientes),
            style = IberdrolaTheme.typography.tituloMedio,
            color = IberdrolaTheme.colors.primary
        )

        Text(
            text = stringResource(R.string.atencion_al_cliente_24h) + "123 456 789",
            style = IberdrolaTheme.typography.cuerpoPeque,
            color = IberdrolaTheme.colors.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .width(140.dp)
                .height(8.dp)
                .background(
                    color = IberdrolaTheme.colors.primary,
                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewIberdrolaHomeScreen() {
    IB2026AlejandroLOTheme {
        IberdrolaHomeScreen(
            onAddressClick = { },
            setCont = { },
            mostrarSheet = false
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewIberdrolaHomeScreenWithAlert() {
    IB2026AlejandroLOTheme {
        IberdrolaHomeScreen(
            onAddressClick = { },
            setCont = { },
            mostrarSheet = true
        )
    }
}
