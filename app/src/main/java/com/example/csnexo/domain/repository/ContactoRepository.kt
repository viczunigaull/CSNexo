package com.example.csnexo.domain.repository

import com.example.csnexo.domain.model.Contacto
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de acceso a contactos.
 */
interface ContactoRepository {

    fun observarTodos(): Flow<List<Contacto>>

    suspend fun obtenerPorId(id: Long): Contacto?

    suspend fun crear(contacto: Contacto): Result<Long>

    suspend fun actualizar(contacto: Contacto): Result<Unit>

    suspend fun eliminar(id: Long): Result<Unit>

    suspend fun reemplazarTodos(contactos: List<Contacto>): Result<Unit>
}
