package com.unilibre.taller05.data.ai

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiClient @Inject constructor(
    private val model: GenerativeModel?
) {

    fun streamRecetas(ingredientes: List<String>): Flow<String> = flow {
        if (model == null) {
            emit("__ERROR__: " + ErrorTipo.SIN_API_KEY.codigo)
            return@flow
        }
        if (ingredientes.isEmpty()) {
            emit("__ERROR__: " + ErrorTipo.SIN_INGREDIENTES.codigo)
            return@flow
        }
        val prompt = """
            Tengo estos ingredientes: ${ingredientes.joinToString(", ")}.
            Sugiere 3 recetas posibles. Responde EXCLUSIVAMENTE con un JSON array valido (sin texto antes ni despues, sin markdown), donde cada elemento tiene los campos:
            - nombre (string)
            - tiempo_minutos (int)
            - dificultad (string: "Facil"|"Media"|"Dificil")
            - pasos (array of strings)
            - calorias (int)
        """.trimIndent()

        try {
            model.generateContentStream(prompt).collect { response ->
                response.text?.let { emit(it) }
            }
        } catch (t: Throwable) {
            emit("__ERROR__: " + clasificarError(t).codigo)
        }
    }.flowOn(Dispatchers.IO)

    private fun clasificarError(t: Throwable): ErrorTipo {
        val msg = (t.message ?: "").lowercase()
        return when {
            "503" in msg || "unavailable" in msg || "high demand" in msg -> ErrorTipo.MODELO_SATURADO
            "429" in msg || "quota" in msg || "rate" in msg -> ErrorTipo.LIMITE_USO
            "401" in msg || "403" in msg || "api key" in msg || "permission" in msg -> ErrorTipo.API_KEY_INVALIDA
            "network" in msg || "unable to resolve host" in msg || "timeout" in msg || "connect" in msg -> ErrorTipo.SIN_CONEXION
            "safety" in msg || "blocked" in msg -> ErrorTipo.BLOQUEADO_SEGURIDAD
            else -> ErrorTipo.DESCONOCIDO
        }
    }
}

/**
 * Tipos de error que la UI sabe interpretar.
 * El codigo se serializa en el flujo y la UI lo mapea a mensaje + icono.
 */
enum class ErrorTipo(val codigo: String) {
    SIN_API_KEY("SIN_API_KEY"),
    SIN_INGREDIENTES("SIN_INGREDIENTES"),
    MODELO_SATURADO("MODELO_SATURADO"),
    LIMITE_USO("LIMITE_USO"),
    API_KEY_INVALIDA("API_KEY_INVALIDA"),
    SIN_CONEXION("SIN_CONEXION"),
    BLOQUEADO_SEGURIDAD("BLOQUEADO_SEGURIDAD"),
    DESCONOCIDO("DESCONOCIDO");

    companion object {
        fun fromCodigo(codigo: String): ErrorTipo =
            values().firstOrNull { it.codigo == codigo.trim() } ?: DESCONOCIDO
    }
}
