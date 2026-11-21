package com.example.csnexo.ui.contactos.form

import com.example.csnexo.domain.model.AreaContacto

enum class ModoContacto {
    CREAR,
    EDITAR
}

data class ContactoFormUiState(
    val modo: ModoContacto = ModoContacto.CREAR,
    val cargando: Boolean = false,
    val guardando: Boolean = false,
    val nombre: String = "",
    val organizacion: String = "",
    val cargo: String = "",
    val area: AreaContacto? = null,
    val correo: String = "",
    val telefono: String = "",
    val nombreError: String? = null,
    val organizacionError: String? = null,
    val cargoError: String? = null,
    val areaError: String? = null,
    val correoError: String? = null,
    val telefonoError: String? = null,
    val errorGeneral: String? = null,
    val guardadoExitosamente: Boolean = false,
    val eliminando: Boolean = false,
    val eliminado: Boolean = false
) {
    val esEdicion: Boolean
        get() = modo == ModoContacto.EDITAR
}
