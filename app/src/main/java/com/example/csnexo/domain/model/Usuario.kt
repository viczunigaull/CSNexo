package com.example.csnexo.domain.model

/**
 * Usuario ficticio de la aplicación.
 *
 * No contiene clave ni datos personales reales: la autenticación de la primera
 * entrega se resuelve contra un repositorio local de demostración.
 */
data class Usuario(
    val id: String,
    val nombre: String,
    val usuario: String,
    val rol: RolUsuario
)
