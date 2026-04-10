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
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.data.model.Direction
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaFeedbackDialog
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.screensimport.IberdrolaHomeLoadingScreen
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel.HomeViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IberdrolaHomeScreen(
    onAddressClick: (Int, String) -> Unit,
    setCont: (Int) -> Unit,
    mostrarSheet: Boolean = false,
    homeViewModel: HomeViewModel
) {
    val TAG = "IberdrolaHomeScreen"
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val homeUiState = homeViewModel.uiState.collectAsState()

    val directionList = homeUiState.value.directionList
    val isLoading = homeUiState.value.isLoading
    val isOnline = homeUiState.value.isOnline
    val error = homeUiState.value.errorMessage

    val localIsOnline = remember(isOnline) { mutableStateOf(isOnline) }
    val showConfirmDialog = remember { mutableStateOf(false) }
    val pendingOnlineValue = remember { mutableStateOf(isOnline) }

    if (showConfirmDialog.value) {
        IberdrolaConfirmDialog(
            onConfirm = {
                showConfirmDialog.value = false
                homeViewModel.updateDirectionsOnline(pendingOnlineValue.value)
            },
            onDismiss = {
                showConfirmDialog.value = false
                localIsOnline.value = isOnline // Revertimos visualmente el switch
            }
        )
    }

    if (isLoading) {
        Log.d(TAG, "IberdrolaHomeScreen: Loading...")
        IberdrolaHomeLoadingScreen()
    } else {
        Box(
            modifier = Modifier
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

                if(error == null) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(directionList) { direccion ->
                            SuministroItem(
                                direccion,
                                { onAddressClick(direccion.id, direccion.street) })
                        }
                    }
                }

                ErrorMessageShowing(error)

                IberdrolaHomeFoot(
                    isOnline = localIsOnline.value,
                    onToggleMode = { newValue ->
                        localIsOnline.value = newValue
                        pendingOnlineValue.value = newValue
                        showConfirmDialog.value = true
                    }
                )
            }

            Log.d(TAG, "IberdrolaHomeScreen: mostrarSheet: $mostrarSheet")
            if (mostrarSheet) {
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
fun SuministroItem(direction: Direction, onClick: () -> Unit) {
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
                    .fillMaxWidth()
                    .height(60.dp),
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
                        text = direction.street,
                        style = IberdrolaTheme.typography.cuerpoGrande,
                        color = IberdrolaTheme.colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = direction.city,
                        style = IberdrolaTheme.typography.cuerpoMedio,
                        color = IberdrolaTheme.colors.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
fun IberdrolaHomeFoot(
    isOnline: Boolean = false,
    onToggleMode: (Boolean) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(IberdrolaTheme.colors.surfaceVariant)
            .padding(24.dp)
            .testTag("home_footer"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isOnline) "Modo Online activo" else "Modo Local activo",
                style = IberdrolaTheme.typography.tituloMedio,
                color = if (isOnline) IberdrolaTheme.colors.primary else IberdrolaTheme.colors.onSurfaceVariant
            )
            Switch(
                checked = isOnline,
                onCheckedChange = { onToggleMode(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = IberdrolaTheme.colors.background,
                    checkedTrackColor = IberdrolaTheme.colors.primary,
                    uncheckedThumbColor = IberdrolaTheme.colors.background,
                    uncheckedTrackColor = IberdrolaTheme.colors.onSurfaceVariant.copy(alpha = 0.5f)
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        IberdrolaTheme.colors.primaryLight,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.informacion),
                    color = IberdrolaTheme.colors.primary,
                    style = IberdrolaTheme.typography.tituloGrande
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = stringResource(R.string.iberdrola_clientes),
                    style = IberdrolaTheme.typography.tituloMedio,
                    color = IberdrolaTheme.colors.primary
                )

                Text(
                    text = stringResource(R.string.atencion_al_cliente_24h) + " 900 225 235",
                    style = IberdrolaTheme.typography.cuerpoPeque,
                    color = IberdrolaTheme.colors.onSurfaceVariant
                )
            }
        }

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
fun IberdrolaConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.confirmar_cambio),
                style = IberdrolaTheme.typography.tituloMedio
            )
        },
        text = {
            Text(
                text = stringResource(R.string.confirmar_cambio_modo_mensaje),
                style = IberdrolaTheme.typography.cuerpoMedio
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm() }
            ) {
                Text(
                    text = stringResource(R.string.aceptar),
                    color = IberdrolaTheme.colors.primary
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(
                    text = stringResource(R.string.cancelar),
                    color = IberdrolaTheme.colors.onSurfaceVariant
                )
            }
        },
        containerColor = IberdrolaTheme.colors.surface,
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
fun ErrorMessageShowing(error: String?){
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

@Composable
@Preview(showBackground = true)
fun PreviewIberdrolaHomeScreen() {
    IB2026AlejandroLOTheme {
        IberdrolaHomeScreen(
            onAddressClick = { _, _ -> },
            setCont = { },
            mostrarSheet = false,
            homeViewModel = viewModel()
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewIberdrolaHomeScreenWithAlert() {
    IB2026AlejandroLOTheme {
        IberdrolaHomeScreen(
            onAddressClick = { _, _ -> },
            setCont = { },
            mostrarSheet = true,
            homeViewModel = viewModel()
        )
    }
}
