package com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ThumbDownOffAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaNextBackButtons
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.VerificationHeader
import com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.viewModel.ElectronicBillsUiState
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IberdrolaVerificationEmailElectronicBillsScreen(
    onCloseClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    electronicBillsUiState: ElectronicBillsUiState,
    updateCounter: () -> Unit,
    reviewCoolDown: () -> Unit,
    resetTimerUpdate: () -> Unit
) {
    var verificationCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val counter = electronicBillsUiState.counter

    var finalProgress by remember { mutableFloatStateOf(0.75f) }

    val supressBackStack: (Boolean) -> Unit = {
        if(!isLoading) {
            onBackClick()
        }
    }

    val bannerJob = remember { mutableStateOf<Job?>(null) }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        reviewCoolDown()
        if(electronicBillsUiState.resetTimer != null){
            val date = Date(electronicBillsUiState.resetTimer)
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
            val fecha = format.format(date)
            Log.d("IberdrolaVerificationEmailElectronicBillsScreen", "hay que esperar hasta: "+ fecha)
        }else {
            Log.d("IberdrolaVerificationEmailElectronicBillsScreen", "reset no seteado aún")
        }
    }

    LaunchedEffect(counter) {
        if(counter == 0 && electronicBillsUiState.resetTimer == null){
            resetTimerUpdate()
        }
    }



    BackHandler(onBack = { supressBackStack(isLoading) })

    Box(modifier = Modifier
        .fillMaxSize()
        .background(IberdrolaTheme.colors.background)
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
    ) {
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
                    progressStart = 0.5f,
                    progressEnd = finalProgress,
                    onCloseClick = onCloseClick
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                ) {

                    VerificationInstructions(
                        instructionTitle = stringResource(R.string.introduce_codigo_verificacion),
                        instructionBody = stringResource(R.string.instrucciones_codigo_verificacion)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    VerificationCodeField(
                        value = verificationCode,
                        onValueChange = { input ->
                            if (input.all{ it.isDigit() } && input.length <= 6) {
                                verificationCode = input
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    HelpSection(
                        onResendClick = {
                            bannerJob.value?.cancel()
                            bannerJob.value = scope.launch {
                                isLoading = true
                                showSuccessMessage = false
                                delay(1500)
                                isLoading = false
                                showSuccessMessage = true
                                delay(5000)
                                showSuccessMessage = false
                            }
                        },
                        counter = counter,
                        updateCounter = { updateCounter() },
                        isLoading = isLoading
                    )
                }

                // Botonera inferior
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (showSuccessMessage) {
                        SuccessBanner(
                            message = stringResource(R.string.sms_enviado_exito),
                            onClose = { showSuccessMessage = false }
                        )
                    }
                    IberdrolaNextBackButtons(
                        isNextEnabled = verificationCode.length == 6,
                        onBackClick = onBackClick,
                        onNextClick = {
                            scope.launch {
                                isLoading = true
                                finalProgress = 1f

                                delay(1500)

                                isLoading = false
                                delay(300)
                                onNextClick()
                            }
                        }
                    )
                }
            }
        }

        if (isLoading) {
            LoadingOverlay()
        }
    }
}

