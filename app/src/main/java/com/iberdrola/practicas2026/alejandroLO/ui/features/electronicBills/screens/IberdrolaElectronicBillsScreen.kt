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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.alejandroLO.ui.common.components.IberdrolaBar
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme

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
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Factura electrónica",
            style = IberdrolaTheme.typography.tituloSecundario,
            color = IberdrolaTheme.colors.onSurface
        )

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

            item { HorizontalDivider(color = IberdrolaTheme.colors.border.copy(alpha = 0.5f)) }

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

            item { HorizontalDivider(color = IberdrolaTheme.colors.border.copy(alpha = 0.5f)) }
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
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActivo) IberdrolaTheme.colors.primaryDark else IberdrolaTheme.colors.disableFontColor.copy(alpha = 0.65f),
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                style = IberdrolaTheme.typography.tituloPeque,
                color = IberdrolaTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            ContratoStatusBadge(estado = estado, isActivo = isActivo)
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = IberdrolaTheme.colors.onSurfaceVariant,
            modifier = Modifier.size(30.dp)
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
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = IberdrolaTheme.typography.etiquetaPeque,
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
