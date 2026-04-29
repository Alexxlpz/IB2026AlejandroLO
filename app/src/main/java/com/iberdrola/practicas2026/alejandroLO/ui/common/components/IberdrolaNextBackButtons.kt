package com.iberdrola.practicas2026.alejandroLO.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme

@Composable
fun IberdrolaNextBackButtons(
    isNextEnabled: Boolean,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(
            color = IberdrolaTheme.colors.border.copy(alpha = 0.7f),
            thickness = 1.5.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, IberdrolaTheme.colors.primary)
            ) {
                Text(
                    text = stringResource(id = R.string.anterior),
                    style = IberdrolaTheme.typography.tituloPeque,
                    color = IberdrolaTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onNextClick,
                enabled = isNextEnabled,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isNextEnabled)
                        IberdrolaTheme.colors.primaryDark
                    else
                        IberdrolaTheme.colors.primary.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = stringResource(id = R.string.siguiente),
                    style = IberdrolaTheme.typography.tituloPeque,
                    color = if (isNextEnabled)
                        IberdrolaTheme.colors.surfaceVariant
                    else
                        IberdrolaTheme.colors.primary.copy(alpha = 0.65f),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(50.dp))
}
