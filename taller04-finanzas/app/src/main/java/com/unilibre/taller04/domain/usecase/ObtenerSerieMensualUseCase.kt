package com.unilibre.taller04.domain.usecase

import com.unilibre.taller04.domain.model.PuntoMensual
import com.unilibre.taller04.domain.model.TipoTransaccion
import com.unilibre.taller04.domain.repository.TransaccionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class ObtenerSerieMensualUseCase @Inject constructor(
    private val repository: TransaccionRepository
) {
    private val etiquetas = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

    operator fun invoke(): Flow<List<PuntoMensual>> =
        repository.observarTodas().map { lista ->
            val ahora = Calendar.getInstance()
            val resultado = mutableListOf<PuntoMensual>()
            // últimos 6 meses, del más antiguo al más reciente
            for (offset in 5 downTo 0) {
                val cal = (ahora.clone() as Calendar).apply {
                    add(Calendar.MONTH, -offset)
                }
                val mes = cal.get(Calendar.MONTH)
                val anio = cal.get(Calendar.YEAR)
                val items = lista.filter {
                    val c = Calendar.getInstance().apply { timeInMillis = it.fecha }
                    c.get(Calendar.MONTH) == mes && c.get(Calendar.YEAR) == anio
                }
                val ingresos = items.filter { it.tipo == TipoTransaccion.INGRESO }.sumOf { it.monto }
                val gastos = items.filter { it.tipo == TipoTransaccion.GASTO }.sumOf { it.monto }
                resultado += PuntoMensual(etiquetas[mes], ingresos, gastos)
            }
            resultado
        }
}
