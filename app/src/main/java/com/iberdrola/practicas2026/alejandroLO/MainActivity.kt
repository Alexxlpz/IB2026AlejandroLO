package com.iberdrola.practicas2026.alejandroLO

import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.iberdrola.practicas2026.alejandroLO.ui.features.main.screens.IberdrolaMainScreen
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locale = Locale.getDefault() // para que coja la region del dispositivo y asi poder
        // adecuarnos a su cultura (forma de representar numeros, idioma(por ahora no), etc)
        Log.i("MainActivity", "Locale: $locale")
        enableEdgeToEdge()
        setContent {
            IB2026AlejandroLOTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IberdrolaMainScreen(
                        modifier = Modifier.padding(innerPadding),
                        locale = locale
                    )
                }
            }
        }
    }
}