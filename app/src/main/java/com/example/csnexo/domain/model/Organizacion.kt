package com.example.csnexo.domain.model

/**
 * Organización responsable de eventos (empresa, municipalidad, etc.).
 * Datos ficticios; el tipo se guarda como enum.
 */
data class Organizacion(
    val id: Long = 0,
    val nombre: String,
    val tipo: TipoOrganizacion,
    val rubro: String,
    val comuna: String,
    val region: String
)
