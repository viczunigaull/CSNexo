package com.example.csnexo.domain.model

/**
 * Contacto corporativo ficticio (nombre, cargo, área, correo y teléfono).
 */
data class Contacto(
    val id: Long = 0,
    val nombre: String,
    val organizacion: String,
    val cargo: String,
    val area: AreaContacto,
    val correo: String,
    val telefono: String
)
