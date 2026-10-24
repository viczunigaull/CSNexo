package com.example.csnexo.data.repository

import com.example.csnexo.data.local.dao.OportunidadDao
import com.example.csnexo.data.mapper.aDominio
import com.example.csnexo.data.mapper.aEntidad
import com.example.csnexo.domain.model.Oportunidad
import com.example.csnexo.domain.repository.OportunidadRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación local de oportunidades. La UI sigue recibiendo modelos de
 * dominio; el DAO y la entidad quedan encapsulados aquí.
 */
class OportunidadRepositoryImpl(
    private val dao: OportunidadDao
) : OportunidadRepository {

    override fun observarTodas(): Flow<List<Oportunidad>> =
        dao.observarTodas().map { entidades ->
            entidades.map { it.aDominio() }
        }

    override suspend fun obtenerPorId(id: Long): Oportunidad? =
        dao.obtenerPorId(id)?.aDominio()

    override suspend fun crear(oportunidad: Oportunidad): Result<Long> =
        ejecutar { dao.insertar(oportunidad.aEntidad()) }

    override suspend fun actualizar(oportunidad: Oportunidad): Result<Unit> =
        ejecutar {
            dao.actualizar(oportunidad.aEntidad())
            Unit
        }

    // Convierte excepciones de Room en Result para que el ViewModel no reciba
    // excepciones crudas. CancellationException se vuelve a lanzar.
    private suspend fun <T> ejecutar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            Result.failure(error)
        }
}
