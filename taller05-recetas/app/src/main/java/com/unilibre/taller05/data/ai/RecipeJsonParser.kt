package com.unilibre.taller05.data.ai

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.unilibre.taller05.domain.model.Receta

@JsonClass(generateAdapter = true)
data class RecetaDto(
    val nombre: String,
    val tiempo_minutos: Int = 0,
    val dificultad: String = "Media",
    val pasos: List<String> = emptyList(),
    val calorias: Int = 0
) {
    fun toDomain(ingredientesBase: List<String> = emptyList()) = Receta(
        nombre = nombre,
        tiempoMinutos = tiempo_minutos,
        dificultad = dificultad,
        pasos = pasos,
        calorias = calorias,
        ingredientesBase = ingredientesBase
    )
}

object RecipeJsonParser {
    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, RecetaDto::class.java)
    private val adapter = moshi.adapter<List<RecetaDto>>(type).lenient()

    fun parse(raw: String, ingredientesBase: List<String> = emptyList()): List<Receta> {
        val cleaned = clean(raw)
        return try {
            adapter.fromJson(cleaned)?.map { it.toDomain(ingredientesBase) } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun clean(raw: String): String {
        var s = raw.trim()
        // strip markdown fences
        if (s.startsWith("```")) {
            s = s.removePrefix("```json").removePrefix("```").trim()
            if (s.endsWith("```")) s = s.removeSuffix("```").trim()
        }
        // try to find first '[' and last ']'
        val start = s.indexOf('[')
        val end = s.lastIndexOf(']')
        if (start >= 0 && end > start) {
            s = s.substring(start, end + 1)
        }
        return s
    }
}
