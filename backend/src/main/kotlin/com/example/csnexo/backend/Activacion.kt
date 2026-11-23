package com.example.csnexo.backend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * Entidad JPA que representa una activación (coordinación de la participación).
 */
@Entity
@Table(name = "activaciones")
data class Activacion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val oportunidad: String,
    val condiciones: String,
    val responsable: String
)
