package com.unilibre.taller03.ui.weather

import com.unilibre.taller03.domain.model.WeatherData

sealed interface WeatherUiState {
    data object Idle : WeatherUiState
    data object Loading : WeatherUiState
    data class Success(val data: WeatherData) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}
