package com.iberdrola.practicas2026.alejandroLO

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.iberdrola.practicas2026.alejandroLO.ui.navigation.IberdrolaNavGraph
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IB2026AlejandroLOTheme
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme
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
                var showExitDialog by remember { mutableStateOf(false) }

                BackHandler(enabled = true) {
                    showExitDialog = true
                }

                if (showExitDialog) {
                    AlertDialog(
                        onDismissRequest = { showExitDialog = false },
                        title = { Text(stringResource(R.string.salir_de_la_app)) },
                        text = { Text(stringResource(R.string.seguro_que_quieres_salir_de_la_aplicacion)) },
                        confirmButton = {
                            Button(
                                onClick = { finish() } // cerramos la Activity
                            ) {
                                Text(stringResource(R.string.aceptar))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showExitDialog = false }) {
                                Text(stringResource(R.string.cancel))
                            }
                        },
                        containerColor = IberdrolaTheme.colors.surface
                    )
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IberdrolaNavGraph(
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}