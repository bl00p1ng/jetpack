package com.unilibre.taller05.presentation.navigation

import androidx.compose.runtime.Composable
import android.net.Uri
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.unilibre.taller05.presentation.camara.CamaraScreen
import com.unilibre.taller05.presentation.detalle.DetalleRecetaScreen
import com.unilibre.taller05.presentation.inicio.InicioScreen
import com.unilibre.taller05.presentation.ingredientes.IngredientesScreen
import com.unilibre.taller05.presentation.recetas.RecetasIAScreen

object Routes {
    const val Inicio = "inicio"
    const val Camara = "camara"
    const val Ingredientes = "ingredientes"
    const val Recetas = "recetas"
    const val Detalle = "detalle/{nombre}"
    fun detalle(nombre: String) = "detalle/${Uri.encode(nombre)}"
}

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.Inicio) {
        composable(Routes.Inicio) {
            InicioScreen(
                onCamera = { nav.navigate(Routes.Camara) },
                onRecetaClick = { nombre -> nav.navigate(Routes.detalle(nombre)) }
            )
        }
        composable(Routes.Camara) {
            CamaraScreen(
                onBack = { nav.popBackStack() },
                onIngredientesDetectados = {
                    nav.navigate(Routes.Ingredientes) {
                        popUpTo(Routes.Inicio)
                    }
                }
            )
        }
        composable(Routes.Ingredientes) {
            IngredientesScreen(
                onBack = { nav.popBackStack() },
                onGenerar = { nav.navigate(Routes.Recetas) }
            )
        }
        composable(Routes.Recetas) {
            RecetasIAScreen(
                onBack = { nav.popBackStack() },
                onRecetaClick = { nombre -> nav.navigate(Routes.detalle(nombre)) }
            )
        }
        composable(
            Routes.Detalle,
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { entry ->
            val nombre = entry.arguments?.getString("nombre") ?: ""
            DetalleRecetaScreen(
                nombre = nombre,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
