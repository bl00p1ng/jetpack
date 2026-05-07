package com.unilibre.taller04.domain.usecase

import com.unilibre.taller04.domain.model.Transaccion
import com.unilibre.taller04.domain.repository.TransaccionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObtenerTransaccionesUseCase @Inject constructor(
    private val repository: TransaccionRepository
) {
    operator fun invoke(): Flow<List<Transaccion>> = repository.observarTodas()
}
