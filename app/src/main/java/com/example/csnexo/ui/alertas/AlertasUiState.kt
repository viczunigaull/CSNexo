package com.example.csnexo.ui.alertas

import com.example.csnexo.domain.model.Evento
import com.example.csnexo.domain.model.Seguimiento

/**
 * Estado de la pantalla de alertas (vista resumen de datos ya existentes).
 */
data class AlertasUiState(
    val eventosProximos: List<Evento> = emptyList(),
    val seguimientosRecientes: List<Seguimiento> = emptyList()
)
