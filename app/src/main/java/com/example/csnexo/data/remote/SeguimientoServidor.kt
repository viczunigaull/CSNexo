package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.Seguimiento
import kotlinx.coroutines.CancellationException

/**
 * Fuente de datos remota de seguimientos (habla con el microservicio).
 */
class SeguimientoServidor(
    private val api: SeguimientoApi
) {

    suspend fun listar(): Result<List<Seguimiento>> =
        ejecutar { api.listar().map { it.aDominio() } }

    suspend fun crear(seguimiento: Seguimiento): Result<Long> =
        ejecutar { api.crear(seguimiento.aDto()).id }

    suspend fun actualizar(seguimiento: Seguimiento): Result<Unit> =
        ejecutar {
            api.actualizar(seguimiento.id, seguimiento.aDto())
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
