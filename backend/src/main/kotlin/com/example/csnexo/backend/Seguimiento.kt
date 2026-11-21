package com.example.csnexo.backend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * Entidad JPA que representa una acción de seguimiento sobre una oportunidad.
 */
@Entity
@Table(name = "seguimientos")
data class Seguimiento(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val oportunidad: String,
    val tipoAccion: String,
    val fecha: String,
    val resultado: String
)
