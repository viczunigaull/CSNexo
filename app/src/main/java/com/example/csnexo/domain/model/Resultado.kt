package com.example.csnexo.domain.model

/**
 * Resultado registrado de una activación.
 */
data class Resultado(
    val id: Long = 0,
    val oportunidad: String,
    val resultado: String,
    val observaciones: String
)
