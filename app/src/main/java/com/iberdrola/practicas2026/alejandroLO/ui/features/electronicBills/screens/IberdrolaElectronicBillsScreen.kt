package com.iberdrola.practicas2026.alejandroLO.ui.features.electronicBills.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaBar
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme
import com.iberdrola.practicas2026.alejandroLO.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IberdrolaElectronicBillsScreen(
    onBackClick: () -> Unit,
    onContratoClick: (Boolean) -> Unit,
    updateSelectedTypeBill: (BillTypeEnum) -> Unit
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
        FacturaElectronicaContent(
            modifier = Modifier.padding(paddingValues),
            onContratoClick = onContratoClick,
            updateSelectedTypeBill = updateSelectedTypeBill
        )
    }
}

@Composable
fun FacturaElectronicaContent(
    modifier: Modifier = Modifier,
    onContratoClick: (Boolean) -> Unit,
    updateSelectedTypeBill: (BillTypeEnum) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        Box(Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = stringResource(R.string.factura_electronica),
                style = IberdrolaTheme.typography.tituloSecundario,
                color = IberdrolaTheme.colors.onSurface
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {

            item {
                ContratoItem(
                    titulo = "Contrato de Luz",
                    estado = "Activa",
                    icon = Icons.Outlined.Lightbulb,
                    isActivo = true,
                    onClick = {
                        onContratoClick(true)
                        updateSelectedTypeBill(BillTypeEnum.LUZ)
                    }
                )
            }

            item {
                HorizontalDivider(
                    color = IberdrolaTheme.colors.border.copy(alpha = 0.95f),
                    thickness = 1.5.dp
                )
            }

            item {
                ContratoItem(
                    titulo = "Contrato de Gas",
                    estado = "Sin Activar",
                    icon = Icons.Outlined.LocalFireDepartment,
                    isActivo = false,
                    onClick = {
                        onContratoClick(false)
                        updateSelectedTypeBill(BillTypeEnum.GAS)
                    }
                )
            }

            item {
                HorizontalDivider(
                    color = IberdrolaTheme.colors.border.copy(alpha = 0.95f),
                    thickness = 1.5.dp
                )
            }
        }
    }
}


@Composable
fun ContratoItem(
    titulo: String,
    estado: String,
    icon: ImageVector,
    isActivo: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 20.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActivo) IberdrolaTheme.colors.primaryDark else IberdrolaTheme.colors.disableFontColor.copy(alpha = 0.65f),
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(15.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                style = IberdrolaTheme.typography.tituloMedio,
                color = IberdrolaTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(13.dp))

            ContratoStatusBadge(estado = estado, isActivo = isActivo)
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = IberdrolaTheme.colors.onSurfaceVariant,
            modifier = Modifier.size(38.dp)
        )
    }
}

@Composable
fun ContratoStatusBadge(
    estado: String,
    isActivo: Boolean
) {
    Surface(
        color = if (isActivo)
            IberdrolaTheme.colors.primaryLight.copy(alpha = 0.5f)
        else
            IberdrolaTheme.colors.disabledContainer,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = estado,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 6.dp),
            style = IberdrolaTheme.typography.etiquetaGrande,
            color = if (isActivo)
                IberdrolaTheme.colors.primaryDark
            else
                IberdrolaTheme.colors.disableFontColor
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewIberdrolaElectronicBillsScreen() {
    IberdrolaElectronicBillsScreen(
        onBackClick = {},
        onContratoClick = {},
        updateSelectedTypeBill = {}
    )
}
