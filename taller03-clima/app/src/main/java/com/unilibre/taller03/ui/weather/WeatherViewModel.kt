package com.unilibre.taller03.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unilibre.taller03.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    val history: StateFlow<List<String>> = repository.observeHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun search(city: String) {
        val trimmed = city.trim()
        if (trimmed.isEmpty()) {
            _uiState.value = WeatherUiState.Error("Ingresa el nombre de una ciudad")
            return
        }
        _uiState.value = WeatherUiState.Loading
        viewModelScope.launch {
            val result = repository.fetchWeather(trimmed)
            _uiState.value = result.fold(
                onSuccess = { WeatherUiState.Success(it) },
                onFailure = { WeatherUiState.Error(parseError(it)) }
            )
        }
    }

    private fun parseError(t: Throwable): String {
        val msg = t.message ?: "Error desconocido"
        return when {
            msg.contains("OPENWEATHER_API_KEY") -> msg
            msg.contains("404") -> "Ciudad no encontrada"
            msg.contains("401") -> "API key invalida"
            msg.contains("Unable to resolve host") || msg.contains("timeout", true) ->
                "Sin conexion a internet"
            else -> "Error: $msg"
        }
    }
}
