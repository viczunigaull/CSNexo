package com.example.csnexo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.csnexo.data.local.entity.ResultadoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultadoDao {

    @Query("SELECT * FROM resultados ORDER BY oportunidad ASC")
    fun observarTodos(): Flow<List<ResultadoEntity>>

    @Query("SELECT * FROM resultados WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): ResultadoEntity?

    @Query("SELECT COUNT(*) FROM resultados")
    suspend fun contar(): Int

    @Insert
    suspend fun insertar(resultado: ResultadoEntity): Long

    @Insert
    suspend fun insertarTodos(resultados: List<ResultadoEntity>): List<Long>

    @Update
    suspend fun actualizar(resultado: ResultadoEntity): Int

    @Query("DELETE FROM resultados WHERE id = :id")
    suspend fun eliminarPorId(id: Long): Int

    @Query("DELETE FROM resultados")
    suspend fun eliminarTodos(): Int
}
