package com.example.csnexo.ui.activaciones.form

enum class ModoActivacion {
    CREAR,
    EDITAR
}

data class ActivacionFormUiState(
    val modo: ModoActivacion = ModoActivacion.CREAR,
    val cargando: Boolean = false,
    val guardando: Boolean = false,
    val oportunidad: String = "",
    val condiciones: String = "",
    val responsable: String = "",
    val oportunidadError: String? = null,
    val condicionesError: String? = null,
    val responsableError: String? = null,
    val errorGeneral: String? = null,
    val guardadoExitosamente: Boolean = false
) {
    val esEdicion: Boolean
        get() = modo == ModoActivacion.EDITAR
}
