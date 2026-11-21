package com.example.csnexo.ui.eventos.list

import com.example.csnexo.domain.model.Evento

data class EventoListaUiState(
    val cargando: Boolean = true,
    val eventos: List<Evento> = emptyList(),
    val error: String? = null,
    val sincronizando: Boolean = false,
    val mensaje: String? = null
) {
    val listaVacia: Boolean
        get() = !cargando && error == null && eventos.isEmpty()
}
