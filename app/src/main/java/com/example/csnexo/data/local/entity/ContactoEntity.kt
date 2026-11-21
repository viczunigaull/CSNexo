package com.example.csnexo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla de contactos. El enum se guarda como texto.
 */
@Entity(tableName = "contactos")
data class ContactoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val organizacion: String,
    val cargo: String,
    val area: String,
    val correo: String,
    val telefono: String
)
