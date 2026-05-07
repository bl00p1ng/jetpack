package com.unilibre.taller02

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.unilibre.taller02.ui.detalle.DetalleTareaScreen
import com.unilibre.taller02.ui.lista.ListaTareasScreen
import com.unilibre.taller02.ui.lista.TareasViewModel

object Rutas {
    const val LISTA = "lista"
    const val DETALLE = "detalle/{id}"
    fun detalle(id: String) = "detalle/$id"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val viewModel: TareasViewModel = viewModel()

    NavHost(navController = navController, startDestination = Rutas.LISTA) {
        composable(Rutas.LISTA) {
            ListaTareasScreen(
                viewModel = viewModel,
                onTareaClick = { id -> navController.navigate(Rutas.detalle(id)) }
            )
        }
        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            DetalleTareaScreen(
                tareaId = id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
