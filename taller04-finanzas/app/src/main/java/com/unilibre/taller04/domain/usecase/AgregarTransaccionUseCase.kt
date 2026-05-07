package com.unilibre.taller04.domain.usecase

import com.unilibre.taller04.domain.model.Transaccion
import com.unilibre.taller04.domain.repository.TransaccionRepository
import javax.inject.Inject

class AgregarTransaccionUseCase @Inject constructor(
    private val repository: TransaccionRepository
) {
    suspend operator fun invoke(transaccion: Transaccion): Result<Long> {
        if (transaccion.descripcion.isBlank()) {
            return Result.failure(IllegalArgumentException("La descripción es obligatoria"))
        }
        if (transaccion.monto <= 0.0) {
            return Result.failure(IllegalArgumentException("El monto debe ser mayor que 0"))
        }
        return runCatching { repository.insertar(transaccion) }
    }
}
