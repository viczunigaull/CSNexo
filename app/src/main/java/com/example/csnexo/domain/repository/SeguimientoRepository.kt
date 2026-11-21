package com.example.csnexo.domain.repository

import com.example.csnexo.domain.model.Seguimiento
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de acceso a seguimientos.
 */
interface SeguimientoRepository {

    fun observarTodos(): Flow<List<Seguimiento>>

    suspend fun obtenerPorId(id: Long): Seguimiento?

    suspend fun crear(seguimiento: Seguimiento): Result<Long>

    suspend fun actualizar(seguimiento: Seguimiento): Result<Unit>

    suspend fun eliminar(id: Long): Result<Unit>

    suspend fun reemplazarTodos(seguimientos: List<Seguimiento>): Result<Unit>
}
