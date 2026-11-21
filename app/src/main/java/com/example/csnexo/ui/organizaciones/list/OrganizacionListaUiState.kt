package com.example.csnexo.ui.organizaciones.list

import com.example.csnexo.domain.model.Organizacion

data class OrganizacionListaUiState(
    val cargando: Boolean = true,
    val organizaciones: List<Organizacion> = emptyList(),
    val error: String? = null,
    val sincronizando: Boolean = false,
    val mensaje: String? = null
) {
    val listaVacia: Boolean
        get() = !cargando && error == null && organizaciones.isEmpty()
}
