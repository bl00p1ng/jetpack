package com.unilibre.taller05.presentation.recetas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unilibre.taller05.data.ai.ErrorTipo
import com.unilibre.taller05.data.ai.RecipeJsonParser
import com.unilibre.taller05.domain.model.Receta
import com.unilibre.taller05.domain.usecase.GenerarRecetasUseCase
import com.unilibre.taller05.domain.usecase.GuardarFavoritaUseCase
import com.unilibre.taller05.presentation.SharedSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecetasIAViewModel @Inject constructor(
    private val generar: GenerarRecetasUseCase,
    private val guardar: GuardarFavoritaUseCase,
    private val session: SharedSession
) : ViewModel() {

    private val _state = MutableStateFlow(RecetasState())
    val state: StateFlow<RecetasState> = _state.asStateFlow()

    init {
        iniciarStream()
    }

    fun iniciarStream() {
        val ingr = session.ingredientes.value
        if (ingr.isEmpty()) {
            _state.value = _state.value.copy(errorTipo = ErrorTipo.SIN_INGREDIENTES)
            return
        }
        _state.value = RecetasState(streaming = true, buffer = "", recetas = emptyList(), errorTipo = null)
        viewModelScope.launch {
            val sb = StringBuilder()
            generar(ingr).collect { chunk ->
                if (chunk.startsWith("__ERROR__")) {
                    val codigo = chunk.removePrefix("__ERROR__: ").trim()
                    _state.value = _state.value.copy(
                        streaming = false,
                        errorTipo = ErrorTipo.fromCodigo(codigo)
                    )
                    return@collect
                }
                sb.append(chunk)
                _state.value = _state.value.copy(buffer = sb.toString())
            }
            val recetas = RecipeJsonParser.parse(sb.toString(), ingr)
            session.recetasGeneradas.value = recetas
            _state.value = _state.value.copy(streaming = false, recetas = recetas)
        }
    }

    fun guardarComoFavorita(receta: Receta, onSaved: (Long) -> Unit) {
        viewModelScope.launch {
            val id = guardar(receta)
            onSaved(id)
        }
    }
}

data class RecetasState(
    val streaming: Boolean = false,
    val buffer: String = "",
    val recetas: List<Receta> = emptyList(),
    val errorTipo: ErrorTipo? = null
)
