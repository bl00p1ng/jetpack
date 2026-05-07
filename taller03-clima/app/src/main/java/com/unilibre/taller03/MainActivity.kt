package com.unilibre.taller03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.unilibre.taller03.ui.theme.Taller03ClimaTheme
import com.unilibre.taller03.ui.weather.WeatherScreen
import com.unilibre.taller03.ui.weather.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Taller03ClimaTheme {
                val vm: WeatherViewModel = hiltViewModel()
                WeatherScreen(viewModel = vm)
            }
        }
    }
}
