package com.example.csnexo.domain.model

/**
 * Activación: coordinación de la participación en un evento/oportunidad.
 */
data class Activacion(
    val id: Long = 0,
    val oportunidad: String,
    val condiciones: String,
    val responsable: String
)
