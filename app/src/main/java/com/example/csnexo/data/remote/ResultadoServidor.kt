package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.Resultado
import kotlinx.coroutines.CancellationException

class ResultadoServidor(
    private val api: ResultadoApi
) {

    suspend fun listar(): Result<List<Resultado>> =
        ejecutar { api.listar().map { it.aDominio() } }

    suspend fun crear(resultado: Resultado): Result<Long> =
        ejecutar { api.crear(resultado.aDto()).id }

    suspend fun actualizar(resultado: Resultado): Result<Unit> =
        ejecutar {
            api.actualizar(resultado.id, resultado.aDto())
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
