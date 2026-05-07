package com.unilibre.taller05.domain.model

data class Receta(
    val id: Long = 0L,
    val nombre: String,
    val tiempoMinutos: Int,
    val dificultad: String,
    val pasos: List<String>,
    val calorias: Int,
    val ingredientesBase: List<String> = emptyList(),
    val esFavorita: Boolean = false
)
