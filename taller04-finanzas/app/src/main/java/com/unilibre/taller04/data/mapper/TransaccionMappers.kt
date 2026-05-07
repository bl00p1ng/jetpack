package com.unilibre.taller04.data.mapper

import com.unilibre.taller04.data.local.TransaccionEntity
import com.unilibre.taller04.domain.model.Transaccion

fun TransaccionEntity.toDomain(): Transaccion = Transaccion(
    id = id,
    descripcion = descripcion,
    monto = monto,
    tipo = tipo,
    categoria = categoria,
    fecha = fecha
)

fun Transaccion.toEntity(): TransaccionEntity = TransaccionEntity(
    id = id,
    descripcion = descripcion,
    monto = monto,
    tipo = tipo,
    categoria = categoria,
    fecha = fecha
)
