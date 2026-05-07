package com.unilibre.taller03.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "name") val name: String,
    @Json(name = "main") val main: Main,
    @Json(name = "weather") val weather: List<WeatherItem>,
    @Json(name = "wind") val wind: Wind?,
    @Json(name = "sys") val sys: Sys?,
    @Json(name = "dt") val dt: Long
)

@JsonClass(generateAdapter = true)
data class Main(
    @Json(name = "temp") val temp: Double,
    @Json(name = "humidity") val humidity: Int
)

@JsonClass(generateAdapter = true)
data class WeatherItem(
    @Json(name = "id") val id: Int,
    @Json(name = "main") val main: String,
    @Json(name = "description") val description: String,
    @Json(name = "icon") val icon: String
)

@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "speed") val speed: Double
)

@JsonClass(generateAdapter = true)
data class Sys(
    @Json(name = "sunrise") val sunrise: Long?,
    @Json(name = "sunset") val sunset: Long?
)
