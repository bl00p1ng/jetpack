package com.unilibre.taller05.presentation

import com.unilibre.taller05.domain.model.Receta
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Estado compartido en memoria entre pantallas Camara -> Ingredientes -> Recetas IA.
 * Para una app real conviene usar Navigation type-safe; aqui simplificamos.
 */
@Singleton
class SharedSession @Inject constructor() {
    val ingredientes = MutableStateFlow<List<String>>(emptyList())
    val recetasGeneradas = MutableStateFlow<List<Receta>>(emptyList())
}
