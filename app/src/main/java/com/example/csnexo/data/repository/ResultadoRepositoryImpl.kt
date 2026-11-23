package com.example.csnexo.data.repository

import com.example.csnexo.data.local.dao.ResultadoDao
import com.example.csnexo.data.mapper.aDominio
import com.example.csnexo.data.mapper.aEntidad
import com.example.csnexo.domain.model.Resultado
import com.example.csnexo.domain.repository.ResultadoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ResultadoRepositoryImpl(
    private val dao: ResultadoDao
) : ResultadoRepository {

    override fun observarTodos(): Flow<List<Resultado>> =
        dao.observarTodos().map { lista -> lista.map { it.aDominio() } }

    override suspend fun obtenerPorId(id: Long): Resultado? =
        dao.obtenerPorId(id)?.aDominio()

    override suspend fun crear(resultado: Resultado): Result<Long> =
        ejecutar { dao.insertar(resultado.aEntidad()) }

    override suspend fun actualizar(resultado: Resultado): Result<Unit> =
        ejecutar {
            dao.actualizar(resultado.aEntidad())
            Unit
        }

    override suspend fun eliminar(id: Long): Result<Unit> =
        ejecutar {
            dao.eliminarPorId(id)
            Unit
        }

    override suspend fun reemplazarTodos(resultados: List<Resultado>): Result<Unit> =
        ejecutar {
            dao.eliminarTodos()
            dao.insertarTodos(resultados.map { it.aEntidad() })
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
