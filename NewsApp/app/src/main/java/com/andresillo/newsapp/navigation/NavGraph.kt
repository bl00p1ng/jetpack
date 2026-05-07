package com.andresillo.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.andresillo.newsapp.ui.screen.DetailsScreen
import com.andresillo.newsapp.ui.screen.ListScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object Routes {
    const val LIST = "list"
    const val DETAIL = "detail/{title}"

    fun detailRoute(title: String): String {
        val encoded = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
        return "detail/$encoded"
    }
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LIST) {
        composable(Routes.LIST) {
            ListScreen(
                onNewsClick = { title ->
                    navController.navigate(Routes.detailRoute(title))
                }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedTitle = backStackEntry.arguments?.getString("title") ?: ""
            val title = URLDecoder.decode(encodedTitle, StandardCharsets.UTF_8.toString())
            DetailsScreen(
                title = title,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
