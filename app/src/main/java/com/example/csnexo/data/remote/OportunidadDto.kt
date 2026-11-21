package com.example.csnexo.data.remote

/**
 * DTO que refleja el JSON del microservicio Spring Boot.
 *
 * Los enums viajan como texto (nombre del enum) y las fechas como epoch millis,
 * exactamente igual que en la base local. Gson mapea los campos por nombre.
 */
data class OportunidadDto(
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
