package com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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
    onNextClick: (String) -> Unit,
    email: String?
) {
    var email by remember { mutableStateOf(email?:"") }
    var acceptedTerms by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }

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
                progressStart = 0f,
                progressEnd = 0.5f,
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

                val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                val isError = (email.isNotEmpty() && !isEmailValid)


                TextField(
                    value = email,
                    onValueChange = { email = it },
                    isError = isError,
                    label = {
                        Text(
                            text = stringResource(id = R.string.new_email_label),
                            style = IberdrolaTheme.typography.tituloPeque
                        )
                    },
                    supportingText = {
                        if (isError) {
                            Text(
                                text = "Introduce un formato de email válido (ejemplo@dominio.com)",
                                style = IberdrolaTheme.typography.etiquetaPeque,
                                color = Color.Red
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = IberdrolaTheme.typography.cuerpoMedio.copy(fontSize = 18.sp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = IberdrolaTheme.colors.primary,
                        unfocusedIndicatorColor = IberdrolaTheme.colors.onSurface,
                        errorIndicatorColor = Color.Red,
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        errorLabelColor = Color.Red
                    )
                )

                val correctEmail = email.isNotEmpty() && isEmailValid

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(id = R.string.info_proteccion_datos_titulo),
                    style = IberdrolaTheme.typography.tituloGrande.copy(fontWeight = FontWeight.ExtraBold),
                    color = IberdrolaTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                val onMoreInfoClick = { tag: String ->
                    dialogTitle = tag
                    dialogMessage = when (tag) {
                        "Responsable" -> "Iberdrola Clientes S.A.U. con domicilio social en Bilbao, Plaza Euskadi número 5. Para más detalles puede contactar con nuestro Delegado de Protección de Datos."
                        "Finalidad" -> "Tratamos sus datos para gestionar el alta en la factura electrónica, así como el envío de comunicaciones comerciales si así lo ha consentido."
                        "Derechos" -> "Podrá ejercitar sus derechos de acceso, rectificación, supresión, limitación del tratamiento, portabilidad de datos u oposición en cualquier momento a través de nuestros canales oficiales."
                        else -> ""
                    }
                    showDialog = true
                }

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    InfoLegalLine(
                        label = "Responsable:",
                        content = " Iberdrola Clientes S.A.U. ",
                        onLinkClick = { onMoreInfoClick("Responsable") }
                    )

                    InfoLegalLine(
                        label = "Finalidad:",
                        content = " Gestión de la factura electrónica. ",
                        onLinkClick = { onMoreInfoClick("Finalidad") }
                    )

                    InfoLegalLine(
                        label = "Derechos:",
                        content = " Acceso, rectificación, supresión, limitación del tratamiento, portabilidad de datos u oposición, incluida la oposición a decisiones individuales automatizadas. ",
                        onLinkClick = { onMoreInfoClick("Derechos") }
                    )
                }

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
                val isNextEnabled = acceptedTerms && correctEmail

                IberdrolaNextBackButtons(
                    isNextEnabled = isNextEnabled,
                    onBackClick = onBackClick,
                    onNextClick = { onNextClick(email) }
                )
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text(text = "Entendido", color = IberdrolaTheme.colors.primary)
                    }
                },
                title = {
                    Text(
                        text = dialogTitle,
                        style = IberdrolaTheme.typography.tituloMedio,
                        color = IberdrolaTheme.colors.onSurface
                    )
                },
                text = {
                    Text(
                        text = dialogMessage,
                        style = IberdrolaTheme.typography.cuerpoMedio,
                        color = IberdrolaTheme.colors.onSurfaceVariant
                    )
                },
                containerColor = IberdrolaTheme.colors.background,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
fun InfoLegalLine(
    label: String,
    content: String,
    onLinkClick: () -> Unit
) {
    val annotatedString = buildAnnotatedString {
        append(label)

        append(content)

        pushStringAnnotation(tag = "more_info", annotation = "more_info")
        withStyle(
            style = SpanStyle(
                color = IberdrolaTheme.colors.primary,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(stringResource(id = R.string.mas_info))
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        style = IberdrolaTheme.typography.cuerpoMedio,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "more_info", start = offset, end = offset)
                .firstOrNull()?.let {
                    onLinkClick()
                }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewIberdrolaFillElectronicBillsScreen() {
    IberdrolaFillElectronicBillsScreen(
        onCloseClick = {},
        onBackClick = {},
        onNextClick = {},
        email = null
    )
}
