package com.example.csnexo.ui.contactos.list

import com.example.csnexo.domain.model.Contacto

data class ContactoListaUiState(
    val cargando: Boolean = true,
    val contactos: List<Contacto> = emptyList(),
    val error: String? = null,
    val sincronizando: Boolean = false,
    val mensaje: String? = null
) {
    val listaVacia: Boolean
        get() = !cargando && error == null && contactos.isEmpty()
}