@Composable
fun VerificationInstructions(instructionTitle: String, instructionBody: String) {
    Column {
        Text(
            text = instructionTitle,
            style = IberdrolaTheme.typography.tituloMedio.copy(fontSize = 18.sp),
            color = IberdrolaTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = instructionBody,
            style = IberdrolaTheme.typography.cuerpoPeque,
            color = IberdrolaTheme.colors.onSurfaceVariant,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun VerificationCodeField(value: String, onValueChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        textStyle = IberdrolaTheme.typography.cuerpoGrande.copy(
            letterSpacing = 1.sp
        ),
        interactionSource = interactionSource,
        enabled = true,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
                interactionSource = interactionSource,
                label = {
                    Text(
                        text = stringResource(R.string.codigo_verificacion_label),
                        style = IberdrolaTheme.typography.etiquetaGrande
                    )
                },
                contentPadding = PaddingValues(start = 0.dp, end = 0.dp, top = 20.dp, bottom = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = IberdrolaTheme.colors.primary,
                    unfocusedIndicatorColor = IberdrolaTheme.colors.onSurfaceVariant,
                    cursorColor = IberdrolaTheme.colors.primary,
                    focusedLabelColor = IberdrolaTheme.colors.primary,
                    unfocusedLabelColor = Color.Gray
                ),
                container = {
                    TextFieldDefaults.Container(
                        enabled = true,
                        isError = false,
                        interactionSource = interactionSource,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        shape = RectangleShape
                    )
                }
            )
        }
    )
}

@Composable
fun HelpSection(
    onResendClick: () -> Unit,
    counter: Int,
    updateCounter: () -> Unit,
    isLoading: Boolean
) {
    Surface(
        color = if(counter > 0) {
            IberdrolaTheme.colors.blueLight
        }else {
            IberdrolaTheme.colors.warningContainer.copy(alpha = 0.3f)
        },
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 20.dp,
            bottomEnd = 20.dp,
            bottomStart = 20.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = if(counter > 0) Icons.Outlined.Info else Icons.Outlined.ThumbDownOffAlt,
                contentDescription = null,
                tint = IberdrolaTheme.colors.onSurface,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(R.string.no_has_recibido_codigo),
                    style = IberdrolaTheme.typography.cuerpoPeque.copy(fontWeight = FontWeight.Bold),
                    color = IberdrolaTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                when (counter) {
                    3 -> {
                        Text(
                            text = stringResource(R.string.counter3_helpSelection),
                            style = IberdrolaTheme.typography.cuerpoPeque,
                            color = IberdrolaTheme.colors.onSurface,
                            lineHeight = 16.sp
                        )
                    }
                    0 -> {
                        Text(
                            text = stringResource(R.string.sin_intentos),
                            style = IberdrolaTheme.typography.cuerpoPeque,
                            color = IberdrolaTheme.colors.onSurface,
                            lineHeight = 16.sp
                        )
                    }
                    else -> {
                        Text(
                            text = stringResource(R.string.texto_helpSelection, counter),
                            style = IberdrolaTheme.typography.cuerpoPeque,
                            color = IberdrolaTheme.colors.onSurface,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.volver_a_enviar),
                    style = IberdrolaTheme.typography.cuerpoPeque.copy(
                        fontWeight = FontWeight.ExtraBold,
                        textDecoration = TextDecoration.Underline
                    ),
                    color = if (isLoading || counter<=0) IberdrolaTheme.colors.onSurface.copy(alpha = 0.5f)
                    else IberdrolaTheme.colors.onSurface,
                    modifier = Modifier
                        .padding(7.dp)
                        .clickable(
                            enabled = !isLoading && counter>0,
                            onClick = {
                                onResendClick()
                                updateCounter()
                            }
                        )
                )
            }
        }
    }
}

@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .pointerInput(Unit) {
                // No se podra pulsar nada mientras carga
            },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = IberdrolaTheme.colors.primary,
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun SuccessBanner(message: String, onClose: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = IberdrolaTheme.colors.successContainer,
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = IberdrolaTheme.colors.onSuccessContainer,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                style = IberdrolaTheme.typography.cuerpoPeque,
                color = IberdrolaTheme.colors.onSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cerrar),
                    tint = IberdrolaTheme.colors.onSurface,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIberdrolaVerificationScreen() {
    IberdrolaVerificationEmailElectronicBillsScreen(
        onCloseClick = {},
        onBackClick = {},
        onNextClick = {},
        electronicBillsUiState = ElectronicBillsUiState(),
        updateCounter = {},
        reviewCoolDown = {},
        resetTimerUpdate = {}
    )
}
