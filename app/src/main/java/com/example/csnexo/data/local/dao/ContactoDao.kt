package com.example.csnexo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.csnexo.data.local.entity.ContactoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactoDao {

    @Query("SELECT * FROM contactos ORDER BY nombre ASC")
    fun observarTodos(): Flow<List<ContactoEntity>>

    @Query("SELECT * FROM contactos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): ContactoEntity?

    @Query("SELECT COUNT(*) FROM contactos")
    suspend fun contar(): Int

    @Insert
    suspend fun insertar(contacto: ContactoEntity): Long

    @Insert
    suspend fun insertarTodos(contactos: List<ContactoEntity>): List<Long>

    @Update
    suspend fun actualizar(contacto: ContactoEntity): Int

    @Query("DELETE FROM contactos WHERE id = :id")
    suspend fun eliminarPorId(id: Long): Int

    @Query("DELETE FROM contactos")
    suspend fun eliminarTodos(): Int
}
