package com.unilibre.taller05.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecetaFavoritaDao {
    @Query("SELECT * FROM recetas_favoritas ORDER BY id DESC")
    fun observarTodas(): Flow<List<RecetaFavoritaEntity>>

    @Query("SELECT * FROM recetas_favoritas WHERE id = :id")
    suspend fun obtener(id: Long): RecetaFavoritaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(entity: RecetaFavoritaEntity): Long

    @Query("DELETE FROM recetas_favoritas WHERE id = :id")
    suspend fun eliminar(id: Long)

    @Query("SELECT id FROM recetas_favoritas WHERE nombre = :nombre LIMIT 1")
    suspend fun idPorNombre(nombre: String): Long?

    @Query("SELECT * FROM recetas_favoritas WHERE nombre = :nombre LIMIT 1")
    suspend fun obtenerPorNombre(nombre: String): RecetaFavoritaEntity?
}
