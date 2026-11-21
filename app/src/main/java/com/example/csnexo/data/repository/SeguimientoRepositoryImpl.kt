package com.example.csnexo.data.repository

import com.example.csnexo.data.local.dao.SeguimientoDao
import com.example.csnexo.data.mapper.aDominio
import com.example.csnexo.data.mapper.aEntidad
import com.example.csnexo.domain.model.Seguimiento
import com.example.csnexo.domain.repository.SeguimientoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación local (Room) del repositorio de seguimientos.
 */
class SeguimientoRepositoryImpl(
    private val dao: SeguimientoDao
) : SeguimientoRepository {

    override fun observarTodos(): Flow<List<Seguimiento>> =
        dao.observarTodos().map { lista -> lista.map { it.aDominio() } }

    override suspend fun obtenerPorId(id: Long): Seguimiento? =
        dao.obtenerPorId(id)?.aDominio()

    override suspend fun crear(seguimiento: Seguimiento): Result<Long> =
        ejecutar { dao.insertar(seguimiento.aEntidad()) }

    override suspend fun actualizar(seguimiento: Seguimiento): Result<Unit> =
        ejecutar {
            dao.actualizar(seguimiento.aEntidad())
            Unit
        }

    override suspend fun eliminar(id: Long): Result<Unit> =
        ejecutar {
            dao.eliminarPorId(id)
            Unit
        }

    override suspend fun reemplazarTodos(seguimientos: List<Seguimiento>): Result<Unit> =
        ejecutar {
            dao.eliminarTodos()
            dao.insertarTodos(seguimientos.map { it.aEntidad() })
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
