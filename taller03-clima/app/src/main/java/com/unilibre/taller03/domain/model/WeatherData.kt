package com.unilibre.taller03.domain.model

data class WeatherData(
    val city: String,
    val tempC: Double,
    val description: String,
    val iconCode: String,
    val condition: String,
    val humidity: Int,
    val windKmh: Double,
    val isNight: Boolean
)
