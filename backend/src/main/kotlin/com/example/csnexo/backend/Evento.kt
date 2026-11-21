package com.example.csnexo.backend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * Entidad JPA que representa un evento (feria, jornada de beneficios, etc.).
 * Las fechas se guardan como texto "yyyy-MM-dd" para simplificar el MVP.
 */
@Entity
@Table(name = "eventos")
data class Evento(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val fechaInicio: String,
    val fechaTermino: String,
    val comuna: String,
    val region: String
)
