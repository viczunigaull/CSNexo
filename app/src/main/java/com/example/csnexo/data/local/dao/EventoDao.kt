package com.example.csnexo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.csnexo.data.local.entity.EventoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventoDao {

    @Query("SELECT * FROM eventos ORDER BY fechaInicio ASC")
    fun observarTodos(): Flow<List<EventoEntity>>

    @Query("SELECT * FROM eventos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): EventoEntity?

    @Query("SELECT COUNT(*) FROM eventos")
    suspend fun contar(): Int

    @Insert
    suspend fun insertar(evento: EventoEntity): Long

    @Insert
    suspend fun insertarTodos(eventos: List<EventoEntity>): List<Long>

    @Update
    suspend fun actualizar(evento: EventoEntity): Int

    @Query("DELETE FROM eventos WHERE id = :id")
    suspend fun eliminarPorId(id: Long): Int

    @Query("DELETE FROM eventos")
    suspend fun eliminarTodos(): Int
}
