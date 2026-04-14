package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums

enum class BillStatusEnum(val title: String) {
    PAGADA (title = "Pagada"),
    PENDIENTE (title = "Pendiente de Pago"),
    EN_TRAMITE (title = "En trámite de cobro"),
    ANULADA (title = "Anulada"),
    CUOTA_FIJA (title = "Cuota Fija")
}