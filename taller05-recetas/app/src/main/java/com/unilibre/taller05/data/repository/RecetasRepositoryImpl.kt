package com.unilibre.taller05.data.repository

import com.unilibre.taller05.data.ai.GeminiClient
import com.unilibre.taller05.data.local.RecetaFavoritaDao
import com.unilibre.taller05.data.local.toDomain
import com.unilibre.taller05.data.local.toEntity
import com.unilibre.taller05.domain.model.Receta
import com.unilibre.taller05.domain.repository.RecetasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecetasRepositoryImpl @Inject constructor(
    private val dao: RecetaFavoritaDao,
    private val gemini: GeminiClient
) : RecetasRepository {

    override fun favoritas(): Flow<List<Receta>> =
        dao.observarTodas().map { list -> list.map { it.toDomain() } }

    override suspend fun obtener(id: Long): Receta? = dao.obtener(id)?.toDomain()

    override suspend fun obtenerPorNombre(nombre: String): Receta? =
        dao.obtenerPorNombre(nombre)?.toDomain()

    override suspend fun esFavorita(nombre: String): Boolean =
        dao.idPorNombre(nombre) != null

    override suspend fun guardar(receta: Receta): Long = dao.insertar(receta.toEntity())

    override suspend fun eliminar(id: Long) = dao.eliminar(id)

    override suspend fun toggleFavorita(receta: Receta): Long {
        val existing = dao.idPorNombre(receta.nombre)
        return if (existing != null) {
            dao.eliminar(existing)
            -1L
        } else {
            dao.insertar(receta.toEntity())
        }
    }

    override fun streamRecetasIA(ingredientes: List<String>): Flow<String> =
        gemini.streamRecetas(ingredientes)
}
