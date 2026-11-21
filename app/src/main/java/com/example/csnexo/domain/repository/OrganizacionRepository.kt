package com.example.csnexo.domain.repository

import com.example.csnexo.domain.model.Organizacion
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de acceso a organizaciones.
 */
interface OrganizacionRepository {

    fun observarTodas(): Flow<List<Organizacion>>

    suspend fun obtenerPorId(id: Long): Organizacion?

    suspend fun crear(organizacion: Organizacion): Result<Long>

    suspend fun actualizar(organizacion: Organizacion): Result<Unit>

    suspend fun eliminar(id: Long): Result<Unit>

    suspend fun reemplazarTodas(organizaciones: List<Organizacion>): Result<Unit>
}
