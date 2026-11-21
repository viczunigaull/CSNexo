package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.Contacto
import kotlinx.coroutines.CancellationException

/**
 * Fuente de datos remota de contactos (habla con el microservicio).
 */
class ContactoServidor(
    private val api: ContactoApi
) {

    suspend fun listar(): Result<List<Contacto>> =
        ejecutar { api.listar().map { it.aDominio() } }

    suspend fun crear(contacto: Contacto): Result<Long> =
        ejecutar { api.crear(contacto.aDto()).id }

    suspend fun actualizar(contacto: Contacto): Result<Unit> =
        ejecutar {
            api.actualizar(contacto.id, contacto.aDto())
            Unit
        }

    suspend fun eliminar(id: Long): Result<Unit> =
        ejecutar {
            api.eliminar(id)
            Unit
        }

    private suspend fun <T> ejecutar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            Result.failure(error)
        }
}
