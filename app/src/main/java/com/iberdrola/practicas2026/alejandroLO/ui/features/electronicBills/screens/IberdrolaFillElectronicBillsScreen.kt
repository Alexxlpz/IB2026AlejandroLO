package com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens

import android.content.Intent
import android.util.Patterns.EMAIL_ADDRESS
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
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
    email: String?,
    fromVerification: Boolean
) {
    var email by remember { mutableStateOf(email?:"") }
    var acceptedTerms by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }


    val isEmailValid = EMAIL_ADDRESS.matcher(email.trim()).matches()
    val isError = (email.isNotEmpty() && !isEmailValid)

    val progressStart = if (fromVerification) 0.75f else 0f

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val condiciones_generales_url = "https://www.tarifasgasyluz.com/pdf/condiciones_generals.pdf"
    val context = LocalContext.current

    BackHandler(onBack = onBackClick)

    Scaffold(
        topBar = {},
        containerColor = IberdrolaTheme.colors.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            VerificationHeader(
                title = stringResource(R.string.activa_tu_factura_electronica),
                progressStart = progressStart,
                progressEnd = 0.5f,
                onCloseClick = onCloseClick
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = stringResource(id = R.string.email_vinculado_cuenta),
                    style = IberdrolaTheme.typography.cuerpoPeque,
                    color = IberdrolaTheme.colors.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.email_dueño),
                    style = IberdrolaTheme.typography.cuerpoMedio.copy(fontWeight = FontWeight.Bold),
                    color = IberdrolaTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(id = R.string.en_que_email_deseas_recibir_facturas),
                    style = IberdrolaTheme.typography.tituloMedio.copy(fontSize = 18.sp),
                    color = IberdrolaTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(15.dp))

                val interactionSource = remember { MutableInteractionSource() }

                BasicTextField(
                    value = email,
                    onValueChange = { email = it.filter { char -> !char.isWhitespace() } },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = IberdrolaTheme.typography.cuerpoMedio.copy(
                        fontSize = 18.sp,
                        color = IberdrolaTheme.colors.onSurface
                    ),
                    interactionSource = interactionSource,
                    enabled = true,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    decorationBox = { innerTextField ->
                        TextFieldDefaults.DecorationBox(
                            value = email,
                            innerTextField = innerTextField,
                            enabled = true,
                            singleLine = true,
                            visualTransformation = VisualTransformation.None,
                            interactionSource = interactionSource,
                            isError = isError,
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.new_email_label),
                                    style = IberdrolaTheme.typography.tituloPeque
                                )
                            },
                            supportingText = {
                                if (isError) {
                                    Text(
                                        text = stringResource(R.string.email_mal_estructurado),
                                        style = IberdrolaTheme.typography.etiquetaPeque,
                                        color = Color.Red
                                    )
                                }
                            },
                            contentPadding = PaddingValues(
                                start = 0.dp,
                                end = 0.dp,
                                top = 10.dp,
                                bottom = 10.dp
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
                            ),
                            container = {
                                TextFieldDefaults.Container(
                                    enabled = true,
                                    isError = isError,
                                    interactionSource = interactionSource,
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        errorContainerColor = Color.Transparent
                                    ),
                                    shape = RectangleShape,
                                )
                            }
                        )
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(id = R.string.info_proteccion_datos_titulo),
                    style = IberdrolaTheme.typography.tituloGrande.copy(fontWeight = FontWeight.ExtraBold),
                    color = IberdrolaTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    InfoLegalLine(
                        label = "Responsable:",
                        content = " Iberdrola Clientes S.A.U. ",
                        description = "Iberdrola Clientes S.A.U. con domicilio social en Bilbao, Plaza Euskadi número 5. Para más detalles puede contactar con nuestro Delegado de Protección de Datos."
                    )

                    InfoLegalLine(
                        label = "Finalidad:",
                        content = " Gestión de la factura electrónica. ",
                        description = "Tratamos sus datos para gestionar el alta en la factura electrónica, así como el envío de comunicaciones comerciales si así lo ha consentido."
                    )

                    InfoLegalLine(
                        label = "Derechos:",
                        content = " Acceso, rectificación, supresión, limitación del tratamiento, portabilidad de datos y oposición, incluida la oposición a decisiones individuales automatizadas.",
                        description = "Se podrá ejercitar sus derechos en cualquier momento a través de nuestros canales oficiales."
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = acceptedTerms,
                        onCheckedChange = { acceptedTerms = it },
                        modifier = Modifier
                            .offset(y = (-18).dp),
                        colors = CheckboxDefaults.colors(
                            checkedColor = IberdrolaTheme.colors.primary,
                            uncheckedColor = IberdrolaTheme.colors.primary
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))


                    val annotatedText = buildAnnotatedString {
                        append("He leído y acepto la Política de privacidad, acepto las ")

                        pushStringAnnotation(
                            tag = "CONDICIONES",
                            annotation = condiciones_generales_url
                        )
                        withStyle(
                            style = SpanStyle(
                                color = IberdrolaTheme.colors.primary,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Condiciones Generales")
                        }
                        pop()

                        append(" y Particulares de la oferta y la suscripción a Factura Electrónica.")
                    }



                    ClickableText(
                        text = annotatedText,
                        style = IberdrolaTheme.typography.cuerpoMedio.copy(
                            fontSize = 17.sp,
                            color = IberdrolaTheme.colors.onSurface
                        ),
                        onClick = { offset ->
                            annotatedText
                                .getStringAnnotations("CONDICIONES", offset, offset)
                                .firstOrNull()
                                ?.let { annotation ->
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        annotation.item.toUri()
                                    )
                                    context.startActivity(intent)
                                }
                        }
                    )

                }

                Spacer(modifier = Modifier.weight(1f))
            }

            val correctEmail = email.isNotEmpty() && isEmailValid
            val isNextEnabled = acceptedTerms && correctEmail

            IberdrolaNextBackButtons(
                isNextEnabled = isNextEnabled,
                onBackClick = onBackClick,
                onNextClick = { onNextClick(email) }
            )
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
    description: String
) {
    var isExpanded by remember { mutableStateOf(false) }

    val textStyle = IberdrolaTheme.typography.cuerpoMedio.copy(
        fontSize = 16.sp
    )

    val annotatedString = buildAnnotatedString {
        withStyle(
            style = ParagraphStyle(
                lineHeight = textStyle.lineHeight
            )
        ) {
            withStyle(
                style = SpanStyle(
                    fontFamily = textStyle.fontFamily,
                    fontSize = textStyle.fontSize,
                    fontWeight = textStyle.fontWeight,
                    color = Color(0xFF1A1A1A)
                )
            ) {
                append(label)
                append(" $content")

                if (isExpanded) {
                    append(" $description")
                }
            }
        }

        pushStringAnnotation(tag = "expand", annotation = "expand")
        withStyle(
            style = SpanStyle(
                fontFamily = textStyle.fontFamily,
                fontSize = textStyle.fontSize,
                fontWeight = FontWeight.Bold,
                color = IberdrolaTheme.colors.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(if (isExpanded) " Leer menos" else " Más info")
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        style = textStyle.copy(
            color = IberdrolaTheme.colors.onSurfaceVariant
        ),
        onClick = { offset ->
            annotatedString
                .getStringAnnotations("expand", offset, offset)
                .firstOrNull()
                ?.let { isExpanded = !isExpanded }
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
        email = null,
        fromVerification = false
    )
}
