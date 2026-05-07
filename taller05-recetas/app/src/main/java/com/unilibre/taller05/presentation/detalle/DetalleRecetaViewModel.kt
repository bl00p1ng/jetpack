package com.unilibre.taller05.presentation.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unilibre.taller05.domain.model.Receta
import com.unilibre.taller05.domain.repository.RecetasRepository
import com.unilibre.taller05.presentation.SharedSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleRecetaViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val repo: RecetasRepository,
    private val session: SharedSession
) : ViewModel() {

    private val nombre: String = savedState["nombre"] ?: ""

    private val _state = MutableStateFlow(DetalleState())
    val state: StateFlow<DetalleState> = _state.asStateFlow()

    init {
        cargar()
    }

    private fun cargar() {
        viewModelScope.launch {
            // 1) Buscar primero en la sesion en memoria (recetas recien generadas por Gemini)
            val enMemoria = session.recetasGeneradas.value.firstOrNull { it.nombre == nombre }
            // 2) Sino, buscar en Room (favoritas)
            val recetaBase = enMemoria ?: repo.obtenerPorNombre(nombre)
            val esFav = repo.esFavorita(nombre)
            _state.value = DetalleState(
                receta = recetaBase?.copy(esFavorita = esFav),
                cargando = false
            )
        }
    }

    fun toggleFavorita() {
        val r = _state.value.receta ?: return
        viewModelScope.launch {
            val erafav = repo.esFavorita(r.nombre)
            if (erafav) {
                val id = r.id.takeIf { it > 0 } ?: repo.obtenerPorNombre(r.nombre)?.id ?: return@launch
                repo.eliminar(id)
            } else {
                repo.guardar(r.copy(id = 0))
            }
            // Refrescar conservando el contenido visible aunque ya no este en Room
            _state.value = _state.value.copy(
                receta = r.copy(esFavorita = !erafav)
            )
        }
    }
}

data class DetalleState(
    val receta: Receta? = null,
    val cargando: Boolean = true
)
