package com.example.csnexo.domain.repository

import com.example.csnexo.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

/**
 * Sesión de la aplicación.
 *
 * `sesion` es un flujo observado por el ViewModel raíz. La persistencia real
 * (DataStore) queda encapsulada en la implementación.
 */
interface SesionRepository {

    val sesion: Flow<Usuario?>

    suspend fun iniciarSesion(usuario: String, clave: String): Result<Usuario>

    suspend fun cerrarSesion()
}
