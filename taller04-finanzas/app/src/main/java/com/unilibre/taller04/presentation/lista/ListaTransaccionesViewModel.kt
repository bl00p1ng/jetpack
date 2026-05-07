package com.unilibre.taller04.presentation.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unilibre.taller04.domain.model.Transaccion
import com.unilibre.taller04.domain.usecase.EliminarTransaccionUseCase
import com.unilibre.taller04.domain.usecase.ObtenerTransaccionesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaTransaccionesViewModel @Inject constructor(
    obtenerTransacciones: ObtenerTransaccionesUseCase,
    private val eliminarUseCase: EliminarTransaccionUseCase
) : ViewModel() {

    val transacciones: StateFlow<List<Transaccion>> =
        obtenerTransacciones().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun eliminar(tx: Transaccion) {
        viewModelScope.launch { eliminarUseCase(tx) }
    }
}
