package com.example.csnexo.ui.oportunidades.form

import com.example.csnexo.domain.model.EstadoOportunidad
import com.example.csnexo.domain.model.PotencialComercial
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad

enum class ModoFormulario {
    CREAR,
    EDITAR
}

/**
 * Estado del formulario de oportunidad.
 *
 * Los errores son textos listos para `supportingText`; se controlan desde el
 * ViewModel y la pantalla solo los muestra.
 */
data class OportunidadFormUiState(
    val modo: ModoFormulario = ModoFormulario.CREAR,
    val cargando: Boolean = false,
    val guardando: Boolean = false,
    val nombre: String = "",
    val tipo: TipoOportunidad? = null,
    val descripcion: String = "",
    val fuenteInformacion: String = "",
    val prioridad: Prioridad? = null,
    val potencial: PotencialComercial? = null,
    val estado: EstadoOportunidad = EstadoOportunidad.EVENTO_IDENTIFICADO,
    val observaciones: String = "",
    val fotoPath: String? = null,
    val ubicacionReferencia: String? = null,
    val nombreError: String? = null,
    val tipoError: String? = null,
    val descripcionError: String? = null,
    val fuenteError: String? = null,
    val prioridadError: String? = null,
    val potencialError: String? = null,
    val errorGeneral: String? = null,
    val guardadoExitosamente: Boolean = false
) {
    val esEdicion: Boolean
        get() = modo == ModoFormulario.EDITAR
}
