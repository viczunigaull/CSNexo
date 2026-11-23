package com.example.csnexo.backend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * Entidad JPA que representa el resultado de una activación.
 */
@Entity
@Table(name = "resultados")
data class Resultado(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val oportunidad: String,
    val resultado: String,
    val observaciones: String
)
