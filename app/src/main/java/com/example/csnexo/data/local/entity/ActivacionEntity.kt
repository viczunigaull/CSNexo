package com.example.csnexo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activaciones")
data class ActivacionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val oportunidad: String,
    val condiciones: String,
    val responsable: String
)
