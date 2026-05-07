package com.unilibre.taller04.data.repository

import com.unilibre.taller04.data.local.TransaccionDao
import com.unilibre.taller04.data.mapper.toDomain
import com.unilibre.taller04.data.mapper.toEntity
import com.unilibre.taller04.domain.model.Transaccion
import com.unilibre.taller04.domain.repository.TransaccionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransaccionRepositoryImpl @Inject constructor(
    private val dao: TransaccionDao
) : TransaccionRepository {
    override fun observarTodas(): Flow<List<Transaccion>> =
        dao.observarTodas().map { lista -> lista.map { it.toDomain() } }

    override suspend fun insertar(transaccion: Transaccion): Long =
        dao.insertar(transaccion.toEntity())

    override suspend fun eliminar(transaccion: Transaccion) =
        dao.eliminar(transaccion.toEntity())

    override suspend fun obtenerPorId(id: Int): Transaccion? =
        dao.obtenerPorId(id)?.toDomain()
}
