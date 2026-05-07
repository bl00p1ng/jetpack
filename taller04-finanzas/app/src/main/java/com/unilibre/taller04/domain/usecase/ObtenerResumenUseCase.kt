package com.unilibre.taller04.domain.usecase

import com.unilibre.taller04.domain.model.Resumen
import com.unilibre.taller04.domain.model.TipoTransaccion
import com.unilibre.taller04.domain.repository.TransaccionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObtenerResumenUseCase @Inject constructor(
    private val repository: TransaccionRepository
) {
    operator fun invoke(): Flow<Resumen> =
        repository.observarTodas().map { lista ->
            val ingresos = lista.filter { it.tipo == TipoTransaccion.INGRESO }.sumOf { it.monto }
            val gastos = lista.filter { it.tipo == TipoTransaccion.GASTO }.sumOf { it.monto }
            Resumen(ingresos, gastos)
        }
}
