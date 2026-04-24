package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums

import androidx.compose.ui.graphics.Color

enum class BillStatusEnum(val title: String, val color: Color, val status: Color) {
    PAGADA(
        title = "Pagada",
        color = Color(0xFF00833E),
        status = Color(0xFFD4EBD0)
    ),
    PENDIENTE(
        title = "Pendiente de Pago",
        color = Color(0xFFD32F2F),
        status = Color(0xFFF2B3BA)
    ),
    EN_TRAMITE(
        title = "En trámite de cobro",
        color = Color(0xFFBF711E),
        status = Color(0xFFFFE198)
    ),
    ANULADA(
        title = "Anulada",
        color = Color(0xFF7B1FA2),
        status = Color(0xFFF9D8FF)
    ),
    CUOTA_FIJA(
        title = "Cuota Fija",
        color = Color(0xFF01579B),
        status = Color(0xFFC1E6F7)
    )
}