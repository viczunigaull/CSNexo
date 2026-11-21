package com.example.csnexo.ui.seguimientos.form

import com.example.csnexo.domain.model.TipoAccion

enum class ModoSeguimiento {
    CREAR,
    EDITAR
}

data class SeguimientoFormUiState(
    val modo: ModoSeguimiento = ModoSeguimiento.CREAR,
    val cargando: Boolean = false,
    val guardando: Boolean = false,
    val oportunidad: String = "",
    val tipoAccion: TipoAccion? = null,
    val fecha: String = "",
    val resultado: String = "",
    val oportunidadError: String? = null,
    val tipoAccionError: String? = null,
    val fechaError: String? = null,
    val resultadoError: String? = null,
    val errorGeneral: String? = null,
    val guardadoExitosamente: Boolean = false,
    val eliminando: Boolean = false,
    val eliminado: Boolean = false
) {
    val esEdicion: Boolean
        get() = modo == ModoSeguimiento.EDITAR
}
