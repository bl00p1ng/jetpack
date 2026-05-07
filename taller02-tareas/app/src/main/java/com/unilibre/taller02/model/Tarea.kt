package com.unilibre.taller02.model

data class Tarea(
    val id: String,
    val titulo: String,
    val completada: Boolean = false
)
