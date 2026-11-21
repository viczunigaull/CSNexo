package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.Oportunidad
import kotlinx.coroutines.CancellationException

/**
 * Fuente de datos remota: habla con el microservicio Spring Boot vía Retrofit.
 *
 * Cada método devuelve un `Result` para que el ViewModel maneje los errores sin
 * recibir excepciones. CancellationException se vuelve a lanzar para no romper
 * la cancelación de corrutinas.
 */
class OportunidadServidor(
    private val api: OportunidadApi
) {

    suspend fun listar(): Result<List<Oportunidad>> =
        ejecutar { api.listar().map { it.aDominio() } }

    suspend fun crear(oportunidad: Oportunidad): Result<Long> =
        ejecutar { api.crear(oportunidad.aDto()).id }

    suspend fun actualizar(oportunidad: Oportunidad): Result<Unit> =
        ejecutar {
            api.actualizar(oportunidad.id, oportunidad.aDto())
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
