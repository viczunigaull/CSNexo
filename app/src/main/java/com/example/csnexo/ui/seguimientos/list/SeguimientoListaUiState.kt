package com.example.csnexo.ui.seguimientos.list

import com.example.csnexo.domain.model.Seguimiento

data class SeguimientoListaUiState(
    val cargando: Boolean = true,
    val seguimientos: List<Seguimiento> = emptyList(),
    val error: String? = null,
    val sincronizando: Boolean = false,
    val mensaje: String? = null
) {
    val listaVacia: Boolean
        get() = !cargando && error == null && seguimientos.isEmpty()
}
