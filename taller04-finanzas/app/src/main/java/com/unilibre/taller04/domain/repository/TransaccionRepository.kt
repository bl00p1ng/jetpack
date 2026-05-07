package com.unilibre.taller04.domain.repository

import com.unilibre.taller04.domain.model.Transaccion
import kotlinx.coroutines.flow.Flow

interface TransaccionRepository {
    fun observarTodas(): Flow<List<Transaccion>>
    suspend fun insertar(transaccion: Transaccion): Long
    suspend fun eliminar(transaccion: Transaccion)
    suspend fun obtenerPorId(id: Int): Transaccion?
}
