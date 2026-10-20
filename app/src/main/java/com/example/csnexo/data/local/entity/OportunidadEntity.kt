package com.example.csnexo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla de oportunidades de la primera entrega.
 *
 * Los enums se guardan como texto para que la base no dependa del orden de las
 * constantes. La conversión a tipos de dominio ocurre en el mapper.
 */
@Entity(tableName = "oportunidades")
data class OportunidadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val fuenteInformacion: String,
    val prioridad: String,
    val potencialComercial: String,
    val estadoGestion: String,
    val observaciones: String?,
    val fotoPath: String?,
    val ubicacionReferencia: String?,
    val creadaEn: Long,
    val actualizadaEn: Long
)
