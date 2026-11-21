package com.example.csnexo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.csnexo.data.local.entity.SeguimientoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeguimientoDao {

    @Query("SELECT * FROM seguimientos ORDER BY fecha DESC")
    fun observarTodos(): Flow<List<SeguimientoEntity>>

    @Query("SELECT * FROM seguimientos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): SeguimientoEntity?

    @Query("SELECT COUNT(*) FROM seguimientos")
    suspend fun contar(): Int

    @Insert
    suspend fun insertar(seguimiento: SeguimientoEntity): Long

    @Insert
    suspend fun insertarTodos(seguimientos: List<SeguimientoEntity>): List<Long>

    @Update
    suspend fun actualizar(seguimiento: SeguimientoEntity): Int

    @Query("DELETE FROM seguimientos WHERE id = :id")
    suspend fun eliminarPorId(id: Long): Int

    @Query("DELETE FROM seguimientos")
    suspend fun eliminarTodos(): Int
}
