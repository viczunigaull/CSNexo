package com.example.csnexo.ui.eventos.form

import com.example.csnexo.domain.model.TipoEvento

enum class ModoEvento {
    CREAR,
    EDITAR
}

data class EventoFormUiState(
    val modo: ModoEvento = ModoEvento.CREAR,
    val cargando: Boolean = false,
    val guardando: Boolean = false,
    val nombre: String = "",
    val tipo: TipoEvento? = null,
    val descripcion: String = "",
    val fechaInicio: String = "",
    val fechaTermino: String = "",
    val comuna: String = "",
    val region: String = "",
    val nombreError: String? = null,
    val tipoError: String? = null,
    val descripcionError: String? = null,
    val fechaInicioError: String? = null,
    val fechaTerminoError: String? = null,
    val comunaError: String? = null,
    val regionError: String? = null,
    val errorGeneral: String? = null,
    val guardadoExitosamente: Boolean = false,
    val eliminando: Boolean = false,
    val eliminado: Boolean = false
) {
    val esEdicion: Boolean
        get() = modo == ModoEvento.EDITAR
}
