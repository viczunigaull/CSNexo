package com.example.csnexo.domain.repository

import com.example.csnexo.domain.model.Activacion
import kotlinx.coroutines.flow.Flow

interface ActivacionRepository {

    fun observarTodas(): Flow<List<Activacion>>

    suspend fun obtenerPorId(id: Long): Activacion?

    suspend fun crear(activacion: Activacion): Result<Long>

    suspend fun actualizar(activacion: Activacion): Result<Unit>

    suspend fun eliminar(id: Long): Result<Unit>

    suspend fun reemplazarTodas(activaciones: List<Activacion>): Result<Unit>
}
