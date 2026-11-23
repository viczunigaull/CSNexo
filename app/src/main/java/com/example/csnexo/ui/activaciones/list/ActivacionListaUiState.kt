package com.example.csnexo.ui.activaciones.list

import com.example.csnexo.domain.model.Activacion

data class ActivacionListaUiState(
    val cargando: Boolean = true,
    val activaciones: List<Activacion> = emptyList(),
    val error: String? = null,
    val sincronizando: Boolean = false,
    val mensaje: String? = null
)
