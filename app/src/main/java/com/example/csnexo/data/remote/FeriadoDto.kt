package com.example.csnexo.data.remote

/**
 * DTO de la API pública de feriados (https://date.nager.at).
 *
 * Solo se mapean los campos que usa la pantalla; Gson ignora el resto del JSON.
 */
data class FeriadoDto(
    val date: String,
    val localName: String,
    val name: String,
    val countryCode: String
)
