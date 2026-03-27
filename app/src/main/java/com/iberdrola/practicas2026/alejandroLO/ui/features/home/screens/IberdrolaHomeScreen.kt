//package com.iberdrola.practicas2026.alejandroLO.ui.features.home.screens
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun IberdrolaHomeScreen(onAddressClick: () -> Unit = {}) {
//    val listaSuministros = listOf(
//        Suministro("C/ PALMA - ARTA KM 49, 5", "4ºA - PINTO (MADRID)", "CUPS: ES00021..."),
//        Suministro("AV. DE LA ALBUFERA 12", "1ºC - MADRID", "CUPS: ES00034..."),
//        Suministro("PASEO DE LA CASTELLANA 250", "PLANTA 12 - MADRID", "CUPS: ES00056...")
//    )
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        // Cabecera estilo Iberdrola
//        IberdrolaHomeHeader()
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = "Selecciona un punto de suministro",
//            style = MaterialTheme.typography.titleMedium,
//            color = Color.Gray,
//            modifier = Modifier.padding(horizontal = 16.dp)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Listado de direcciones
//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            contentPadding = PaddingValues(bottom = 16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items(listaSuministros) { suministro ->
//                SuministroItem(suministro, onAddressClick)
//            }
//        }
//    }
//}
//
//@Composable
//fun IberdrolaHomeHeader() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                imageVector = Icons.Default.Person,
//                contentDescription = null,
//                tint = Color(0xFF00833E),
//                modifier = Modifier.size(32.dp)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = "Hola, Alejandro",
//                style = MaterialTheme.typography.headlineSmall,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFF1A1A1A)
//            )
//        }
//
//        Text(
//            text = "Gestiona tus contratos y facturas",
//            style = MaterialTheme.typography.bodyLarge,
//            color = Color.Gray
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
//    }
//}
//
//@Composable
//fun SuministroItem(suministro: Suministro, onClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp)
//            .clickable { onClick() },
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        border = BorderStroke(1.dp, Color(0xFFE0E6E2))
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Icono circular verde suave
//            Surface(
//                color = Color(0xFFD4EBD0),
//                shape = RoundedCornerShape(12.dp),
//                modifier = Modifier.size(48.dp)
//            ) {
//                Box(contentAlignment = Alignment.Center) {
//                    Icon(
//                        imageVector = Icons.Default.Home,
//                        contentDescription = null,
//                        tint = Color(0xFF00833E),
//                        modifier = Modifier.size(24.dp)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text = suministro.direccion,
//                    fontWeight = FontWeight.ExtraBold,
//                    fontSize = 16.sp,
//                    color = Color(0xFF1A1A1A)
//                )
//                Text(
//                    text = suministro.ciudad,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.Gray
//                )
//                Text(
//                    text = suministro.cups,
//                    fontSize = 12.sp,
//                    color = Color(0xFF00833E),
//                    fontWeight = FontWeight.SemiBold
//                )
//            }
//
//            Icon(
//                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
//                contentDescription = null,
//                tint = Color.LightGray
//            )
//        }
//    }
//}
//
//@Composable
//@Preview(showBackground = true)
//fun PreviewIberdrolaHomeScreen() {
//    IberdrolaHomeScreen()
//}