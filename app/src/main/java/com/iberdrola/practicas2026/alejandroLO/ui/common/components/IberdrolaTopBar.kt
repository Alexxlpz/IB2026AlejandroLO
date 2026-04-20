package com.iberdrola.practicas2026.alejandroLO.ui.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.alejandroLO.R
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme

@Composable
fun IberdrolaTopBar(selectedOption: BillTypeEnum,
                    streetName: String,
                    options: List<BillTypeEnum>,
                    onOptionSelected: (BillTypeEnum) -> Unit,
                    onBackButtonClick: () -> Unit,
                    modifier: Modifier = Modifier){
    Column(modifier = modifier
        .fillMaxWidth()
        .testTag("top_bar")) {

        IberdrolaBar(
            onBackButtonClick = onBackButtonClick

        )

        IberdrolaTitleAndDescription(
            description = streetName,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(18.dp))
        ServiceSelector(
            selectedOption = selectedOption.title,
            options = options.map { it.title },
            onOptionSelected = { selectedOptionTitle ->
                val selectedOption = BillTypeEnum.entries.find { it.title == selectedOptionTitle } ?: BillTypeEnum.LUZ
                onOptionSelected(selectedOption)
            }
        )
    }
}

@Composable
fun IberdrolaBar(onBackButtonClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) IberdrolaTheme.colors.primary.copy(alpha = 0.12f) else Color.Transparent,
        label = "backButtonBackground"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(backgroundColor)
                .clickable { onBackButtonClick() }
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 10.dp)
                .testTag("main_back_button")
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = stringResource(R.string.back_button),
                modifier = Modifier.size(27.dp),
                tint = IberdrolaTheme.colors.primary
            )

            Text(
                text = stringResource(R.string.atras),
                style = IberdrolaTheme.typography.tituloPeque,
                color = IberdrolaTheme.colors.primary,
                textDecoration = TextDecoration.Underline
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
            text = stringResource(R.string.mis_facturas),
            style = IberdrolaTheme.typography.tituloPrincipal,
            color = IberdrolaTheme.colors.onSurface
        )
        Text(
            text = description,
            style = IberdrolaTheme.typography.tituloMedio,
            color = IberdrolaTheme.colors.onSurface
        )
    }
}

@Composable
fun ServiceSelector(
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)
    ) {
        for (option in options) {
            ServiceOption(
                text = option,
                isSelected = selectedOption == option,
                onClick = { onOptionSelected(option) }
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {
        HorizontalDivider(
            thickness = 3.dp,
            color = IberdrolaTheme.colors.border.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun ServiceOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val textColor by animateColorAsState(
        targetValue = if (isSelected) IberdrolaTheme.colors.onSurface else IberdrolaTheme.colors.onSurfaceVariant,
        animationSpec = tween(durationMillis = 300),
        label = "textColorAnimation"
    )

    val barWidth by animateDpAsState(
        targetValue = if (isSelected) 50.dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "barWidthAnimation"
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)) // Ripple recortado
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = IberdrolaTheme.colors.onSurface),
                onClick = onClick
            )
            .padding(top = 12.dp)
            .testTag("service_option_$text"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            style = IberdrolaTheme.typography.tituloGrande,
            color = textColor,
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .width(barWidth)
                .height(4.dp)
                .background(
                    color = if (isSelected) IberdrolaTheme.colors.primary else Color.Transparent
                )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewIberdrolaTopBar() {
    IB2026AlejandroLOTheme {
        IberdrolaTopBar(
            selectedOption = BillTypeEnum.LUZ,
            streetName = "Calle Falsa 123",
            options = BillTypeEnum.entries,
            onOptionSelected = { },
            onBackButtonClick = { },
            modifier = Modifier
        )
    }
}