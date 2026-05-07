package com.unilibre.taller04.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaccionDao {
    @Query("SELECT * FROM transacciones ORDER BY fecha DESC")
    fun observarTodas(): Flow<List<TransaccionEntity>>

    @Query("SELECT * FROM transacciones WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): TransaccionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(entity: TransaccionEntity): Long

    @Delete
    suspend fun eliminar(entity: TransaccionEntity)
}
