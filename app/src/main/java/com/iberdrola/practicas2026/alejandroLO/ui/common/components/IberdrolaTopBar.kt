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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.BillType
import java.util.Locale

@Composable
fun IberdrolaTopBar(selectedOption: BillType,
                    options: List<BillType>,
                    onOptionSelected: (BillType) -> Unit,
                    isSyncEnabled: Boolean,
                    onSyncToggle: (Boolean) -> Unit,
                    modifier: Modifier = Modifier){
    Column(modifier = modifier.fillMaxWidth()) {

        IberdrolaBar(
            onBackClick = { /* Navegar atrás, se implementará mas adelante */ },
            isSyncEnabled = isSyncEnabled,
            onSyncToggle = onSyncToggle
        )
        IberdrolaTitleAndDescription(
            description = "Esta es la descripción de tus facturas",
            modifier = Modifier.padding(16.dp)
        )
        ServiceSelector(
            selectedOption = selectedOption.title,
            options = options.map { it.title },
            onOptionSelected = {
                onOptionSelected(BillType.valueOf(it.uppercase(Locale.getDefault())))
            }
        )
    }
}

@Composable
fun IberdrolaBar(
    onBackClick: () -> Unit,
    isSyncEnabled: Boolean,
    onSyncToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Alignment.Start,
            modifier = Modifier.clickable { onBackClick() }
        ) {
            IconButton(onClick = {} /*onBackClick*/) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
            Text(text = " Atrás",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (isSyncEnabled) "Online" else "Offline",
                style = MaterialTheme.typography.labelMedium,
                color = if (isSyncEnabled) Color(0xFF008F39) else Color.Gray
            )
            Switch(
                checked = isSyncEnabled,
                onCheckedChange = onSyncToggle
            )
        }
    }
}

@Composable
fun IberdrolaTitleAndDescription(
    description: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Mis facturas",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = description,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun ServiceSelector(
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {
        for (option in options) {
            ServiceOption(
                text = option,
                isSelected = selectedOption == option,
                onClick = { onOptionSelected(option) }
            )
            Spacer(modifier = Modifier.width(24.dp))
        }
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {
        HorizontalDivider(
            thickness = 2.5.dp,
            color = Color.LightGray.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun ServiceOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.Black else Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(
                    color = if (isSelected) Color(0xFF008F39) else Color.Transparent
                )
        )
    }
}