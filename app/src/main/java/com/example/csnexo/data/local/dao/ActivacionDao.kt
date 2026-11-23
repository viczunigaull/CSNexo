package com.example.csnexo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.csnexo.data.local.entity.ActivacionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivacionDao {

    @Query("SELECT * FROM activaciones ORDER BY oportunidad ASC")
    fun observarTodas(): Flow<List<ActivacionEntity>>

    @Query("SELECT * FROM activaciones WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): ActivacionEntity?

    @Query("SELECT COUNT(*) FROM activaciones")
    suspend fun contar(): Int

    @Insert
    suspend fun insertar(activacion: ActivacionEntity): Long

    @Insert
    suspend fun insertarTodas(activaciones: List<ActivacionEntity>): List<Long>

    @Update
    suspend fun actualizar(activacion: ActivacionEntity): Int

    @Query("DELETE FROM activaciones WHERE id = :id")
    suspend fun eliminarPorId(id: Long): Int

    @Query("DELETE FROM activaciones")
    suspend fun eliminarTodas(): Int
}
