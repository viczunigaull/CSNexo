package com.example.csnexo.ui.feriados

import com.example.csnexo.data.remote.FeriadoDto

/**
 * Estado de la pantalla de feriados.
 */
data class FeriadosUiState(
    val cargando: Boolean = true,
    val feriados: List<FeriadoDto> = emptyList(),
    val error: String? = null
)
