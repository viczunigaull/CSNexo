package com.example.csnexo.backend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * Entidad JPA que representa un contacto corporativo ficticio.
 */
@Entity
@Table(name = "contactos")
data class Contacto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val nombre: String,
    val organizacion: String,
    val cargo: String,
    val area: String,
    val correo: String,
    val telefono: String
)
