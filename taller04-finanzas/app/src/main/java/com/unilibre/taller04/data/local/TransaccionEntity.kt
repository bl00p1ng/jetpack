package com.unilibre.taller04.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unilibre.taller04.domain.model.Categoria
import com.unilibre.taller04.domain.model.TipoTransaccion

@Entity(tableName = "transacciones")
data class TransaccionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val tipo: TipoTransaccion,
    val categoria: Categoria,
    val fecha: Long
)
