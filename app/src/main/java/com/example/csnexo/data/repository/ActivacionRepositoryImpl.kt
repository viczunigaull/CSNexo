package com.example.csnexo.data.repository

import com.example.csnexo.data.local.dao.ActivacionDao
import com.example.csnexo.data.mapper.aDominio
import com.example.csnexo.data.mapper.aEntidad
import com.example.csnexo.domain.model.Activacion
import com.example.csnexo.domain.repository.ActivacionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ActivacionRepositoryImpl(
    private val dao: ActivacionDao
) : ActivacionRepository {

    override fun observarTodas(): Flow<List<Activacion>> =
        dao.observarTodas().map { lista -> lista.map { it.aDominio() } }

    override suspend fun obtenerPorId(id: Long): Activacion? =
        dao.obtenerPorId(id)?.aDominio()

    override suspend fun crear(activacion: Activacion): Result<Long> =
        ejecutar { dao.insertar(activacion.aEntidad()) }

    override suspend fun actualizar(activacion: Activacion): Result<Unit> =
        ejecutar {
            dao.actualizar(activacion.aEntidad())
            Unit
        }

    override suspend fun eliminar(id: Long): Result<Unit> =
        ejecutar {
            dao.eliminarPorId(id)
            Unit
        }

    override suspend fun reemplazarTodas(activaciones: List<Activacion>): Result<Unit> =
        ejecutar {
            dao.eliminarTodas()
            dao.insertarTodas(activaciones.map { it.aEntidad() })
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
