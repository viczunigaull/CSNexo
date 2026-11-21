package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.Evento
import kotlinx.coroutines.CancellationException

/**
 * Fuente de datos remota de eventos (habla con el microservicio).
 */
class EventoServidor(
    private val api: EventoApi
) {

    suspend fun listar(): Result<List<Evento>> =
        ejecutar { api.listar().map { it.aDominio() } }

    suspend fun crear(evento: Evento): Result<Long> =
        ejecutar { api.crear(evento.aDto()).id }

    suspend fun actualizar(evento: Evento): Result<Unit> =
        ejecutar {
            api.actualizar(evento.id, evento.aDto())
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
