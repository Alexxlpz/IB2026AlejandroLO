package com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel

import com.iberdrola.practicas2026.alejandroLO.data.model.Direction

data class HomeUiState(
    val directionList: List<Direction> = emptyList(),
    val isLoading: Boolean = false,
    val isOnline: Boolean = false,
    val errorMessage: String? = null
)
