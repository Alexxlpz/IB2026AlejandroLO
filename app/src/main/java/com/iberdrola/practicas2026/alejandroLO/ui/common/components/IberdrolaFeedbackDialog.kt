package com.iberdrola.practicas2026.alejandroLO.ui.common.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.SentimentNeutral
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material.icons.outlined.SentimentVeryDissatisfied
import androidx.compose.material.icons.outlined.SentimentVerySatisfied
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme
import com.iberdrola.practicas2026.alejandroLO.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackDialogContent(
    onAskLater: () -> Unit,
    thanksActivate: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tu opinión nos importa",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "¿Cómo de probable es que recomiendes esta app a amigos o familiares para que realicen sus gestiones?",
            style = Typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Color(0xFFE0E0E0))

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            FeedbackIcon(Icons.Outlined.SentimentVeryDissatisfied, Color(0xFFE53935), thanksActivate)
            FeedbackIcon(Icons.Outlined.SentimentDissatisfied, Color(0xFFFB8C00), thanksActivate)
            FeedbackIcon(Icons.Outlined.SentimentNeutral, Color(0xFF9E9E9E), thanksActivate)
            FeedbackIcon(Icons.Outlined.SentimentSatisfied, Color(0xFF1E88E5), thanksActivate)
            FeedbackIcon(Icons.Outlined.SentimentVerySatisfied, Color(0xFF43A047), thanksActivate)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.responder_mas_tarde),
            color = Color(0xFF2E7D32),
            style = MaterialTheme.typography.bodyMedium.copy(
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(
                        color = IberdrolaTheme.colors.onSurface.copy(alpha = 0.12f)
                    ),
                    onClick = { onAskLater() }
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

/*
- si da su valoracion esperamos 10 (cont = 10)
- si le da a responder mas tarde 3 (cont = 3)
- si lo cierra de otra manera a la proxima le aparecerá de nuevo (el cont se queda en 0)
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IberdrolaFeedbackDialog(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onAskLater: () -> Unit,
    onRatingSelected: () -> Unit
) {

    val showThanks = remember { mutableStateOf(false) }
    val thanksActivate: (Boolean) -> Unit = {
        showThanks.value = it
        onRatingSelected()
    }

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = IberdrolaTheme.colors.surfaceVariant,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        scrimColor = Color.Black.copy(alpha = 0.4f),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.testTag("bottom_sheet")
    ) {
        if(showThanks.value){
            IberdrolaThanksFeedback()
        } else {
            FeedbackDialogContent(onAskLater, thanksActivate)
        }
    }
}

@Composable
fun FeedbackIcon(
    icon: ImageVector,
    color: Color,
    onClick: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = IberdrolaTheme.colors.onSurface.copy(alpha = 0.12f)
                ),
                onClick = { onClick(true) }
            )
            .padding(6.dp)
    )
}

@Composable
fun IberdrolaThanksFeedback() {
    val visible = remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (visible.value) 1.2f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "ThumbScale"
    )

    LaunchedEffect(Unit) {
        visible.value = true
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(21.7.dp))

        Icon(
            imageVector = Icons.Outlined.ThumbUp,
            contentDescription = null,
            tint = Color(0xFF43A047),
            modifier = Modifier
                .size(80.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¡Muchas gracias!",
            style = IberdrolaTheme.typography.tituloGrande,
            fontWeight = FontWeight.Bold,
            color = IberdrolaTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Tu valoración nos ayuda a seguir mejorando para ofrecerte el mejor servicio.",
            style = IberdrolaTheme.typography.cuerpoMedio,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun OpinionRequestPreview(){
    IberdrolaFeedbackDialog(
        sheetState = rememberModalBottomSheetState(),
        onDismiss = { },
        onAskLater = { },
        onRatingSelected = { }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ThanksPreview(){
    IberdrolaThanksFeedback()
}