package com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaNextBackButtons
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.VerificationHeader
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IberdrolaFillElectronicBillsScreen(
    onCloseClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {},
        containerColor = IberdrolaTheme.colors.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            VerificationHeader(
                title = stringResource(R.string.activa_tu_factura_electronica),
                progress = 0.50f,
                onCloseClick = onCloseClick
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.email_vinculado_cuenta),
                    style = IberdrolaTheme.typography.cuerpoPeque,
                    color = IberdrolaTheme.colors.onSurfaceVariant
                )
                Text(
                    text = "a*****a@a.com",
                    style = IberdrolaTheme.typography.cuerpoMedio.copy(fontWeight = FontWeight.Bold),
                    color = IberdrolaTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(id = R.string.en_que_email_deseas_recibir_facturas),
                    style = IberdrolaTheme.typography.tituloMedio.copy(fontSize = 18.sp),
                    color = IberdrolaTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(id = R.string.email_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = IberdrolaTheme.colors.primary,
                        unfocusedIndicatorColor = IberdrolaTheme.colors.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(id = R.string.info_proteccion_datos_titulo),
                    style = IberdrolaTheme.typography.tituloPeque.copy(fontWeight = FontWeight.Bold),
                    color = IberdrolaTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.info_proteccion_datos_cuerpo),
                    style = IberdrolaTheme.typography.cuerpoPeque.copy(fontSize = 12.sp),
                    color = IberdrolaTheme.colors.onSurfaceVariant,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Checkbox(
                        checked = acceptedTerms,
                        onCheckedChange = { acceptedTerms = it },
                        colors = CheckboxDefaults.colors(checkedColor = IberdrolaTheme.colors.primary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.aceptacion_terminos_factura_electronica),
                        style = IberdrolaTheme.typography.cuerpoPeque.copy(fontSize = 12.sp),
                        color = IberdrolaTheme.colors.onSurface,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                val isNextEnabled = acceptedTerms && email.isNotEmpty()

                IberdrolaNextBackButtons(
                    isNextEnabled = isNextEnabled,
                    onBackClick = onBackClick,
                    onNextClick = onNextClick
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewIberdrolaFillElectronicBillsScreen() {
    IberdrolaFillElectronicBillsScreen(
        onCloseClick = {},
        onBackClick = {},
        onNextClick = {}
    )
}
