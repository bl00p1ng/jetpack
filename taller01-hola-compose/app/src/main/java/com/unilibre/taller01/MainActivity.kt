// Taller 01 - Hola Compose
// Universidad Libre - Jetpack Compose
//
// NOTA: La pantalla principal es PerfilScreen (ver ui/PerfilScreen.kt). Esa pantalla
// implementa el ejemplo de la "Actividad IA" del taller: tarjeta de perfil con foto,
// nombre, rol y botón de contacto. Las demás composables del taller también están
// disponibles con sus respectivos @Preview:
//   - ui/TarjetaBienvenida.kt          -> Composable + @Preview básicos
//   - ui/PresentacionPersonalScreen.kt -> Actividad de extensión (presentación personal)
package com.unilibre.taller01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.unilibre.taller01.ui.PerfilScreen
import com.unilibre.taller01.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                PerfilScreen()
            }
        }
    }
}
