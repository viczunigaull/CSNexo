package com.example.csnexo.data.repository

import com.example.csnexo.domain.model.RolUsuario
import com.example.csnexo.domain.model.Usuario
import com.example.csnexo.domain.repository.AuthRepository
import com.example.csnexo.domain.repository.CredencialesInvalidasException

/**
 * Usuarios ficticios de demostración.
 *
 * Las claves son deliberadamente visibles y no corresponden a personas ni
 * servicios reales. No se deben reutilizar credenciales reales en este proyecto.
 */
class FakeAuthRepository : AuthRepository {

    private data class UsuarioFicticio(
        val datos: Usuario,
        val clave: String
    )

    private val usuarios = listOf(
        UsuarioFicticio(
            datos = Usuario(
                id = "u-ejecutivo",
                nombre = "Ejecutiva Demo",
                usuario = "ejecutivo.demo",
                rol = RolUsuario.EJECUTIVO
            ),
            clave = "demo1234"
        ),
        UsuarioFicticio(
            datos = Usuario(
                id = "u-supervisor",
                nombre = "Supervisor Demo",
                usuario = "supervisor.demo",
                rol = RolUsuario.SUPERVISOR
            ),
            clave = "demo1234"
        )
    )

    override suspend fun autenticar(usuario: String, clave: String): Result<Usuario> {
        val encontrado = usuarios.firstOrNull {
            it.datos.usuario.equals(usuario.trim(), ignoreCase = true) && it.clave == clave
        }
        return if (encontrado != null) {
            Result.success(encontrado.datos)
        } else {
            Result.failure(CredencialesInvalidasException())
        }
    }

    override suspend fun buscarPorUsuario(usuario: String): Usuario? =
        usuarios.firstOrNull {
            it.datos.usuario.equals(usuario, ignoreCase = true)
        }?.datos
}
