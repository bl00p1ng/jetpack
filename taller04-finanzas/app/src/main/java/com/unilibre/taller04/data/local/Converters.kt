package com.unilibre.taller04.data.local

import androidx.room.TypeConverter
import com.unilibre.taller04.domain.model.Categoria
import com.unilibre.taller04.domain.model.TipoTransaccion

class Converters {
    @TypeConverter fun fromTipo(t: TipoTransaccion): String = t.name
    @TypeConverter fun toTipo(v: String): TipoTransaccion = TipoTransaccion.valueOf(v)

    @TypeConverter fun fromCategoria(c: Categoria): String = c.name
    @TypeConverter fun toCategoria(v: String): Categoria = Categoria.valueOf(v)
}
