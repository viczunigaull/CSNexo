package com.example.csnexo.ui.oportunidades.resumen

import com.example.csnexo.domain.model.Oportunidad

/**
 * Estado de la pantalla de resumen (detalle de una oportunidad guardada).
 */
data class ResumenUiState(
    val cargando: Boolean = true,
    val oportunidad: Oportunidad? = null,
    val error: String? = null
)
