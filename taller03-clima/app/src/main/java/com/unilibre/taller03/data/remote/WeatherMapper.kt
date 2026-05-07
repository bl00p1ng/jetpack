package com.unilibre.taller03.data.remote

import com.unilibre.taller03.domain.model.WeatherData

fun WeatherResponse.toDomain(): WeatherData {
    val w = weather.firstOrNull()
    val iconCode = w?.icon ?: "01d"
    val isNight = iconCode.endsWith("n")
    return WeatherData(
        city = name,
        tempC = main.temp,
        description = w?.description?.replaceFirstChar { it.uppercase() } ?: "",
        iconCode = iconCode,
        condition = w?.main ?: "Clear",
        humidity = main.humidity,
        windKmh = (wind?.speed ?: 0.0) * 3.6,
        isNight = isNight
    )
}
