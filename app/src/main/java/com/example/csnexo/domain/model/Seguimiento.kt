package com.example.csnexo.domain.model

/**
 * Acción de seguimiento sobre una oportunidad.
 */
data class Seguimiento(
    val id: Long = 0,
    val oportunidad: String,
    val tipoAccion: TipoAccion,
    val fecha: String,
    val resultado: String
)
