package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.Organizacion
import kotlinx.coroutines.CancellationException

/**
 * Fuente de datos remota de organizaciones (habla con el microservicio).
 */
class OrganizacionServidor(
    private val api: OrganizacionApi
) {

    suspend fun listar(): Result<List<Organizacion>> =
        ejecutar { api.listar().map { it.aDominio() } }

    suspend fun crear(organizacion: Organizacion): Result<Long> =
        ejecutar { api.crear(organizacion.aDto()).id }

    suspend fun actualizar(organizacion: Organizacion): Result<Unit> =
        ejecutar {
            api.actualizar(organizacion.id, organizacion.aDto())
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
