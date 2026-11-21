package com.example.csnexo.backend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * Entidad JPA que representa una oportunidad comercial.
 *
 * Los enums del dominio se guardan como texto (el nombre del enum) para que el
 * contrato JSON sea simple y coincida con lo que usa la app móvil.
 * Las fechas se guardan como timestamp en milisegundos (epoch millis).
 */
@Entity
@Table(name = "oportunidades")
data class Oportunidad(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val fuenteInformacion: String,
    val prioridad: String,
    val potencialComercial: String,
    val estadoGestion: String,
    val observaciones: String? = null,
    val creadaEn: Long = 0,
    val actualizadaEn: Long = 0
)
