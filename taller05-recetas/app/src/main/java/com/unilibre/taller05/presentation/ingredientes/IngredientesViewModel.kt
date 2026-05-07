package com.unilibre.taller05.presentation.ingredientes

import androidx.lifecycle.ViewModel
import com.unilibre.taller05.presentation.SharedSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class IngredientesViewModel @Inject constructor(
    private val session: SharedSession
) : ViewModel() {

    val ingredientes: StateFlow<List<String>> = session.ingredientes

    fun agregar(nombre: String) {
        val n = nombre.trim()
        if (n.isEmpty()) return
        val current = session.ingredientes.value
        if (current.any { it.equals(n, ignoreCase = true) }) return
        session.ingredientes.value = current + n
    }

    fun eliminar(nombre: String) {
        session.ingredientes.value = session.ingredientes.value.filterNot { it == nombre }
    }
}
