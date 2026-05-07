package com.unilibre.taller05.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unilibre.taller05.domain.model.Receta

@Entity(tableName = "recetas_favoritas")
data class RecetaFavoritaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val tiempoMinutos: Int,
    val dificultad: String,
    val pasos: List<String>,
    val calorias: Int,
    val ingredientesBase: List<String>
)

fun RecetaFavoritaEntity.toDomain() = Receta(
    id = id,
    nombre = nombre,
    tiempoMinutos = tiempoMinutos,
    dificultad = dificultad,
    pasos = pasos,
    calorias = calorias,
    ingredientesBase = ingredientesBase,
    esFavorita = true
)

fun Receta.toEntity() = RecetaFavoritaEntity(
    id = id,
    nombre = nombre,
    tiempoMinutos = tiempoMinutos,
    dificultad = dificultad,
    pasos = pasos,
    calorias = calorias,
    ingredientesBase = ingredientesBase
)
