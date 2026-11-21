package com.example.csnexo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.csnexo.data.local.entity.OrganizacionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrganizacionDao {

    @Query("SELECT * FROM organizaciones ORDER BY nombre ASC")
    fun observarTodas(): Flow<List<OrganizacionEntity>>

    @Query("SELECT * FROM organizaciones WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): OrganizacionEntity?

    @Query("SELECT COUNT(*) FROM organizaciones")
    suspend fun contar(): Int

    @Insert
    suspend fun insertar(organizacion: OrganizacionEntity): Long

    @Insert
    suspend fun insertarTodas(organizaciones: List<OrganizacionEntity>): List<Long>

    @Update
    suspend fun actualizar(organizacion: OrganizacionEntity): Int

    @Query("DELETE FROM organizaciones WHERE id = :id")
    suspend fun eliminarPorId(id: Long): Int

    @Query("DELETE FROM organizaciones")
    suspend fun eliminarTodas(): Int
}
