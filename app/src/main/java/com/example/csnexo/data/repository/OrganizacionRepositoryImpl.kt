package com.example.csnexo.data.repository

import com.example.csnexo.data.local.dao.OrganizacionDao
import com.example.csnexo.data.mapper.aDominio
import com.example.csnexo.data.mapper.aEntidad
import com.example.csnexo.domain.model.Organizacion
import com.example.csnexo.domain.repository.OrganizacionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación local (Room) del repositorio de organizaciones.
 */
class OrganizacionRepositoryImpl(
    private val dao: OrganizacionDao
) : OrganizacionRepository {

    override fun observarTodas(): Flow<List<Organizacion>> =
        dao.observarTodas().map { lista -> lista.map { it.aDominio() } }

    override suspend fun obtenerPorId(id: Long): Organizacion? =
        dao.obtenerPorId(id)?.aDominio()

    override suspend fun crear(organizacion: Organizacion): Result<Long> =
        ejecutar { dao.insertar(organizacion.aEntidad()) }

    override suspend fun actualizar(organizacion: Organizacion): Result<Unit> =
        ejecutar {
            dao.actualizar(organizacion.aEntidad())
            Unit
        }

    override suspend fun eliminar(id: Long): Result<Unit> =
        ejecutar {
            dao.eliminarPorId(id)
            Unit
        }

    override suspend fun reemplazarTodas(organizaciones: List<Organizacion>): Result<Unit> =
        ejecutar {
            dao.eliminarTodas()
            dao.insertarTodas(organizaciones.map { it.aEntidad() })
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
