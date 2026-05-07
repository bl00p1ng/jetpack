package com.unilibre.taller05.domain.repository

import com.unilibre.taller05.domain.model.Receta
import kotlinx.coroutines.flow.Flow

interface RecetasRepository {
    fun favoritas(): Flow<List<Receta>>
    suspend fun obtener(id: Long): Receta?
    suspend fun obtenerPorNombre(nombre: String): Receta?
    suspend fun esFavorita(nombre: String): Boolean
    suspend fun guardar(receta: Receta): Long
    suspend fun eliminar(id: Long)
    suspend fun toggleFavorita(receta: Receta): Long
    fun streamRecetasIA(ingredientes: List<String>): Flow<String>
}
