package com.unilibre.taller04.domain.model

data class Resumen(
    val totalIngresos: Double,
    val totalGastos: Double
) {
    val balance: Double get() = totalIngresos - totalGastos
}

data class PuntoMensual(
    val etiqueta: String,
    val ingresos: Double,
    val gastos: Double
)
