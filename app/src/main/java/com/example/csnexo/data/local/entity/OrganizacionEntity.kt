package com.example.csnexo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla de organizaciones. El enum se guarda como texto.
 */
@Entity(tableName = "organizaciones")
data class OrganizacionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val tipo: String,
    val rubro: String,
    val comuna: String,
    val region: String
)
