package com.example.csnexo.ui.oportunidades.list

import com.example.csnexo.domain.model.EstadoOportunidad
import com.example.csnexo.domain.model.Oportunidad

data class ListaOportunidadesUiState(
    val cargando: Boolean = true,
    val oportunidades: List<Oportunidad> = emptyList(),
    val error: String? = null,
    val sincronizando: Boolean = false,
    val mensaje: String? = null,
    val filtroEstado: EstadoOportunidad? = null
) {
    val listaVacia: Boolean
        get() = !cargando && error == null && oportunidades.isEmpty()
}
