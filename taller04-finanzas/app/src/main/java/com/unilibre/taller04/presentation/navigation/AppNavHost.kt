package com.unilibre.taller04.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unilibre.taller04.presentation.agregar.AgregarTransaccionScreen
import com.unilibre.taller04.presentation.dashboard.DashboardScreen
import com.unilibre.taller04.presentation.lista.ListaTransaccionesScreen

object Routes {
    const val DASHBOARD = "dashboard"
    const val AGREGAR = "agregar"
    const val LISTA = "lista"
}

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.DASHBOARD) {
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onAgregar = { nav.navigate(Routes.AGREGAR) },
                onVerTodas = { nav.navigate(Routes.LISTA) }
            )
        }
        composable(Routes.AGREGAR) {
            AgregarTransaccionScreen(onBack = { nav.popBackStack() })
        }
        composable(Routes.LISTA) {
            ListaTransaccionesScreen(onBack = { nav.popBackStack() })
        }
    }
}
