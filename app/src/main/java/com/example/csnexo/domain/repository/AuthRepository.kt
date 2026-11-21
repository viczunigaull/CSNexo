package com.example.csnexo.domain.repository

import com.example.csnexo.domain.model.Usuario

/**
 * Contrato de autenticación. En la primera entrega se implementa con usuarios
 * ficticios; en el alcance completo podría respaldarse con Room o un servicio.
 */
interface AuthRepository {

    suspend fun autenticar(usuario: String, clave: String): Result<Usuario>

    suspend fun buscarPorUsuario(usuario: String): Usuario?
}

class CredencialesInvalidasException : Exception("Credenciales inválidas")
