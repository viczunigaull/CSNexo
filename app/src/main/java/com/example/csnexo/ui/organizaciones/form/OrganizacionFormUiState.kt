package com.example.csnexo.ui.organizaciones.form

import com.example.csnexo.domain.model.TipoOrganizacion

enum class ModoOrganizacion {
    CREAR,
    EDITAR
}

data class OrganizacionFormUiState(
    val modo: ModoOrganizacion = ModoOrganizacion.CREAR,
    val cargando: Boolean = false,
    val guardando: Boolean = false,
    val nombre: String = "",
    val tipo: TipoOrganizacion? = null,
    val rubro: String = "",
    val comuna: String = "",
    val region: String = "",
    val nombreError: String? = null,
    val tipoError: String? = null,
    val rubroError: String? = null,
    val comunaError: String? = null,
    val regionError: String? = null,
    val errorGeneral: String? = null,
    val guardadoExitosamente: Boolean = false,
    val eliminando: Boolean = false,
    val eliminado: Boolean = false
) {
    val esEdicion: Boolean
        get() = modo == ModoOrganizacion.EDITAR
}
