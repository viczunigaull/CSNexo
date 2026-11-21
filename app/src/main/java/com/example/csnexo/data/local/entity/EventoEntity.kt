package com.example.csnexo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla de eventos. El enum se guarda como texto.
 */
@Entity(tableName = "eventos")
data class EventoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val fechaInicio: String,
    val fechaTermino: String,
    val comuna: String,
    val region: String
)
