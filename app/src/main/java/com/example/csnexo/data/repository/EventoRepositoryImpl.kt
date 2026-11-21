package com.example.csnexo.data.repository

import com.example.csnexo.data.local.dao.EventoDao
import com.example.csnexo.data.mapper.aDominio
import com.example.csnexo.data.mapper.aEntidad
import com.example.csnexo.domain.model.Evento
import com.example.csnexo.domain.repository.EventoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación local (Room) del repositorio de eventos.
 */
class EventoRepositoryImpl(
    private val dao: EventoDao
) : EventoRepository {

    override fun observarTodos(): Flow<List<Evento>> =
        dao.observarTodos().map { lista -> lista.map { it.aDominio() } }

    override suspend fun obtenerPorId(id: Long): Evento? =
        dao.obtenerPorId(id)?.aDominio()

    override suspend fun crear(evento: Evento): Result<Long> =
        ejecutar { dao.insertar(evento.aEntidad()) }

    override suspend fun actualizar(evento: Evento): Result<Unit> =
        ejecutar {
            dao.actualizar(evento.aEntidad())
            Unit
        }

    override suspend fun eliminar(id: Long): Result<Unit> =
        ejecutar {
            dao.eliminarPorId(id)
            Unit
        }

    override suspend fun reemplazarTodos(eventos: List<Evento>): Result<Unit> =
        ejecutar {
            dao.eliminarTodos()
            dao.insertarTodos(eventos.map { it.aEntidad() })
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
