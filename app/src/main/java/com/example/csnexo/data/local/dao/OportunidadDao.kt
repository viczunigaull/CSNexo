package com.example.csnexo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.csnexo.data.local.entity.OportunidadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OportunidadDao {

    @Query("SELECT * FROM oportunidades ORDER BY actualizadaEn DESC")
    fun observarTodas(): Flow<List<OportunidadEntity>>

    @Query("SELECT * FROM oportunidades WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): OportunidadEntity?

    @Query("SELECT COUNT(*) FROM oportunidades")
    suspend fun contar(): Int

    @Insert
    suspend fun insertar(oportunidad: OportunidadEntity): Long

    @Insert
    suspend fun insertarTodas(oportunidades: List<OportunidadEntity>): List<Long>

    @Update
    suspend fun actualizar(oportunidad: OportunidadEntity): Int
}
