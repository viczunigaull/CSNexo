package com.example.csnexo.data.repository

import com.example.csnexo.data.local.dao.ContactoDao
import com.example.csnexo.data.mapper.aDominio
import com.example.csnexo.data.mapper.aEntidad
import com.example.csnexo.domain.model.Contacto
import com.example.csnexo.domain.repository.ContactoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación local (Room) del repositorio de contactos.
 */
class ContactoRepositoryImpl(
    private val dao: ContactoDao
) : ContactoRepository {

    override fun observarTodos(): Flow<List<Contacto>> =
        dao.observarTodos().map { lista -> lista.map { it.aDominio() } }

    override suspend fun obtenerPorId(id: Long): Contacto? =
        dao.obtenerPorId(id)?.aDominio()

    override suspend fun crear(contacto: Contacto): Result<Long> =
        ejecutar { dao.insertar(contacto.aEntidad()) }

    override suspend fun actualizar(contacto: Contacto): Result<Unit> =
        ejecutar {
            dao.actualizar(contacto.aEntidad())
            Unit
        }

    override suspend fun eliminar(id: Long): Result<Unit> =
        ejecutar {
            dao.eliminarPorId(id)
            Unit
        }

    override suspend fun reemplazarTodos(contactos: List<Contacto>): Result<Unit> =
        ejecutar {
            dao.eliminarTodos()
            dao.insertarTodos(contactos.map { it.aEntidad() })
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
