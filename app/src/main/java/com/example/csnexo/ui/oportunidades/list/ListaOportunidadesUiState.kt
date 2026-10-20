package com.example.csnexo.ui.oportunidades.list

import com.example.csnexo.domain.model.Oportunidad

data class ListaOportunidadesUiState(
    val cargando: Boolean = true,
    val oportunidades: List<Oportunidad> = emptyList(),
    val error: String? = null
) {
    val listaVacia: Boolean
        get() = !cargando && error == null && oportunidades.isEmpty()
}
