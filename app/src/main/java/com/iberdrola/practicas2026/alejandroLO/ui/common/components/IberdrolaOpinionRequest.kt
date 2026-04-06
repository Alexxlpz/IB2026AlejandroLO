package com.iberdrola.practicas2026.alejandroLO.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.SentimentNeutral
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material.icons.outlined.SentimentVeryDissatisfied
import androidx.compose.material.icons.outlined.SentimentVerySatisfied
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.alejandroLO.ui.theme.Typography
@Composable
fun FeedbackDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onRatingSelected: (Int) -> Unit
) {
    if (!showDialog) return

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Barra superior
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(Color(0xFFBDBDBD), RoundedCornerShape(50))
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    FeedbackIcon(Icons.Outlined.SentimentVeryDissatisfied, Color(0xFFE53935), 1, onRatingSelected)
                    FeedbackIcon(Icons.Outlined.SentimentDissatisfied, Color(0xFFFB8C00), 2, onRatingSelected)
                    FeedbackIcon(Icons.Outlined.SentimentNeutral, Color(0xFF9E9E9E), 3, onRatingSelected)
                    FeedbackIcon(Icons.Outlined.SentimentSatisfied, Color(0xFF1E88E5), 4, onRatingSelected)
                    FeedbackIcon(Icons.Outlined.SentimentVerySatisfied, Color(0xFF43A047), 5, onRatingSelected)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Responder más tarde",
                    color = Color(0xFF2E7D32),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { onDismiss() }
                )
            }
        }
    )
}

@Composable
fun FeedbackIcon(
    icon: ImageVector,
    color: Color,
    rating: Int,
    onClick: (Int) -> Unit
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color,
        modifier = Modifier
            .size(36.dp)
            .clickable { onClick(rating) }
    )
}

@Composable
@Preview
fun OpinionRequestPreview(){
    FeedbackDialog(
        showDialog = true,
        onDismiss = {},
        onRatingSelected = {}
    )
}