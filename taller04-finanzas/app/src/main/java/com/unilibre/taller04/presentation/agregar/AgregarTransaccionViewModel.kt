package com.unilibre.taller04.presentation.agregar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unilibre.taller04.domain.model.Categoria
import com.unilibre.taller04.domain.model.TipoTransaccion
import com.unilibre.taller04.domain.model.Transaccion
import com.unilibre.taller04.domain.usecase.AgregarTransaccionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AgregarUiState(
    val descripcion: String = "",
    val monto: String = "",
    val tipo: TipoTransaccion = TipoTransaccion.GASTO,
    val categoria: Categoria = Categoria.ALIMENTACION,
    val error: String? = null,
    val guardado: Boolean = false
)

@HiltViewModel
class AgregarTransaccionViewModel @Inject constructor(
    private val agregarUseCase: AgregarTransaccionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AgregarUiState())
    val state: StateFlow<AgregarUiState> = _state.asStateFlow()

    fun onDescripcionChange(v: String) = _state.update { it.copy(descripcion = v, error = null) }
    fun onMontoChange(v: String) = _state.update { it.copy(monto = v.filter { c -> c.isDigit() || c == '.' }, error = null) }
    fun onTipoChange(t: TipoTransaccion) = _state.update { it.copy(tipo = t) }
    fun onCategoriaChange(c: Categoria) = _state.update { it.copy(categoria = c) }

    fun guardar() {
        val s = _state.value
        val monto = s.monto.toDoubleOrNull() ?: 0.0
        val tx = Transaccion(
            descripcion = s.descripcion.trim(),
            monto = monto,
            tipo = s.tipo,
            categoria = s.categoria,
            fecha = System.currentTimeMillis()
        )
        viewModelScope.launch {
            agregarUseCase(tx)
                .onSuccess { _state.update { it.copy(guardado = true, error = null) } }
                .onFailure { e -> _state.update { it.copy(error = e.message ?: "Error al guardar") } }
        }
    }
}
