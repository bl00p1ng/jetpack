package com.unilibre.taller04.presentation.dashboard

import com.unilibre.taller04.domain.model.PuntoMensual
import com.unilibre.taller04.domain.model.Resumen
import com.unilibre.taller04.domain.model.Transaccion

data class DashboardUiState(
    val resumen: Resumen = Resumen(0.0, 0.0),
    val recientes: List<Transaccion> = emptyList(),
    val serieMensual: List<PuntoMensual> = emptyList(),
    val cargando: Boolean = true
)
