package com.example.csnexo.domain.model

/**
 * Evento (feria, jornada de beneficios, etc.). Las fechas se guardan como texto
 * "yyyy-MM-dd" para simplificar el ingreso en el formulario.
 */
data class Evento(
    val id: Long = 0,
    val nombre: String,
    val tipo: TipoEvento,
    val descripcion: String,
    val fechaInicio: String,
    val fechaTermino: String,
    val comuna: String,
    val region: String
)
