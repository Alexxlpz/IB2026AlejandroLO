package com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaNextBackButtons
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.VerificationHeader
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IberdrolaModifyEmailElectronicBillScreen(
    onCloseClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    var newEmail by remember { mutableStateOf("") }

    BackHandler(
        onBack = onCloseClick
    )

    Box(modifier = Modifier.fillMaxSize()) {
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
                    title = stringResource(R.string.modificar_email),
                    progress = 0.50f,
                    onCloseClick = onCloseClick
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.en_que_email_deseas_recibir_facturas),
                        style = IberdrolaTheme.typography.tituloGrande,
                        color = IberdrolaTheme.colors.onSurface
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    TextField(
                        value = newEmail,
                        onValueChange = { newEmail = it },
                        label = {
                            Text(
                                text = stringResource(id = R.string.new_email_label),
                                style = IberdrolaTheme.typography.tituloPeque
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = IberdrolaTheme.typography.cuerpoMedio.copy(fontSize = 18.sp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = IberdrolaTheme.colors.primary,
                            unfocusedIndicatorColor = IberdrolaTheme.colors.onSurface,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray
                        )
                    )
                    val isNextEnabled = newEmail.isNotEmpty()

                    Spacer(modifier = Modifier.weight(1f))

                    IberdrolaNextBackButtons(
                        isNextEnabled = isNextEnabled,
                        onBackClick = onBackClick,
                        onNextClick = onNextClick
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewIberdrolaModifyEmailElectronicBillScreen() {
    IberdrolaModifyEmailElectronicBillScreen(
        onCloseClick = {},
        onBackClick = {},
        onNextClick = {}
    )
}
