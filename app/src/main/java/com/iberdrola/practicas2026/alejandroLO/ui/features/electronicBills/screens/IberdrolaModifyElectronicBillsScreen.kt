package com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaBar
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IberdrolaModifyElectronicBillsScreen(
    onBackClick: () -> Unit,
    onEditEmailClick: () -> Unit,
    selectedStreet: String,
    email: String
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .background(IberdrolaTheme.colors.surface)
                    .padding(WindowInsets.statusBars.asPaddingValues())
            ){
                IberdrolaBar(
                    onBackButtonClick = onBackClick
                )
            }
        },
        containerColor = IberdrolaTheme.colors.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.contrato_de_luz),
                style = IberdrolaTheme.typography.tituloGrande.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = IberdrolaTheme.colors.onSurface
            )

            Text(
                text = selectedStreet,
                style = IberdrolaTheme.typography.tituloPeque.copy(
                    fontSize = 18.sp,
                    lineHeight = 24.sp
                ),
                color = IberdrolaTheme.colors.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.recibes_facturas_electronicas_info),
                style = IberdrolaTheme.typography.cuerpoMedio
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.recibes_facturas_email_label),
                    style = IberdrolaTheme.typography.tituloPeque.copy(fontWeight = FontWeight.Bold),
                    color = IberdrolaTheme.colors.onSurface
                )
                Text(
                    text = email,
                    style = IberdrolaTheme.typography.cuerpoMedio,
                    color = IberdrolaTheme.colors.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp),
                    color = IberdrolaTheme.colors.border.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = IberdrolaTheme.colors.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(id = R.string.info_requisito_plan),
                    style = IberdrolaTheme.typography.cuerpoPeque,
                    color = IberdrolaTheme.colors.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onEditEmailClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = IberdrolaTheme.colors.primaryDark
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.modificar_email),
                        style = IberdrolaTheme.typography.tituloPeque,
                        color = IberdrolaTheme.colors.surface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewIberdrolaModifyElectronicBillsScreen() {
    IberdrolaModifyElectronicBillsScreen(
        onBackClick = {},
        onEditEmailClick = {},
        selectedStreet = "Calle Falsa 123",
        email = "emailPrueba@hotmail.com"
    )
}
