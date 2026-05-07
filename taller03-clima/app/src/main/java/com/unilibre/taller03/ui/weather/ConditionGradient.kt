package com.unilibre.taller03.ui.weather

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.unilibre.taller03.domain.model.WeatherData

private val Sunny = Color(0xFFFFA726) to Color(0xFFFF7043)
private val Rain = Color(0xFF1976D2) to Color(0xFF0D47A1)
private val Cloudy = Color(0xFF90A4AE) to Color(0xFF455A64)
private val Snow = Color(0xFFB3E5FC) to Color(0xFF4FC3F7)
private val Night = Color(0xFF512DA8) to Color(0xFF1A237E)
private val Idle = Color(0xFF42A5F5) to Color(0xFF1565C0)

private fun colorsFor(data: WeatherData?): Pair<Color, Color> {
    if (data == null) return Idle
    if (data.isNight) return Night
    return when (data.condition.lowercase()) {
        "clear" -> Sunny
        "rain", "drizzle", "thunderstorm" -> Rain
        "snow" -> Snow
        "clouds", "mist", "fog", "haze", "smoke" -> Cloudy
        else -> Sunny
    }
}

@Composable
fun rememberConditionGradient(data: WeatherData?): Brush {
    val (top, bottom) = colorsFor(data)
    val animTop: State<Color> = animateColorAsState(
        targetValue = top,
        animationSpec = tween(durationMillis = 800),
        label = "gradientTop"
    )
    val animBottom: State<Color> = animateColorAsState(
        targetValue = bottom,
        animationSpec = tween(durationMillis = 800),
        label = "gradientBottom"
    )
    return Brush.verticalGradient(listOf(animTop.value, animBottom.value))
}
