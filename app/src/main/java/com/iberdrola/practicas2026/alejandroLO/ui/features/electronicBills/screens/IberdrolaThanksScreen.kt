package com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme

@Composable
fun IberdrolaThanksScreen(
    email: String,
    isModificacion: Boolean,
    onAcceptClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    BackHandler(onBack = onCloseClick)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(IberdrolaTheme.colors.primaryDark)
    ) {
        ThanksCloseButton(onCloseClick = onCloseClick)

        ThanksMainContent(
            visible = visible,
            isModificacion = isModificacion,
            email = email
        )

        ThanksAcceptButton(
            onAcceptClick = onAcceptClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ThanksCloseButton(onCloseClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 4.dp)
                .offset(y = (-4).dp)
                .size(48.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.cerrar),
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun ThanksMainContent(
    visible: Boolean,
    isModificacion: Boolean,
    email: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(800)) +
                    scaleIn(initialScale = 0.8f, animationSpec = tween(800))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = if (isModificacion)
                        stringResource(R.string.thanks_modified_email)
                    else
                        stringResource(R.string.thanks_activated_electronic_bill),
                    style = IberdrolaTheme.typography.tituloGrande.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 32.sp
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.thanks_email_verification_msg, email),
                    style = IberdrolaTheme.typography.cuerpoMedio,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Composable
private fun ThanksAcceptButton(
    onAcceptClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onAcceptClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 64.dp, start = 32.dp, end = 32.dp)
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = IberdrolaTheme.colors.primaryDark
        )
    ) {
        Text(
            text = stringResource(R.string.aceptar),
            style = IberdrolaTheme.typography.tituloPeque,
            fontWeight = FontWeight.Bold
        )
    }
}