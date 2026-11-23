package com.example.csnexo.ui.resultados.form

enum class ModoResultado {
    CREAR,
    EDITAR
}

data class ResultadoFormUiState(
    val modo: ModoResultado = ModoResultado.CREAR,
    val cargando: Boolean = false,
    val guardando: Boolean = false,
    val oportunidad: String = "",
    val resultado: String = "",
    val observaciones: String = "",
    val oportunidadError: String? = null,
    val resultadoError: String? = null,
    val observacionesError: String? = null,
    val errorGeneral: String? = null,
    val guardadoExitosamente: Boolean = false
) {
    val esEdicion: Boolean
        get() = modo == ModoResultado.EDITAR
}
