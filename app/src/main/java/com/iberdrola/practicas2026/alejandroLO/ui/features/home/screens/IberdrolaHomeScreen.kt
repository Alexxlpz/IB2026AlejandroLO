package com.iberdrola.practicas2026.alejandroLO.ui.features.home.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel.HomeUiState

@Composable
fun IberdrolaHomeScreen(onAddressClick: () -> Unit = {}) {
    val listaSuministros = listOf(
        HomeUiState("C/ PALMA - ARTA KM 49, 5", "4ºA - PINTO (MADRID)"),
        HomeUiState("AV. DE LA ALBUFERA 12", "1ºC - MADRID"),
        HomeUiState("PASEO DE LA CASTELLANA 250", "PLANTA 12 - MADRID")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        IberdrolaHomeHeader()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.selecciona_un_punto_de_suministro),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
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
}

@Composable
fun IberdrolaHomeHeader() {
    Surface(
        color = Color(0xFF00833E),
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
        modifier = Modifier.fillMaxWidth()
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
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = stringResource(R.string.hola) + " Alejandro", // TODO HAGO USUARIOS AUNQUE NO HAYA LOGIN????
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.gestiona_tus_contratos_y_facturas),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.8f)
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
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, Color(0xFFE0E6E2))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFFD4EBD0),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = Color(0xFF00833E),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = suministro.direccion,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = suministro.ciudad,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF00833E)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Color(0xFF00833E))
            )
        }
    }
}

@Composable
fun IberdrolaHomeFoot(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F9F9))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFD4EBD0), shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.informacion),
                color = Color(0xFF00833E),
                fontWeight = FontWeight.Black,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.iberdrola_clientes),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF00833E),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(R.string.atencion_al_cliente_24h) + "123 456 789",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .width(140.dp)
                .height(8.dp)
                .background(
                    color = Color(0xFF00833E),
                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewIberdrolaHomeScreen() {
    IberdrolaHomeScreen()
}