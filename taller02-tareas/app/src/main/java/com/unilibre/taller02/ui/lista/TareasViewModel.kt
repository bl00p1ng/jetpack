package com.unilibre.taller02.ui.lista

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.unilibre.taller02.model.Tarea
import java.util.UUID

/**
 * Fuente unica de verdad para la lista de tareas.
 *
 * Usa [mutableStateListOf] porque es observable por Compose y sobrevive
 * a cambios de configuracion mientras el ViewModel siga vivo. Para que
 * tambien sobreviva a la muerte del proceso se podria usar SavedStateHandle,
 * pero para los objetivos del taller es suficiente.
 */
class TareasViewModel : ViewModel() {

    private val _tareas: SnapshotStateList<Tarea> = mutableStateListOf(
        Tarea(id = UUID.randomUUID().toString(), titulo = "Leer capitulo de Compose"),
        Tarea(id = UUID.randomUUID().toString(), titulo = "Resolver ejercicios de Kotlin"),
        Tarea(id = UUID.randomUUID().toString(), titulo = "Repasar navegacion", completada = true)
    )

    val tareas: List<Tarea> get() = _tareas

    fun agregarTarea(titulo: String) {
        val limpio = titulo.trim()
        if (limpio.isEmpty()) return
        _tareas.add(Tarea(id = UUID.randomUUID().toString(), titulo = limpio))
    }

    fun eliminarTarea(id: String) {
        _tareas.removeAll { it.id == id }
    }

    fun alternarCompletada(id: String) {
        val index = _tareas.indexOfFirst { it.id == id }
        if (index >= 0) {
            val actual = _tareas[index]
            _tareas[index] = actual.copy(completada = !actual.completada)
        }
    }

    fun obtenerPorId(id: String): Tarea? = _tareas.firstOrNull { it.id == id }
}
