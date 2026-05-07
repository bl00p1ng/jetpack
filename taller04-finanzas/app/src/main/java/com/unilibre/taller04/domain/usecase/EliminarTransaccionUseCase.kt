package com.unilibre.taller04.domain.usecase

import com.unilibre.taller04.domain.model.Transaccion
import com.unilibre.taller04.domain.repository.TransaccionRepository
import javax.inject.Inject

class EliminarTransaccionUseCase @Inject constructor(
    private val repository: TransaccionRepository
) {
    suspend operator fun invoke(transaccion: Transaccion) {
        repository.eliminar(transaccion)
    }
}
