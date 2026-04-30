package com.iberdrola.practicas2026.alejandroLO.ui.common.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme

@Composable
fun VerificationHeader(title: String, progressStart: Float, progressEnd: Float, onCloseClick: () -> Unit) {

    var targetProgress by remember { mutableStateOf(progressStart) }

    LaunchedEffect(progressEnd) {
        targetProgress = progressEnd
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(
            durationMillis = 1750,
            easing = LinearOutSlowInEasing
        ),
        label = "ProgressAnimation"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, end = 4.dp)
        ) {
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = (-4).dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cerrar),
                    tint = IberdrolaTheme.colors.primaryDark,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Text(
            text = title,
            style = IberdrolaTheme.typography.tituloGrande.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 34.sp
            ),
            color = IberdrolaTheme.colors.onSurface,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 0.dp, bottom = 16.dp)
        )

        // Barra de progreso
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(IberdrolaTheme.colors.border.copy(alpha = 0.5f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .background(IberdrolaTheme.colors.primary)
            )
        }
    }
    Spacer(modifier = Modifier.height(32.dp))
}