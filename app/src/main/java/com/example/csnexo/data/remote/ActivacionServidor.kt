package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.Activacion
import kotlinx.coroutines.CancellationException

class ActivacionServidor(
    private val api: ActivacionApi
) {

    suspend fun listar(): Result<List<Activacion>> =
        ejecutar { api.listar().map { it.aDominio() } }

    suspend fun crear(activacion: Activacion): Result<Long> =
        ejecutar { api.crear(activacion.aDto()).id }

    suspend fun actualizar(activacion: Activacion): Result<Unit> =
        ejecutar {
            api.actualizar(activacion.id, activacion.aDto())
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
