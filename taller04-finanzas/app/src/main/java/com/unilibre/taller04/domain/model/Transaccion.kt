package com.unilibre.taller04.domain.model

data class Transaccion(
    val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val tipo: TipoTransaccion,
    val categoria: Categoria,
    val fecha: Long
)
