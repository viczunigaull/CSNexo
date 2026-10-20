package com.example.csnexo.domain.repository

import com.example.csnexo.domain.model.Oportunidad
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de acceso a oportunidades.
 *
 * La UI solo ve modelos de dominio; Room, entidades y mappers quedan en la capa
 * de datos. Las operaciones de escritura devuelven `Result` para que el
 * ViewModel pueda mostrar errores sin recibir excepciones.
 */
interface OportunidadRepository {

    fun observarTodas(): Flow<List<Oportunidad>>

    suspend fun obtenerPorId(id: Long): Oportunidad?

    suspend fun crear(oportunidad: Oportunidad): Result<Long>

    suspend fun actualizar(oportunidad: Oportunidad): Result<Unit>
}
