package com.example.csnexo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resultados")
data class ResultadoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val oportunidad: String,
    val resultado: String,
    val observaciones: String
)
