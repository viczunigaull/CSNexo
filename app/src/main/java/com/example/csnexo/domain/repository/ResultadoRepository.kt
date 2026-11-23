package com.example.csnexo.domain.repository

import com.example.csnexo.domain.model.Resultado
import kotlinx.coroutines.flow.Flow

interface ResultadoRepository {

    fun observarTodos(): Flow<List<Resultado>>

    suspend fun obtenerPorId(id: Long): Resultado?

    suspend fun crear(resultado: Resultado): Result<Long>

    suspend fun actualizar(resultado: Resultado): Result<Unit>

    suspend fun eliminar(id: Long): Result<Unit>

    suspend fun reemplazarTodos(resultados: List<Resultado>): Result<Unit>
}
