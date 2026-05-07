package com.unilibre.taller04.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unilibre.taller04.domain.model.Transaccion
import com.unilibre.taller04.domain.usecase.EliminarTransaccionUseCase
import com.unilibre.taller04.domain.usecase.ObtenerResumenUseCase
import com.unilibre.taller04.domain.usecase.ObtenerSerieMensualUseCase
import com.unilibre.taller04.domain.usecase.ObtenerTransaccionesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    obtenerTransacciones: ObtenerTransaccionesUseCase,
    obtenerResumen: ObtenerResumenUseCase,
    obtenerSerie: ObtenerSerieMensualUseCase,
    private val eliminarUseCase: EliminarTransaccionUseCase
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> =
        combine(
            obtenerTransacciones(),
            obtenerResumen(),
            obtenerSerie()
        ) { transacciones, resumen, serie ->
            DashboardUiState(
                resumen = resumen,
                recientes = transacciones.take(5),
                serieMensual = serie,
                cargando = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState()
        )

    fun eliminar(transaccion: Transaccion) {
        viewModelScope.launch { eliminarUseCase(transaccion) }
    }
}
