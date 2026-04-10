package com.iberdrola.practicas2026.alejandroLO.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle


// Objeto para acceder al tema de forma "local"
object IberdrolaTheme {
    val colors: IberdrolaColors
        @Composable
        @ReadOnlyComposable
        get() = LocalIberdrolaColors.current

    val typography: IberdrolaTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalIberdrolaTypography.current
}

// Estructura de Colores Propios
data class IberdrolaColors(
    val primary: Color = IberdrolaGreen,
    val primaryLight: Color = IberdrolaGreenLight,
    val primaryDark: Color = IberdrolaGreenDark,
    val background: Color = Color.White,
    val surface: Color = Color.White,
    val surfaceVariant: Color = SurfaceGray,
    val onSurface: Color = TextPrimary,
    val onSurfaceVariant: Color = TextSecondary,
    val border: Color = BorderGray,
    val successContainer: Color = IberdrolaGreenLight,
    val onSuccessContainer: Color = IberdrolaGreenDark,
    val errorContainer: Color = IberdrolaRedStatus,
    val onErrorContainer: Color = IberdrolaRedText,
    val iconLuzGas: Color = Color(0xFF004D3F),
    val importeHistorico: Color = Color(0xFF868686)
)

// Estructura de Tipografía Propia
data class IberdrolaTypography(
    val tituloGrande: TextStyle = Typography.titleLarge,
    val tituloMedio: TextStyle = Typography.titleMedium,
    val tituloPeque: TextStyle = Typography.titleSmall,
    val importe: TextStyle = Typography.headlineMedium,
    val cuerpoGrande: TextStyle = Typography.bodyLarge,
    val cuerpoMedio: TextStyle = Typography.bodyMedium,
    val cuerpoPeque: TextStyle = Typography.bodySmall,
    val etiquetaGrande: TextStyle = Typography.labelLarge,
    val etiquetaPeque: TextStyle = Typography.labelSmall,
    val importeHistorico: TextStyle = PriceHistoryStyle
)

val LocalIberdrolaColors = staticCompositionLocalOf { IberdrolaColors() }
val LocalIberdrolaTypography = staticCompositionLocalOf { IberdrolaTypography() }

@Composable
fun IB2026AlejandroLOTheme(
    content: @Composable () -> Unit
) {
    val colors = IberdrolaColors()
    val typography = IberdrolaTypography()

    CompositionLocalProvider(
        LocalIberdrolaColors provides colors,
        LocalIberdrolaTypography provides typography
    ) {
        MaterialTheme(
            colorScheme = lightColorScheme(primary = IberdrolaGreen),
            content = content
        )
    }
}
