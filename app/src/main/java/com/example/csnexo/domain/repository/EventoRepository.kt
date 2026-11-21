package com.example.csnexo.domain.repository

import com.example.csnexo.domain.model.Evento
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de acceso a eventos.
 */
interface EventoRepository {

    fun observarTodos(): Flow<List<Evento>>

    suspend fun obtenerPorId(id: Long): Evento?

    suspend fun crear(evento: Evento): Result<Long>

    suspend fun actualizar(evento: Evento): Result<Unit>

    suspend fun eliminar(id: Long): Result<Unit>

    suspend fun reemplazarTodos(eventos: List<Evento>): Result<Unit>
}
