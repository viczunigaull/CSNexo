package com.example.csnexo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla de seguimientos. El enum se guarda como texto.
 */
@Entity(tableName = "seguimientos")
data class SeguimientoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val oportunidad: String,
    val tipoAccion: String,
    val fecha: String,
    val resultado: String
)
