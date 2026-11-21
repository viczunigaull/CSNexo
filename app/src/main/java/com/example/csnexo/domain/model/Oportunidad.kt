package com.example.csnexo.domain.model

import java.time.Instant

/**
 * Oportunidad comercial del alcance de la primera entrega.
 *
 * No incluye evento, contacto ni responsable: esos vínculos pertenecen al caso
 * completo y se agregarán cuando se amplíe el modelo. `fotoPath` y
 * `ubicacionReferencia` quedan opcionales para el recurso nativo.
 */
data class Oportunidad(
    val id: Long = 0,
    val nombre: String,
    val tipo: TipoOportunidad,
    val descripcion: String,
    val fuenteInformacion: String,
    val prioridad: Prioridad,
    val potencialComercial: PotencialComercial,
    val estadoGestion: EstadoOportunidad,
    val observaciones: String = "",
    val fotoPath: String? = null,
    val ubicacionReferencia: String? = null,
    val creadaEn: Instant,
    val actualizadaEn: Instant
)
